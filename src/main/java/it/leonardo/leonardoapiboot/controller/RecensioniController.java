package it.leonardo.leonardoapiboot.controller;


import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.AnnuncioWithoutRecensione;
import it.leonardo.leonardoapiboot.entity.Recensione;
import it.leonardo.leonardoapiboot.entity.form.InserisciRecensioneForm;
import it.leonardo.leonardoapiboot.service.AnnunciLibriService;
import it.leonardo.leonardoapiboot.service.AnnuncioWithoutRecensioneService;
import it.leonardo.leonardoapiboot.service.RecensioneService;
import it.leonardo.leonardoapiboot.service.UtenteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/recensioni")
public class RecensioniController {

    private final Log log = LogFactory.getLog(RecensioniController.class);

    @Autowired
    private AnnuncioWithoutRecensioneService annuncioWithoutRecensioneService;

    @Autowired
    private AnnunciLibriService annunciLibriService;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private HttpSession session;


    @Operation(description = "Inserisce una recensione per un annuncio, dato l'id dell'annuncio e la recensione.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine"),
            @ApiResponse(responseCode = "403", description = "Utente non connesso"),
            @ApiResponse(responseCode = "404", description = "Annuncio non trovato / annuncio non corrispondente all'utente"),
            @ApiResponse(responseCode = "409", description = "Recensione già presente"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @PostMapping("/{id}")
    public ResponseEntity<Object> inserisciRecensione(@PathVariable Integer id, InserisciRecensioneForm form) {
        log.info("Invoked RecensioniController.inserisciRecensione(" + id + ", " + form + ")");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userid = session.getAttribute("userID").toString();

        try {
            Optional<AnnuncioWithoutRecensione> annuncio = annuncioWithoutRecensioneService.findByAnnuncioId(id);
            if (annuncio.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            AnnuncioWithoutRecensione ann = annuncio.get();
            if (!ann.getSold_to().equals(Integer.parseInt(userid))) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            Recensione r = new Recensione();
            r.setUtenteRecensito(utenteService.findById(ann.getUtente()).get());
            r.setUtenteRecensore(utenteService.findById(Integer.parseInt(userid)).get());
            r.setAnnuncio(annunciLibriService.findById(ann.getAnnuncioId()).get());
            r.setCommento(form.getCommento());
            r.setVoto(form.getVoto());

            Recensione rSaved = recensioneService.save(r);

            return ResponseEntity.ok(rSaved);
        } catch (Exception e) {
            Sentry.captureException(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
