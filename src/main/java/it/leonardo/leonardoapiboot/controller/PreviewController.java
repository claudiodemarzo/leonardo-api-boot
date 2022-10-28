package it.leonardo.leonardoapiboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.PreviewList;
import it.leonardo.leonardoapiboot.service.PreviewListService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/preview")
@CrossOrigin(origins = {"https://leonardostart.tk", "https://localhost", "https://buybooks.it"}, allowCredentials = "true")
public class PreviewController {

    @Autowired
    private PreviewListService previewListService;

    private static final Log log = LogFactory.getLog(PreviewController.class);


    @Operation(summary = "Aggiunge email alla lista preview")
    @Parameters({
            @Parameter(name = "email", description = "L'email da aggiungere", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email aggiunta correttamente"),
            @ApiResponse(responseCode = "400", description = "Email non valida")
    })
    @PostMapping
    public ResponseEntity<PreviewList> register(String email){
        log.info("Invoked PreviewController.register("+email+")");
        if(previewListService.findByEmailIgnoreCase(email).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        PreviewList pl = new PreviewList();
        pl.setTimestamp(Date.from(Instant.now()));
        pl.setEmail(email);
        return ResponseEntity.ok(previewListService.save(pl));
    }
}
