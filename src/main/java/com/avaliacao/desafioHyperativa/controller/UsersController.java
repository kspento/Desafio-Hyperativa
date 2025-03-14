package com.avaliacao.desafioHyperativa.controller;

import com.avaliacao.desafioHyperativa.dto.CreateUserDTO;
import com.avaliacao.desafioHyperativa.dto.UserDTO;
import com.avaliacao.desafioHyperativa.model.User;
import com.avaliacao.desafioHyperativa.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user",
            description = "This endpoint allows the creation of a new user with the provided user data.")
    @ApiResponse(responseCode = "201", description = "Successfully created the user")
    @PostMapping
    public HttpStatus createUser(@RequestBody CreateUserDTO user) {
         userService.registerUser(user);
        return HttpStatus.CREATED;
    }

    @Operation(summary = "Retrieve a list of all users",
            description = "This endpoint retrieves a list of all users registered in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users")
    @GetMapping
    public List<User> getAll()
    {
        return userService.getAll();
    }
}