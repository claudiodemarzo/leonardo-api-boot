package it.leonardo.leonardoapiboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("setup")
@CrossOrigin(origins = {"https://leonardostart.tk", "https://localhost"}, allowCredentials = "true")
public class SetupController {

	private static Log log = LogFactory.getLog(SetupController.class);

	@Operation(description = "Esegue operazioni di setup, come il setting del token anti-XSRF")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine.")
	})
	@GetMapping
	public ResponseEntity<Object> getXSRFToken(){
		log.info("Invoked SetupController.getXSRFToken()");
		return ResponseEntity.ok("{\"message\" : \"XSRF-Token Set\"}");
	}
}
