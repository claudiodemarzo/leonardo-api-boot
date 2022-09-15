package it.leonardo.leonardoapiboot.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.sentry.Breadcrumb;
import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.*;
import it.leonardo.leonardoapiboot.entity.form.*;
import it.leonardo.leonardoapiboot.service.*;
import nu.pattern.OpenCV;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.leonardo.leonardoapiboot.utils.ImageUtils.cropImageSquare;
import static it.leonardo.leonardoapiboot.utils.ImageUtils.encodeWebp;

@RestController
@RequestMapping("/utenti")
//@CrossOrigin(origins = {"http://leonardostart.ddns.net", "http://127.0.0.1:5507", "http://192.168.1.2"}, allowCredentials = "true")
@CrossOrigin(origins = {"https://leonardostart.tk", "https://localhost"}, allowCredentials = "true")
public class UtenteController {

    private final Log log = LogFactory.getLog(UtenteController.class);

    @Autowired
    private UtenteService service;

    @Autowired
    private IstitutoService istitutoService;

    @Autowired
    private HttpSession session;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CittaService cittaService;

    @Autowired
    private UtentiPreferencesService utentiPreferencesService;

    @Autowired
    private UtentePublicInfoService utentePublicInfoService;

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/register")
    @Operation(description = "Esegue la registrazione in db di un utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registrazione effettuata con successo."),
            @ApiResponse(responseCode = "409", description = "Conflitto su campi univoci. Ulteriori informazioni ottenute nel campo 'duplicateField' della risposta"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server.")
    })
    public ResponseEntity<Object> register(RegisterForm form) {
        log.info("Invoked UtenteController.register(" + form + ")");
        if (form.getUsername() == null) return ResponseEntity.badRequest().body("{\"missingField\" : \"username\"}");
        if (form.getEmail() == null) return ResponseEntity.badRequest().body("{\"missingField\" : \"email\"}");
        if (form.getPassword() == null) return ResponseEntity.badRequest().body("{\"missingField\" : \"password\"}");
        if (form.getNome() == null) return ResponseEntity.badRequest().body("{\"missingField\" : \"nome\"}");
        if (form.getCognome() == null) return ResponseEntity.badRequest().body("{\"missingField\" : \"cognome\"}");
        if (form.getDataNascita() == null)
            return ResponseEntity.badRequest().body("{\"missingField\" : \"dataNascita\"}");
        if (form.getGenere() == null) return ResponseEntity.badRequest().body("{\"missingField\" : \"genere\"}");
        if (form.getUsername().length() < 3)
            return ResponseEntity.badRequest().body("{\"invalidField\" : \"username\"}");

        Utente uSaved;
        try {
            Optional<Utente> uByUsername = service.findByUsername(form.getUsername());
            if (uByUsername.isPresent())
                return new ResponseEntity<>("{\"duplicateField\" : \"username\"}", HttpStatus.CONFLICT);
            Pattern pattern = Pattern.compile("^(?=.*[a-z].*)([a-z0-9]{1}[a-z0-9\\.\\_\\-\\!]{2,29})$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(form.getUsername());
            if (!matcher.matches())
                return ResponseEntity.badRequest().body("{\"invalidField\" : \"username\"}");
            Optional<Utente> uByEmail = service.findByEmail(form.getEmail());
            if (uByEmail.isPresent())
                return new ResponseEntity<>("{\"duplicateField\" : \"email\"}", HttpStatus.CONFLICT);

            Utente u = new Utente();
            u.copyFromRegisterForm(form);
            u.setPassword(passwordEncoder.encode(u.getPassword()));
            if (u.getDataNascita().getTime() + 441796964000L >= new GregorianCalendar().getTimeInMillis())
                return ResponseEntity.badRequest().body("{\"invalidField\" : \"dataNascita\"}");

            String confirmToken = UUID.randomUUID().toString();
            u.setConfirmToken(confirmToken);

            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setFrom("leonardo.start0@gmail.com");
            smm.setTo(u.getEmail());
            smm.setSubject("Conferma Email");
            smm.setText("Pijate sto token de conferma zi: " + confirmToken + "\n oppure segui questo collegamento: https://leonardostart.tk/confirmemail?token=" + confirmToken);
            mailSender.send(smm);

            uSaved = service.save(u);

            String token = UUID.randomUUID().toString();

            session.setAttribute("token", token);
            session.setAttribute("userID", uSaved.getUtenteId());
            return ResponseEntity.ok("{\"userID\" : \"" + uSaved.getUtenteId() + "\", \"propic\" : \"" + uSaved.getFoto() + "\"}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Esegue la registrazione tramite Google sul db per un utente")
    @Parameters({
            @Parameter(name = "credential", description = "JWT restituito dal flow di login di Google", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registrazione effettuata con successo."),
            @ApiResponse(responseCode = "200", description = "Utente già registrato, la mail era già verificata, quindi il login è stato effettuato"),
            @ApiResponse(responseCode = "400", description = "L'utente è gia loggato"),
            @ApiResponse(responseCode = "401", description = "JWT non valido."),
            @ApiResponse(responseCode = "409", description = "L'email è associata all'account Google è l'email di un utente già registrato, la quale mail non è verificata"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server.")
    })
    @PostMapping("/googleSignIn")
    public ResponseEntity<Object> googleSignIn(String credential) {
        log.info("Invoked UtenteController.googleSignIn(" + credential + ")");

        if (session.getAttribute("token") != null)
            return ResponseEntity.badRequest().body("{\"error\" : \"Utente già loggato\"}");
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList("330035935477-n072k8tgsb3hd343qm3ft8lefl5ktse5.apps.googleusercontent.com"))
                    .build();
            GoogleIdToken idToken = verifier.verify(credential);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String nome = (String) payload.get("given_name");
                String cognome = (String) payload.get("family_name");
                String username = generateUsername(nome, cognome);
                String password = UUID.randomUUID().toString();
                String foto = (String) payload.get("picture");

                Utente u = new Utente();
                Optional<Utente> lookup = service.findByEmail(email);

                if (lookup.isPresent()) {
                    if (lookup.get().getEmail_confermata()) {
                        session.setAttribute("token", UUID.randomUUID().toString());
                        session.setAttribute("userID", lookup.get().getUtenteId());
                        return ResponseEntity.ok("{\"userID\" : \"" + lookup.get().getUtenteId() + "\", \"propic\" : \"" + lookup.get().getFoto() + "\"}");
                    } else
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("{}");
                }
                u.setEmail(email);

                u.setNome(nome);
                u.setCognome(cognome);
                u.setUsername(username);
                u.setPassword(passwordEncoder.encode(password));
                u.setEmail_confermata(true);
                u.setFoto(foto);
                u.setDataNascita(Date.from(Instant.parse("1970-01-01T00:00:00.00Z")));
                u.setGenere("n");

                String resetToken = UUID.randomUUID().toString();
                u.setResetToken(resetToken);

                Utente uSaved = service.save(u);

                SimpleMailMessage smm = new SimpleMailMessage();
                smm.setFrom("leonardo.start0@gmail.com");
                smm.setTo(u.getEmail());
                smm.setSubject("Registrazione con Google");
                smm.setText("Ti sei registrato con Google, e devi resettare la password, e cambiare la tua data di nascita: Ecco il link di reset: https://leonardostart.tk/reset-password?token=" + resetToken);
                mailSender.send(smm);

                String token = UUID.randomUUID().toString();

                session.setAttribute("token", token);
                session.setAttribute("userID", uSaved.getUtenteId());
                return ResponseEntity.created(null).body("{\"userID\" : \"" + lookup.get().getUtenteId() + "\", \"propic\" : \"" + lookup.get().getFoto() + "\"}");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
            }
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private String generateUsername(String nome, String cognome) {
        String username = nome.toLowerCase().replace(" ", "") + "." + cognome.toLowerCase().replace(" ", "");
        int i = 1;
        while (service.findByUsername(username).isPresent()) {
            username = nome.toLowerCase() + "." + cognome.toLowerCase() + i;
            i++;
        }
        return username;
    }

    @PostMapping("/confirmemail")
    @Operation(description = "Esegue la conferma dell'indirizzo email di un utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email confermata con successo"),
            @ApiResponse(responseCode = "400", description = "Richiesta malformata, vedi json di risposta"),
            @ApiResponse(responseCode = "404", description = "Il token di conferma non si riferisce ad alcun account"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> confirmEmail(String token) {
        log.info("Invoked UtenteController.confirmEmail(" + token + ")");
        if (token.equals("")) return ResponseEntity.badRequest().body("{\"invalidField\" : \"token\"}");

        try {
            Optional<Utente> utenteOptional = service.findByConfirmToken(token);
            if (!utenteOptional.isPresent()) return ResponseEntity.notFound().build();

            Utente u = utenteOptional.get();
            u.setEmail_confermata(true);
            Utente uSaved = service.save(u);

            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    @Operation(description = "Esegue login di un utente, con username e password hashata in sha-256")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Effettuato con successo. Il token di sessione è settato."),
            @ApiResponse(responseCode = "400", description = "Login non effettuato. La richiesta pecca di valori richiesti quali 'username' e 'password'"),
            @ApiResponse(responseCode = "403", description = "L'utente è già connesso a un account"),
            @ApiResponse(responseCode = "404", description = "La combinazione utente/pw non corrisponde a nessun utente."),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> login(LoginForm data) {
        log.info("Invoked UtenteController.login(" + data.getUsername() + ", " + data.getPassword() + ")");
        if (session.getAttribute("userID") == null) {
            if (data.getUsername() == null)
                return new ResponseEntity<>("{\"missingField\" : \"username\"}", HttpStatus.BAD_REQUEST);

            if (data.getPassword() == null)
                return ResponseEntity.badRequest().body("{\"missingField\" : \"password\"}");
            Optional<Utente> u;
            try {
                u = service.findByUsernameOrEmail(data.getUsername());
                if (!u.isPresent()) return ResponseEntity.notFound().build();
                if (!passwordEncoder.matches(data.getPassword(), u.get().getPassword()))
                    return ResponseEntity.notFound().build();
                String token = UUID.randomUUID().toString();
                session.setAttribute("token", token);
                session.setAttribute("userID", u.get().getUtenteId());
                return ResponseEntity.ok().body("{\"userID\" : \"" + u.get().getUtenteId() + "\", \"propic\" : \"" + u.get().getFoto() + "\"}");
            } catch (Exception e) {
                Sentry.captureException(e);
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    @Operation(description = "Esegue logout di un utente, invalidando la sessione")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessione invalidata."),
            @ApiResponse(responseCode = "403", description = "L'utente non è connesso ad alcun account"),

    })
    public ResponseEntity<Object> logout() {
        log.info("Invoked UtenteController.logout()");
        if (session.getAttribute("userID") != null) {
            session.invalidate();
            return ResponseEntity.ok().body("{}");
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/find")
    public ResponseEntity<Boolean> utenteExists(@RequestParam String q, @RequestParam(required = false, defaultValue = "true") Boolean e) {
        log.info("Invoked UtenteController.utenteExists(" + q + ", " + e + ")");
        ResponseEntity<Boolean> resp = null;

        q = q.toLowerCase();
        if (e) {
            if (q.startsWith("@")) {
                Optional<Utente> opt = service.findByUsername(q.substring(1));
                if (opt.isPresent()) resp = new ResponseEntity<>(true, HttpStatus.OK);
                else resp = new ResponseEntity<>(false, HttpStatus.OK);
            } else {
                Optional<Utente> optEmail = service.findByEmail(q);
                if (optEmail.isPresent()) resp = new ResponseEntity<>(true, HttpStatus.OK);
                else {
                    Optional<Utente> optUsername = service.findByUsername(q);
                    if (optUsername.isPresent()) resp = new ResponseEntity<>(true, HttpStatus.OK);
                    else resp = new ResponseEntity<>(false, HttpStatus.OK);
                }
            }
        } else {

        }

        return resp;
    }

    @GetMapping("/getinfo")
    @Operation(description = "Ottiene le informazioni dall'utente dal token di sessione. Se il parametro query 'details' è presente con valore 'true', ottiene tutte le informazioni, anche quelle private. Altrimenti ritorna solo quelle pubbliche")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restituisce l'oggetto Utente/UtentePublicInfo in questione"),
            @ApiResponse(responseCode = "401", description = "Il token di sessione non è settato, di conseguenza non è possibile accedere a questo endpoint."),
            @ApiResponse(responseCode = "500", description = "Errore generico del server.")
    })
    public ResponseEntity<Object> getInfo(@RequestParam(required = false, defaultValue = "false") Boolean details) {
        log.info("Invoked UtenteController.getInfo(" + details + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (details) {
            try {
                Optional<Utente> u = service.findById(Integer.parseInt(session.getAttribute("userID").toString()));
                if (!u.isPresent()) {
                    return ResponseEntity.internalServerError().build();
                }
                return ResponseEntity.ok(u.get());
            } catch (Exception e) {
                Sentry.captureException(e);
                return ResponseEntity.internalServerError().build();
            }
        }
        try {
            Optional<UtentePublicInfo> upi = utentePublicInfoService.getById(Integer.parseInt(session.getAttribute("userID").toString()));
            if (!upi.isPresent()) {
                return ResponseEntity.internalServerError().build();
            }

            UtentePublicInfo upi_ = upi.get();
            upi_.setRecensioniRicevute(recensioneService.getByUtenteRecensito(service.findById(upi_.getId()).get()) == null ? new ArrayList<>() : recensioneService.getByUtenteRecensito(service.findById(upi_.getId()).get()));
            upi_.setRecensioniFatte(recensioneService.getByUtenteRecensore(service.findById(upi_.getId()).get()) == null ? new ArrayList<>() : recensioneService.getByUtenteRecensore(service.findById(upi_.getId()).get()));
            return ResponseEntity.ok(upi_.toMap());
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/public")
    @Operation(description = "Permette di effettuare una modifica delle informazioni pubbliche del profilo di ciascun utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La modifica è stata effettuata, l'utente aggiornato è restituito"),
            @ApiResponse(responseCode = "401", description = "Il token di sessione non è settato, di conseguenza non è possibile accedere a questo endpoint."),
            @ApiResponse(responseCode = "400", description = "Alcuni campi non sono validi, vedi il campo 'invalidField' per ulteriori informazioni"),
            @ApiResponse(responseCode = "409", description = "Campi duplicati trovati, vedi il campo 'duplicatedField' per ulteriori informazioni"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> updatePublic(UpdatePublicForm form) {
        log.info("Invoked UtenteController.updatePublic(" + form + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            Utente u = service.findById(Integer.parseInt(session.getAttribute("userID").toString())).get();

            Optional<Utente> opt = service.findByUsername(form.getUsername());

            String oldUsername = u.getUsername();
            u.copyFromPublicUpdateForm(form);
            String newUsername = u.getUsername();
            if (opt.isPresent() && !oldUsername.equals(newUsername))
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"duplicatedField\":\"username\"}");
            if (form.getIstituto() != null) {
                Optional<Istituto> ist = istitutoService.getById(form.getIstituto());
                if (!ist.isPresent()) return ResponseEntity.badRequest().body("{\"invalidFields\" : \"istituto\"}");
                u.setIstituto(ist.get());
            }
            Utente uSaved = service.save(u);
            return ResponseEntity.ok(uSaved);
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/propic")
    @Operation(description = "Permette di effettuare una modifica della foto del profilo di ciascun utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La modifica è stata effettuata, l'utente aggiornato è restituito"),
            @ApiResponse(responseCode = "401", description = "Il token di sessione non è settato, di conseguenza non è possibile accedere a questo endpoint."),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> updatePropic(@RequestParam String base64, @RequestParam(required = false, defaultValue = "true") Boolean isAvatar, @RequestParam(required = false, defaultValue = "") String avatarJson) {
        log.info("Invoked UtenteController.updatePropic(" + base64 + ", " + isAvatar + ", " + avatarJson + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        Optional<Utente> utente = service.findById(Integer.parseInt(userID));
        if (!utente.isPresent()) {
            return ResponseEntity.internalServerError().build();
        }

        OpenCV.loadLocally();

        String filePath = "/var/www/html/assets/imgs/utenti/" + userID + ".webp";
        File file = new File(filePath);
        File pngFile = new File(filePath + ".png");
        OutputStream png_ostream = null;
        boolean isSVG = base64.contains("data:image/svg");
        boolean isWEBP = base64.contains("data:image/webp");
        if (!isAvatar) base64 = base64.split(",")[1];
        try {
            if (!isWEBP) {
                if (isAvatar || isSVG) {
                    OutputStream stream = new FileOutputStream(filePath);
                    byte[] svgBytes = Base64.decodeBase64(base64);
                    stream.write(svgBytes);
                    TranscoderInput input_svg_image = new TranscoderInput(file.toURI().toURL().toString());
                    png_ostream = new FileOutputStream(pngFile);
                    TranscoderOutput output_png_image = new TranscoderOutput(png_ostream);
                    PNGTranscoder my_converter = new PNGTranscoder();
                    my_converter.transcode(input_svg_image, output_png_image);
                    png_ostream.flush();
                    png_ostream.close();
                    stream.flush();
                    stream.close();
                    file.delete();
                } else {
                    OutputStream stream = new FileOutputStream(pngFile);
                    byte[] imageBytes = Base64.decodeBase64(base64);
                    stream.write(imageBytes);
                    stream.flush();
                    stream.close();
                }
                BufferedImage image = cropImageSquare(ImageIO.read(pngFile));
                BufferedImage imageCopy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

                imageCopy.getGraphics().drawImage(image, 0, 0, null);
                byte[] imageCopyBytes = ((DataBufferByte) imageCopy.getRaster().getDataBuffer()).getData();

                Mat imageMatrix = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
                imageMatrix.put(0, 0, imageCopyBytes);

                imageCopyBytes = encodeWebp(imageMatrix, 50);
                OutputStream webpOutputStream = new FileOutputStream(filePath);
                webpOutputStream.write(imageCopyBytes);
                pngFile.delete();
            } else {
                byte[] webpBytes = Base64.decodeBase64(base64);
                try (OutputStream webpOutputStream = new FileOutputStream(filePath)) {
                    webpOutputStream.write(webpBytes);
                }
            }

            if (isAvatar) {
                png_ostream.flush();
                png_ostream.close();
            }

            Utente u = utente.get();
            u.setFoto(filePath.replace("/var/www/html", ""));
            UtentiPreferences up = utentiPreferencesService.getById(Integer.parseInt(userID)).get();
            JSONObject preferencesJson = new JSONObject(up.getPreferences());
            if (isAvatar) {
                preferencesJson.put("avatar", "true");
                preferencesJson.put("avatarJson", avatarJson);
            } else {
                preferencesJson.put("avatar", "false");

            }
            up.setPreferences(preferencesJson.toString());
            u.copyFromUtentiPreferences(up);

            Utente uSaved = service.save(u);

            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }


    //crop a webp image to a square and return the bytes of the new image


    @PatchMapping("/private")
    @Operation(description = "Permette di effettuare una modifica delle informazioni private del profilo di ciascun utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La modifica è stata effettuata, l'utente aggiornato è restituito"),
            @ApiResponse(responseCode = "400", description = "Alcuni campi non sono validi, vedi il campo 'invalidField' per ulteriori informazioni"),
            @ApiResponse(responseCode = "409", description = "Campi duplicati trovati, vedi il campo 'duplicatedField' per ulteriori informazioni"),
            @ApiResponse(responseCode = "401", description = "Il token di sessione non è settato, di conseguenza non è possibile accedere a questo endpoint."),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> updatePrivate(UpdatePrivateForm form) {
        log.info("Invoked UtenteController.updatePrivate(" + form + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            Utente u = service.findById(Integer.parseInt(session.getAttribute("userID").toString())).get();
            String oldEmail = u.getEmail();
            u.copyFromPrivateUpdateForm(form);
            String newEmail = u.getEmail();

            Optional<Utente> utente = service.findByEmail(newEmail);
            if (utente.isPresent() && !oldEmail.equals(newEmail)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"duplicatedField\":\"email\"}");
            }

            if (form.getCitta() != null) {
                Optional<Citta> c = cittaService.getById(form.getCitta());
                if (!c.isPresent()) return ResponseEntity.badRequest().body("{\"invalidField\" : \"citta\"}");
                u.setCitta(c.get());
            }

            if (!arrayContains(form.getGenere(), new String[]{"m", "f", "b", "a", "n"}))
                return ResponseEntity.badRequest().body("{\"invalidField\" : \"genere\"}");
            GregorianCalendar now = new GregorianCalendar(), dataNascita = new GregorianCalendar();
            dataNascita.setTime(form.getDataNascita());
            if (form.getDataNascita().getTime() + 441796964000L >= new GregorianCalendar().getTimeInMillis() || dataNascita.get(GregorianCalendar.YEAR) < now.get(GregorianCalendar.YEAR) - 100 || dataNascita.get(GregorianCalendar.YEAR) > now.get(GregorianCalendar.YEAR))
                return ResponseEntity.badRequest().body("{\"invalidField\" : \"dataNascita\"}");

            if (!oldEmail.equalsIgnoreCase(newEmail)) {
                String confirmToken = UUID.randomUUID().toString();
                u.setConfirmToken(confirmToken);

                SimpleMailMessage smm = new SimpleMailMessage();
                smm.setFrom("leonardo.start0@gmail.com");
                smm.setTo(u.getEmail());
                smm.setSubject("Conferma Email");
                smm.setText("Pijate sto token de conferma zi: " + confirmToken + "\n oppure segui questo collegamento: https://leonardostart.tk/confirmemail?token=" + confirmToken);
                mailSender.send(smm);
            }

            Utente uSaved = service.save(u);

            return ResponseEntity.ok(uSaved);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean arrayContains(String needle, String[] haystack) {
        for (String hay : haystack)
            if (needle.equals(hay)) return true;
        return false;
    }


    @GetMapping("/preferences")
    @Operation(description = "Permette di ottenere le preferenze relative al sito dell'utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetch Ok"),
            @ApiResponse(responseCode = "401", description = "Il token di sessione non è settato, di conseguenza non è possibile accedere a questo endpoint."),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> getPreferences(@RequestParam(required = false, defaultValue = "") String key) {
        log.info("Invoked UtenteController.getPreferences(" + key + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            int userId = Integer.parseInt(session.getAttribute("userID").toString());
            Optional<UtentiPreferences> utentiPreferencesOptional = utentiPreferencesService.getById(userId);

            if (key.isEmpty()) {
                return ResponseEntity.ok(utentiPreferencesOptional.get().getPreferences());
            } else {
                JSONObject preferences = new JSONObject(utentiPreferencesOptional.get().getPreferences());
                try {
                    Object value = preferences.get(key);
                    if (value instanceof JSONObject) {
                        return ResponseEntity.ok(((JSONObject) value).toString());
                    } else {
                        return ResponseEntity.ok(value);
                    }
                } catch (JSONException e) {
                    Sentry.captureException(e);
                    return ResponseEntity.internalServerError().body("'" + key + "' not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/preferences")
    @Operation(description = "Permette di aggiornare le preferenze relative al sito dell'utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La modifica è stata effettuata, l'entità aggiornata è restituito"),
            @ApiResponse(responseCode = "401", description = "Il token di sessione non è settato, di conseguenza non è possibile accedere a questo endpoint."),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> setPreferences(@RequestParam String key, @RequestParam String preferences) {
        log.info("Invoked UtenteController.setPreferences(" + key + ", " + preferences + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            int userId = Integer.parseInt(session.getAttribute("userID").toString());

            UtentiPreferences up = utentiPreferencesService.getById(userId).get();
            if (key.isEmpty()) {
                return ResponseEntity.badRequest().body("{\"error\" : \"key cannot be empty\"}");
            }
            JSONObject preferencesJson = new JSONObject(up.getPreferences());
            preferencesJson.put(key, preferences);
            up.setPreferences(preferencesJson.toString());
            Utente u = service.findById(userId).get();
            u.copyFromUtentiPreferences(up);
            Utente uSaved = service.save(u);
            return ResponseEntity.ok(uSaved.getPreferencesStr());
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/password")
    @Operation(description = "Permette di aggiornare la password dell'utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La modifica è stata effettuata con successo"),
            @ApiResponse(responseCode = "401", description = "Il token di sessione non è settato, di conseguenza non è possibile accedere a questo endpoint."),
            @ApiResponse(responseCode = "400", description = "La password fornita è errata, o le password non coincidono"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> updatePassword(UpdatePasswordForm form) {
        log.info("Invoked UtenteController.updatePassword(" + form + ")");
        try {
            String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

            if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            if (!form.getNewPassword().equals(form.getConfirmPassword())) return ResponseEntity.badRequest().build();

            Utente u = service.findById(Integer.parseInt(session.getAttribute("userID").toString())).get();
            String password = u.getPassword();

            if (!passwordEncoder.matches(form.getOldPassword(), password)) return ResponseEntity.badRequest().build();

            String newPass = passwordEncoder.encode(form.getNewPassword());

            u.setPassword(newPass);
            Utente uSaved = service.save(u);

            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/resetpassword")
    @Operation(description = "Permette di effettuare il reset della password attraverso l'username o l'email dell'utente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta di reset effettuata con successo, email inviata"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> resetPassword(String query) {
        log.info("Invoked UtenteController.resetPassword(" + query + ")");
        try {
            Optional<Utente> utenteOpt = service.findByUsernameOrEmail(query);
            if (!utenteOpt.isPresent()) return ResponseEntity.ok().build();
            Utente u = utenteOpt.get();

            SimpleMailMessage smm = new SimpleMailMessage();
            String uuid = String.valueOf(UUID.randomUUID());
            u.setResetToken(uuid);

            smm.setFrom("leonardo.start0@gmail.com");
            smm.setTo(u.getEmail());
            smm.setSubject("Reset Password");
            smm.setText("Ecco il link di reset password: https://leonardostart.tk/reset-password?token=" + uuid);

            mailSender.send(smm);
            Utente uSaved = service.save(u);
            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/resetpassword")
    @Operation(description = "Permette di effettuare il reset della password attraverso il token di richiesta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset effettuato con successo"),
            @ApiResponse(responseCode = "404", description = "Non è stato possibile individuare l'utente"),
            @ApiResponse(responseCode = "400", description = "Le password non coincidono, o manca qualche campo richiesto"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> resetPassword(ResetPasswordForm form) {
        log.info("Invoked UtenteController.resetPassword(" + form + ")");
        if (form.getToken() == null) return ResponseEntity.badRequest().body("\"missingField\" : \"token\"");
        if (form.getNewPassword() == null)
            return ResponseEntity.badRequest().body("\"missingField\" : \"newPassword\"");
        if (form.getConfirmPassword() == null)
            return ResponseEntity.badRequest().body("\"missingField\" : \"confirmPassword\"");
        if (!form.getConfirmPassword().equals(form.getNewPassword()))
            return ResponseEntity.badRequest().body("\"invalidField\" : \"confirmPassword\"");

        try {
            Optional<Utente> optionalUtente = service.findByResetToken(form.getToken());
            if (!optionalUtente.isPresent()) return ResponseEntity.notFound().build();
            Utente u = optionalUtente.get();

            String password = passwordEncoder.encode(form.getNewPassword());
            u.setResetToken(null);
            u.setPassword(password);

            Utente uSaved = service.save(u);

            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setFrom("leonardo.start0@gmail.com");
            smm.setTo(u.getEmail());
            smm.setSubject("Password Resettata");
            smm.setText("Uè vedi che la password è stata resettata");
            mailSender.send(smm);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/sendconfirmemail")
    @Operation(description = "Invia una mail di conferma all'utente preso dalla sessione")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine, email inviata"),
            @ApiResponse(responseCode = "401", description = "Il token di sessione non è settato, di conseguenza non è possibile accedere a questo endpoint."),
            @ApiResponse(responseCode = "409", description = "Si sta cercando di mandare una email di conferma per un utente la quale email è già stata confermata."),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> sendConfirmEmail() {
        log.info("Invoked UtenteController.sendConfirmEmail()");
        try {
            String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
            if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            String userID = session.getAttribute("userID").toString();
            Optional<Utente> utenteOptional = service.findById(Integer.parseInt(userID));
            if (!utenteOptional.isPresent()) {
                return ResponseEntity.internalServerError().build();
            }

            Utente u = utenteOptional.get();
            if (u.getEmail_confermata()) return new ResponseEntity<>(HttpStatus.CONFLICT);

            String confirmToken = UUID.randomUUID().toString();
            u.setConfirmToken(confirmToken);

            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setFrom("leonardo.start0@gmail.com");
            smm.setTo(u.getEmail());
            smm.setSubject("Conferma Email");
            smm.setText("Pijate sto token de conferma zi: " + confirmToken + "\n oppure segui questo collegamento: https://leonardostart.tk/confirmemail?token=" + confirmToken);
            mailSender.send(smm);

            Utente uSaved = service.save(u);
            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Ricerca le informazioni pubbliche di un utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine"),
            @ApiResponse(responseCode = "204", description = "Utente non trovato"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @GetMapping("search/{username}")
    public ResponseEntity<List<UtentePublicInfo>> searchUtente(@PathVariable String username) {
        log.info("Invoked UtenteController.searchUtente(" + username + ")");
        try {
            List<UtentePublicInfo> utenti = utentePublicInfoService.searchUsername(username);
            if (utenti.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(utenti);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Ricerca le informazioni pubbliche di un utente, dato l'id o l'username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @GetMapping("{query}")
    public ResponseEntity<Map<Object, Object>> getExact(@PathVariable String query) {
        log.info("Invoked UtenteController.getExact(" + query + ")");
        try {
            UtentePublicInfo upf;
            try {
                Integer userID = Integer.parseInt(query);
                Optional<UtentePublicInfo> upfOptional = utentePublicInfoService.getById(userID);
                if (!upfOptional.isPresent()) return ResponseEntity.notFound().build();
                upf = upfOptional.get();
            } catch (NumberFormatException e) {
                Optional<UtentePublicInfo> upfOptional = utentePublicInfoService.getByUsername(query);
                if (!upfOptional.isPresent()) return ResponseEntity.notFound().build();
                upf = upfOptional.get();
            }
            upf.setRecensioniRicevute(recensioneService.getByUtenteRecensito(service.findById(upf.getId()).get()) == null ? new ArrayList<>() : recensioneService.getByUtenteRecensito(service.findById(upf.getId()).get()));
            return ResponseEntity.ok(upf.toMap());
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
