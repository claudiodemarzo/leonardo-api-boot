package it.leonardo.leonardoapiboot.controller;

import io.sentry.Sentry;
import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Richiesta;
import it.leonardo.leonardoapiboot.service.AnnunciLibriService;
import it.leonardo.leonardoapiboot.service.RichiestaService;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/richieste")
public class RichiesteController {

    private final Log log = LogFactory.getLog(RichiesteController.class);

    @Autowired
    private RichiestaService richiestaService;

    @Autowired
    private AnnunciLibriService annunciLibriService;

    @Autowired
    private HttpSession session;

    @GetMapping("/{id}")
    public ResponseEntity<Richiesta> getRichiestaById(@PathVariable Integer id) {
        log.info("Invoked RichiesteController.getRichiestaById(" + id + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userid = session.getAttribute("userID").toString();

        try{
            Optional<Richiesta> richiesta = richiestaService.findById(id);
            if (richiesta.isPresent()) {
                if (richiesta.get().getMessaggio().getChatroom().getUtenteDest().getUtenteId().toString().equals(userid)) {
                    return ResponseEntity.ok(richiesta.get());
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            Sentry.captureException(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Richiesta> saveRichiesta(@PathVariable Integer id, Boolean value){
        log.info("Invoked RichiesteController.saveRichiesta("+id+")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userid = session.getAttribute("userID").toString();

        try{
            Optional<Richiesta> richiesta = richiestaService.findById(id);
            if (richiesta.isPresent() && richiesta.get().getStato() == null) {
                if (richiesta.get().getMessaggio().getChatroom().getUtenteDest().getUtenteId().toString().equals(userid)) {
                    Richiesta r = richiesta.get();
                    r.setStato(value ? 1: 0);
                    if(!value){
                        AnnunciLibri al = annunciLibriService.findById(r.getAnnuncio().getAnnuncio_id()).get();
                        al.setStato(1);
                        al.setSoldTo(null);
                        al.setSaleDate(null);
                        annunciLibriService.save(al);
                    }
                    return ResponseEntity.ok(richiestaService.save(richiesta.get()));
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            Sentry.captureException(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
