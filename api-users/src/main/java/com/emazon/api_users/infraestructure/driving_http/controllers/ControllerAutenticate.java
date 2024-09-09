package com.emazon.api_users.infraestructure.driving_http.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emazon.api_users.infraestructure.driving_http.dtos.security.AuthenticationRequest;
import com.emazon.api_users.infraestructure.driving_http.dtos.security.AuthenticationResponse;
import com.emazon.api_users.infraestructure.security_config.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class ControllerAutenticate {

    private final AuthenticationService service;

    
    @Operation(summary = "Method for login", 
    description = "This method is for login with the email and password.\n\n" + //
                "rules:\n\n"+//
                "       - The correct username and password must be validated.\n\n" + //
                "       - The number of attempts must be unlimited.\n\n" + //
                "       - Once the session has started, it must be ensured that with that session started, each user has the permissions to perform the actions that correspond to their role."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = ""),
        @ApiResponse(responseCode = "400", description = " Invalid parameter. Possible errors:\n\n" +
                                                          "    - `email`: Cannot be null.\n\n" +
                                                          "    - `password`: cannot by null.\n\n"),
        @ApiResponse(responseCode = "401", description = " Unauthorized\n\n" +
                                                          "    - `credentials': credential invalid or not found user.\n\n")
                                                        
})
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody  @Valid AuthenticationRequest request
    ) {
        return  ResponseEntity.ok(service.authenticate(request));
    }

    }