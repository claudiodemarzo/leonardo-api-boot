package it.leonardo.leonardoapiboot.controller;

import it.leonardo.leonardoapiboot.service.RichiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController("/richieste")
public class RichiesteController {
    @Autowired
    private RichiestaService richiestaService;
}
