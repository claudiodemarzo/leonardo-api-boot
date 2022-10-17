package it.leonardo.leonardoapiboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import it.leonardo.leonardoapiboot.entity.PreviewList;
import it.leonardo.leonardoapiboot.service.PreviewListService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/preview")
public class PreviewController {

    @Autowired
    private PreviewListService previewListService;

    private static final Log log = LogFactory.getLog(PreviewController.class);


    @Operation(summary = "Add email to preview list")
    @Parameters({
            @Parameter(name = "email", description = "Email to add to preview list", required = true)
    })
    @PostMapping
    public ResponseEntity<PreviewList> register(String email){
        log.info("Invoked PreviewController.register("+email+")");
        if(previewListService.findByEmailIgnoreCase(email).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        PreviewList pl = new PreviewList();
        pl.setEmail(email);
        return ResponseEntity.ok(previewListService.save(pl));
    }
}
