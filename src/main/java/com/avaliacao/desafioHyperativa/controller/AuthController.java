package com.avaliacao.desafioHyperativa.controller;

import com.avaliacao.desafioHyperativa.model.LoginRequest;
import com.avaliacao.desafioHyperativa.security.JwtUtil;
import com.avaliacao.desafioHyperativa.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @Operation(
            summary = "User login",
            description = "This endpoint allows the user to authenticate by providing their username and password. A valid JWT token will be returned upon successful authentication.",
            security = @SecurityRequirement(name = "")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated and returned JWT token"),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid credentials or missing authorities")
    })
    @PostMapping("/login")
    @SecurityRequirement(name = "")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            String jwt = authService.authenticateAndGenerateToken(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(jwt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

