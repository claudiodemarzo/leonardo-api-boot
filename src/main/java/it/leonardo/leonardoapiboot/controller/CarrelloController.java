package it.leonardo.leonardoapiboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Carrello;
import it.leonardo.leonardoapiboot.entity.Libro;
import it.leonardo.leonardoapiboot.service.AnnunciLibriService;
import it.leonardo.leonardoapiboot.service.CarrelloService;
import it.leonardo.leonardoapiboot.service.LibroService;
import it.leonardo.leonardoapiboot.service.UtenteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carrello")
//@CrossOrigin(origins = {"http://leonardostart.ddns.net", "http://127.0.0.1:5507", "http://192.168.1.2"}, allowCredentials = "true")
@CrossOrigin(origins = "https://leonardostart.tk", allowCredentials = "true")
public class CarrelloController {
    private Log log = LogFactory.getLog(CarrelloController.class);

    @Autowired
    private CarrelloService service;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private AnnunciLibriService annunciLibriService;

    @Autowired
    private HttpSession session;

    @Operation(description = "Restituisce una lista di oggetti Carrello che rappresenta il carrello associato all'utente, ricavato dal token di sessione")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, nel body è presente la lista popolata."),
            @ApiResponse(responseCode = "204", description = "La richiesta è andata a buon fine, ma la lista è vuota"),
            @ApiResponse(responseCode = "403", description = "Utente non connesso")
    })
    @GetMapping
    public ResponseEntity<List<Libro>> getByUtente() {
        log.info("Invoked CarrelloController.getByUtente()");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token != null) {
            List<Carrello> c = service.getByUtente(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get());
            if (c.isEmpty()) return ResponseEntity.noContent().build();
            List<Libro> l = new ArrayList<>();
            for (Carrello cart : c) {
                Libro lib = libroService.findByIsbn(cart.getAnnuncio().getLibro().getIsbn()).get();
                lib.getAnnunci().removeIf(ann -> ann.getAnnuncio_id().intValue() != cart.getAnnuncio().getAnnuncio_id().intValue());
                l.add(lib);
            }
            return ResponseEntity.ok(l);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(description = "Inserisce nel carrello dell'utente, ricavato dal token di sessione, un annuncio il quale id è specificato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "La richiesta è andata a buon fine, la entry del carello è stata creata."),
            @ApiResponse(responseCode = "409", description = "È già presente l'annuncio specificato nel carrello dell'utente."),
            @ApiResponse(responseCode = "404", description = "L'annuncio specificato è inesistente"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido")
    })
    @PostMapping
    public ResponseEntity<Object> insert(Integer annuncio) {
        log.info("Invoked CarrelloController.insert(" + annuncio + ")");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token != null) {
            Optional<AnnunciLibri> annOpt = annunciLibriService.findById(annuncio);
            if (annOpt.isPresent()) {
                try {
                    Carrello c = new Carrello();
                    c.setAnnuncio(annOpt.get());
                    c.setUtente(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get());
                    Carrello cSaved = service.save(c);
                    return new ResponseEntity<>(cSaved, HttpStatus.CREATED);
                } catch (DataIntegrityViolationException ex) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            } else {
                return new ResponseEntity<>("{\"invalid_field\" : \"annuncio\"}", HttpStatus.NOT_FOUND);
            }

        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Operation(description = "Inserisce (se non ancora presente) nel carrello dell'utente, ricavato dal token di sessione, più annunci i quali id sono specificati in un array di Interi")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "La richiesta è andata a buon fine, le entry del carrello sono state create"),
            @ApiResponse(responseCode = "400", description = "L'array fornito è vuoto"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @PostMapping("/multiple")
    public ResponseEntity<Object> insertMultiple(String annunciIn) {
        log.info("Invoked CarrelloController.insert(" + annunciIn+ ")");
        List<Integer> annunci = new ArrayList<>();
        new JSONArray(annunciIn).forEach((val) -> {
            annunci.add((Integer) val);
        });
        if (annunci.size() == 0) return ResponseEntity.badRequest().build();

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token != null) {
            for (Integer annuncio : annunci) {
                Optional<AnnunciLibri> annOpt = annunciLibriService.findById(annuncio);
                if (annOpt.isPresent()) {
                    try {
                        Carrello c = new Carrello();
                        c.setAnnuncio(annOpt.get());
                        c.setUtente(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get());
                        Carrello cSaved = service.save(c);
                    } catch (DataIntegrityViolationException ex) {
                    }
                }
            }
            return ResponseEntity.created(null).build();
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @Operation(description = "Elimina dal carrello dell'utente, ricavato dal token di sessione, tutte o una entry, qualora venga specificato l'id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, la/le entry del carello è/sono state rimossa/e"),
            @ApiResponse(responseCode = "404", description = "La entry specificata è inesistente"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido")
    })
    @Transactional
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestParam(required = false, defaultValue = "-1") Integer annuncio) {
        log.info("Invoked CarrelloController.delete(" + annuncio + ")");
        ResponseEntity<Object> resp = null;

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token != null) {

            if (annuncio == -1) {
                service.deleteAllByUtente(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get());
                return ResponseEntity.ok().build();
            }

            List<Carrello> carrelloList = service.getByUtente(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get());
            boolean found = false;

            for (Carrello c : carrelloList) {
                if (c.getAnnuncio().getAnnuncio_id() == annuncio) {
                    found = true;
                    service.deleteById(c.getEntryId());
                    break;
                }
            }

            if (found) return new ResponseEntity<>("{}", HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


}
