package it.leonardo.leonardoapiboot.controller;

import it.leonardo.leonardoapiboot.entity.Tag;
import it.leonardo.leonardoapiboot.service.TagService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
@CrossOrigin(origins = "*")
public class TagController {

    private final Log log = LogFactory.getLog(TagController.class);

    @Autowired
    private TagService service;

    @GetMapping
    public ResponseEntity<List<Tag>> getAll() {
        log.info("Invoked TagController.getAll()");
        ResponseEntity<List<Tag>> resp = null;

        try {
            List<Tag> tags = service.getAll();
            if (tags.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else resp = new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resp;
    }

    @GetMapping("{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") Integer id) {
        log.info("Invoked TagController.getById("+id+")");
        ResponseEntity<Tag> resp = null;

        try {
            Optional<Tag> tag = service.getById(id);
            if (!tag.isPresent()) resp = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else resp = new ResponseEntity<>(tag.get(), HttpStatus.OK);
        } catch (Exception e) {
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resp;
    }

}
