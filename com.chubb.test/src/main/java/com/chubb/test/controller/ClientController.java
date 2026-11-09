package com.chubb.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.test.model.dtos.ClientDto;
import com.chubb.test.model.dtos.ServiceResponse;
import com.chubb.test.service.IClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@CrossOrigin(origins = { "http://localhost:8080" })
@RequestMapping("/api")
@Tag(
    name = "Client Management",
    description = "API for managing client information"
)
public class ClientController {

	@Autowired
	private IClientService clientService;
	
	@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/clients")
	 @Operation(
        summary = "Get all clients",
        description = "Retrieves a list containing all registered clients."
    )
	@ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Clients successfully retrieved",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClientDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    public ResponseEntity<List<ClientDto>> getClients() {
    	return ResponseEntity.ok(clientService.findAll());
    	
    }

	@CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/client")
	@Operation(
        summary = "Save a new client",
        description = "Registers a new client using the information provided."
    )
	@ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Client successfully saved",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ServiceResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid client information",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    public ResponseEntity<ServiceResponse> saveClient(
		 @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Client information to be registered",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ClientDto.class)
            )
        )
		@RequestBody ClientDto client) {
    	ServiceResponse serviceReponse = new ServiceResponse();
    	int result = clientService.save(client);
    	if (result == 1) {
    		serviceReponse.setSuccess(true);
	    	serviceReponse.setMessage("Client saved with sucesss");
    	}
    	
    	return new ResponseEntity<>(serviceReponse, HttpStatus.OK);
    	
    }

	@CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/client")
	 @Operation(
        summary = "Delete a client by id",
        description = "Deletes a client using its id."
    )
	@ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Client successfully deleted",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ServiceResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Client not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ServiceResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    public ResponseEntity<ServiceResponse> deleteClient(
		@Parameter(
            description = "Client's identifier",
            required = true,
            example = "1"
        )
        @PathVariable int idClient) {
    	ServiceResponse serviceReponse = new ServiceResponse();
    	int result = clientService.delete(idClient);
    	
		if (result == 1) {
    		return ResponseEntity.noContent().build();
    	} else {
			serviceReponse.setSuccess(false);
	    	serviceReponse.setMessage("Client with id: [" + idClient + "] not found");
			HttpStatus status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(serviceReponse, status);
		}
    	
    }
	
}
