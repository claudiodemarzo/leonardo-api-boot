package it.leonardo.leonardoapiboot.controller;

import io.sentry.Sentry;
import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.entity.Istituto;
import it.leonardo.leonardoapiboot.service.CittaService;
import it.leonardo.leonardoapiboot.service.IstitutoService;
import it.leonardo.leonardoapiboot.service.ProvinciaService;
import it.leonardo.leonardoapiboot.service.RegioneService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/istituto")
@CrossOrigin(origins = {"https://leonardostart.tk", "https://localhost", "https://buybooks.it"}, allowCredentials = "true")
public class IstitutoController {

    private final Log log = LogFactory.getLog(IstitutoController.class);

    @Autowired
    private IstitutoService service;

    @Autowired
    private CittaService cittaService;

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private RegioneService regioneService;

    @GetMapping
    public ResponseEntity<List<Istituto>> getAll() {
        log.info("Invoked IstitutoController.getAll()");
        List<Istituto> lst = null;
        ResponseEntity<List<Istituto>> resp = null;

        try {
            lst = service.getAll();
            if (lst.isEmpty()) {
                resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                resp = new ResponseEntity<>(lst, HttpStatus.OK);
            }
        } catch (Exception e) {
            Sentry.captureException(e);
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resp;
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Integer id) {
        log.info("Invoked IstitutoController.getById(" + id + ")");
        ResponseEntity<Object> resp = null;

        try {
            Optional<Istituto> i = service.getById(id);
            if (i.isPresent()) resp = new ResponseEntity<>(i.get(), HttpStatus.OK);
            else resp = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Sentry.captureException(e);
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resp;
    }

    @PostMapping
    public ResponseEntity<Object> insertIstituto(@RequestParam("logo") MultipartFile file, @RequestParam("cod_id") String codiceIdentificativo, @RequestParam("nome") String nome, @RequestParam("citta") Integer citta, @RequestParam("lat") Float lat, @RequestParam("lng") Float lng) {
        log.info("Invoked IstitutoController.insertIstituto(" + file + ", " + codiceIdentificativo + ", " + nome + ", " + citta + ", " + lat + ", " + lng + ")");
        ResponseEntity<Object> resp = null;
        Citta c = null;
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        sdf.setLenient(true);

        try {
            Optional<Citta> cittaOptional = cittaService.getById(citta);
            if (cittaOptional.isPresent()) c = cittaOptional.get();
            else resp = new ResponseEntity<>("{\"invalidField\" : \"citta\"}", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Sentry.captureException(e);
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (c != null) {
            Istituto i = new Istituto();

            try {
                String path = "/var/www/bb-temp/istituti/" + nome + sdf.format(gc.getTime()) + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

                file.transferTo(new File(path));
                i.setCitta(c);
                i.setCodiceIdentificativo(codiceIdentificativo);
                i.setNome(nome);
                i.setPathLogo(path);
                i.setLat(lat);
                i.setLng(lng);

                Istituto iSaved = service.save(i);
                iSaved.setPathLogo(null);
                resp = new ResponseEntity<>(iSaved, HttpStatus.CREATED);
            } catch (Exception e) {
                Sentry.captureException(e);
                resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return resp;
    }
}
