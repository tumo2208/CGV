package com.spring.backend.Controllers;

import com.spring.backend.DTOs.User.LoginRequest;
import com.spring.backend.DTOs.User.RegisterRequest;
import com.spring.backend.Exceptions.InvalidCredentialsException;
import com.spring.backend.Exceptions.ResourceAlreadyExistedException;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute RegisterRequest request) {
        try {
            service.register(request);
            return ResponseEntity.ok("User registered successfully");
        } catch (ResourceAlreadyExistedException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading avatar image");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginForm) {
        try {
            String token = service.login(loginForm);

            return ResponseEntity.ok(token);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
