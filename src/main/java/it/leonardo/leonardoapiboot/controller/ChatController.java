package it.leonardo.leonardoapiboot.controller;

import io.sentry.Breadcrumb;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Messaggio;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.entity.form.MessaggioWS;
import it.leonardo.leonardoapiboot.entity.form.SendEmbedForm;
import it.leonardo.leonardoapiboot.service.ChatroomService;
import it.leonardo.leonardoapiboot.service.MessaggioService;
import it.leonardo.leonardoapiboot.service.UtentePublicInfoService;
import it.leonardo.leonardoapiboot.service.UtenteService;
import it.leonardo.leonardoapiboot.utils.ChatroomComparator;
import it.leonardo.leonardoapiboot.utils.MessaggiComparator;
import nu.pattern.OpenCV;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static it.leonardo.leonardoapiboot.utils.ImageUtils.cropImageSquare;
import static it.leonardo.leonardoapiboot.utils.ImageUtils.encodeWebp;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatroomService chatroomService;

    @Autowired
    private MessaggioService messaggioService;

    @Autowired
    private HttpSession session;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private UtentePublicInfoService utentePublicInfoService;

    private final Log log = LogFactory.getLog(ChatController.class);


    @Operation(description = "Resitutisce una lista di tutti i messaggi che l'utente richiedente ha scambiato con l'utente specificato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messaggi trovati"),
            @ApiResponse(responseCode = "401", description = "Login non effettuato"),
            @ApiResponse(responseCode = "404", description = "Messaggi non trovati"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<Messaggio>> getMessagesWithUtente(@PathVariable Integer id) {
        log.info("Invoked ChatController.getMessagesWithUtente(" + id + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            Optional<Utente> otherUtenteOptional = utenteService.findById(id);
            if (!otherUtenteOptional.isPresent()) return ResponseEntity.notFound().build();
            Utente dest = otherUtenteOptional.get(), mit = utenteService.findById(Integer.parseInt(userID)).get();
            List<Messaggio> messaggi = new ArrayList<>();
            List<Messaggio> tmp = messaggioService.findByUtenteMitAndUtenteDest(mit, dest);
            if (!tmp.isEmpty()) messaggi.addAll(tmp);
            tmp = messaggioService.findByUtenteMitAndUtenteDest(dest, mit);
            if (!tmp.isEmpty()) messaggi.addAll(tmp);
            for (Messaggio m : messaggi) {
                m.getChatroom().setUtenteMitInfo(m.getChatroom().getUtenteMit().getUtenteId());
            }

            messaggi.sort(new MessaggiComparator());

            return ResponseEntity.ok(messaggi);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }

    }

    @Operation(description = "Restituisce il numero di messaggi non letti nella chat con un utente specificato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messaggi trovati"),
            @ApiResponse(responseCode = "401", description = "Login non effettuato"),
            @ApiResponse(responseCode = "404", description = "Messaggi non trovati"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/{id}/unread")
    public ResponseEntity<Integer> getUnreadMessagesWithUtente(@PathVariable Integer id) {
        log.info("Invoked ChatController.getUnreadMessagesWithUtente(" + id + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            Optional<Utente> otherUtenteOptional = utenteService.findById(id);
            if (!otherUtenteOptional.isPresent()) return ResponseEntity.notFound().build();
            Utente dest = otherUtenteOptional.get(), mit = utenteService.findById(Integer.parseInt(userID)).get();
            Integer count = messaggioService.getUnreadMessagesCount(mit, dest);
            if (count == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Imposta i messaggi ricevuto dall'utente specificato come letti")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messaggi trovati"),
            @ApiResponse(responseCode = "401", description = "Login non effettuato"),
            @ApiResponse(responseCode = "404", description = "Messaggi non trovati"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/{id}/read")
    public ResponseEntity<Object> setMessagesAsRead(@PathVariable Integer id) {
        log.info("Invoked ChatController.setMessagesAsRead(" + id + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            Optional<Utente> otherUtenteOptional = utenteService.findById(id);
            if (!otherUtenteOptional.isPresent()) return ResponseEntity.notFound().build();
            Utente dest = utenteService.findById(Integer.parseInt(userID)).get(), mit = otherUtenteOptional.get();
            messaggioService.setMessagesAsRead(mit, dest);

            Chatroom chatroom = chatroomService.getOrCreate(mit, dest);
            chatroom.setMarkedUnread(false);
            chatroomService.save(chatroom);

            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Restituisce tutte le chat nel quale l'utente è coinvolto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat trovate"),
            @ApiResponse(responseCode = "204", description = "Nessuna chat ancora aperta"),
            @ApiResponse(responseCode = "401", description = "Login non effettuato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/open")
    public ResponseEntity<List<Chatroom>> getOpenChatrooms() {
        log.info("Invoked ChatController.getOpenChatrooms()");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            List<Chatroom> chatrooms = chatroomService.getByUtenteDest(utenteService.findById(Integer.parseInt(userID)).get());
            if (chatrooms.isEmpty()) return ResponseEntity.noContent().build();
            Breadcrumb bBeforeSorting = new Breadcrumb(),
                    bAfterSortingMessages = new Breadcrumb(),
                    bAfterSortingChatroomsPerMessages = new Breadcrumb();

            bBeforeSorting.setData("chatrooms", chatrooms);
            bBeforeSorting.setLevel(SentryLevel.DEBUG);
            bBeforeSorting.setMessage("Chatrooms prima di ordinamento");

            List<Messaggio> messaggi;

            for (Chatroom c : chatrooms) {
                messaggi = new ArrayList<>();
                Chatroom reverse = chatroomService.getOrCreate(c.getUtenteDest(), c.getUtenteMit());

                messaggi.addAll(reverse.getMessaggi());
                messaggi.addAll(c.getMessaggi());

                messaggi.sort(new MessaggiComparator());
                Collections.reverse(messaggi);
                c.getMessaggi().sort(new MessaggiComparator());
                c.setLastMessageDate(messaggi.size() != 0 ? messaggi.get(0).getTimestamp() : null);
            }
            bAfterSortingMessages.setData("chatrooms", chatrooms);
            bAfterSortingMessages.setLevel(SentryLevel.DEBUG);
            bAfterSortingMessages.setMessage("Chatrooms dopo ordinamento di Messaggi");

            for (Chatroom c : chatrooms)
                c.setUtenteMitInfo(utentePublicInfoService.getById(c.getUtenteMit().getUtenteId()).get());

            //chatrooms.forEach(c -> c.getMessaggi().forEach(log::warn));
            for (Chatroom c : chatrooms)
                c.setUnreadMessages(messaggioService.getUnreadMessagesCount(chatroomService.getOrCreate(c.getUtenteMit(), c.getUtenteDest()).getChatroomId()));

            chatrooms.sort(new ChatroomComparator());

            bAfterSortingChatroomsPerMessages.setData("chatrooms", chatrooms);
            bAfterSortingChatroomsPerMessages.setLevel(SentryLevel.DEBUG);
            bAfterSortingChatroomsPerMessages.setMessage("Chatrooms dopo ordinamento");

            Sentry.addBreadcrumb(bBeforeSorting);
            Sentry.addBreadcrumb(bAfterSortingMessages);
            Sentry.addBreadcrumb(bAfterSortingChatroomsPerMessages);

            return ResponseEntity.ok(chatrooms);
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Imposta una chat come non letta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat impostata come non letta"),
            @ApiResponse(responseCode = "404", description = "Chat/Utente non trovati"),
            @ApiResponse(responseCode = "401", description = "Login non effettuato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/{id}/mark-unread")
    public ResponseEntity<Object> setUnread(@PathVariable Integer id) {
        log.info("Invoked ChatController.setUnread()");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            Optional<Utente> otherUtenteOptional = utenteService.findById(id);
            if (!otherUtenteOptional.isPresent())
                return new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);
            Utente dest = otherUtenteOptional.get(), mit = utenteService.findById(Integer.parseInt(userID)).get();
            Chatroom chatroom = chatroomService.getOrCreate(dest, mit);

            chatroom.setMarkedUnread(true);

            chatroomService.save(chatroom);

            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Permette di inviare un'immagine via chat ad un utente")
    @Parameters(value = {
            @Parameter(name = "id", description = "id del destinatario"),
            @Parameter(name = "tipo", description = "tipo dell'embed"),
            @Parameter(name = "payload", description = "payload dell'embed")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Immagine inviata"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato"),
            @ApiResponse(responseCode = "401", description = "Login non effettuato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @PostMapping("/embed")
    public ResponseEntity<Object> sendEmbed(SendEmbedForm form) {
        log.info("Invoked ChatController.sendEmbed("+form.getId()+", "+form.getTipo()+", "+form.getPayload()+")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            Optional<Utente> otherUtenteOptional = utenteService.findById(Integer.parseInt(form.getId()));
            if (!otherUtenteOptional.isPresent())
                return new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);
            Utente dest = otherUtenteOptional.get(), mit = utenteService.findById(Integer.parseInt(userID)).get();
            Chatroom chatroom = chatroomService.getOrCreate(mit, dest);
            Messaggio messaggio = new Messaggio();
            messaggio.setChatroom(chatroom);
            Instant now = Instant.now();
            messaggio.setTimestamp(Date.from(now));
            String filePath = "";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

            switch (form.getTipo().toLowerCase()) {
                case "image":
                case "location":
                    OpenCV.loadLocally();

                    filePath = "/var/www/html/assets/imgs/chat/"+chatroom.getChatroomId()+"/";
                    File file = new File(filePath);
                    file.mkdirs();
                    filePath += sdf.format(Date.from(now)) + ".webp";
                    file = new File(filePath);
                    File pngFile = new File(filePath + ".png");
                    OutputStream png_ostream = null;
                    boolean isSVG = form.getPayload().contains("data:image/svg");
                    boolean isWEBP = form.getPayload().contains("data:image/webp");
                    form.setPayload(form.getPayload().split(",")[1]);
                    if (!isWEBP) {
                        if (isSVG) {
                            OutputStream stream = new FileOutputStream(filePath);
                            byte[] svgBytes = Base64.decodeBase64(form.getPayload());
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
                            byte[] imageBytes = Base64.decodeBase64(form.getPayload());
                            stream.write(imageBytes);
                            stream.flush();
                            stream.close();
                        }
                        BufferedImage image = ImageIO.read(new FileInputStream(pngFile));
                        BufferedImage imageCopy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

                        imageCopy.getGraphics().drawImage(image, 0, 0, null);
                        byte[] imageCopyBytes = ((DataBufferByte) imageCopy.getRaster().getDataBuffer()).getData();

                        Mat imageMatrix = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
                        imageMatrix.put(0, 0, imageCopyBytes);

                        imageCopyBytes = encodeWebp(imageMatrix, 70);
                        OutputStream webpOutputStream = new FileOutputStream(filePath);
                        webpOutputStream.write(imageCopyBytes);
                        pngFile.delete();
                    } else {
                        byte[] webpBytes = Base64.decodeBase64(form.getPayload());
                        try (OutputStream webpOutputStream = new FileOutputStream(filePath)) {
                            webpOutputStream.write(webpBytes);
                        }
                    }
                    messaggio.setMessaggio(filePath.replace("/var/www/html/assets/imgs/chat/", ""));
                    break;
                case "ad":
                    break;
            }

            messaggio.setStatus(0);
            messaggio.setTipo(form.getTipo());

            ChatWSController.sendMessage(chatroom.getUtenteMit(), chatroom.getUtenteDest(), messaggio.getMessaggio(), messaggio.getTipo(), chatroomService, utentePublicInfoService, messaggioService);
            return ResponseEntity.ok("{\"fileName\": \"" + filePath.replace("/var/www/html/assets/imgs/chat/", "") + "\"}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }


}
