package it.leonardo.leonardoapiboot.controller;


import it.leonardo.leonardoapiboot.entity.Livello;
import it.leonardo.leonardoapiboot.service.LivelloService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livelli")
@CrossOrigin(origins = "*")
public class LivelloController {

    private final Log log = LogFactory.getLog(LivelloController.class);

    @Autowired
    private LivelloService service;

    @GetMapping
    public ResponseEntity<List<Livello>> getAll(){
        log.info("Invoked LivelloController.getAll()");
        ResponseEntity<List<Livello>> resp = null;

        try{
            List<Livello> lst = service.getAll();
            if(!lst.isEmpty()) resp = new ResponseEntity<>(lst, HttpStatus.OK);
            else resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(Exception e){
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resp;
    }

    @GetMapping("{id}")
    public ResponseEntity<Livello> getById(@PathVariable("id") Integer id){
        log.info("Invoked LivelloController.getById(" + id + ")");
        ResponseEntity<Livello> resp = null;

        try{
            Optional<Livello> lvl = service.getById(id);
            if(lvl.isPresent()) resp = new ResponseEntity<>(lvl.get(), HttpStatus.OK);
            else resp = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception e){
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resp;
    }
}
