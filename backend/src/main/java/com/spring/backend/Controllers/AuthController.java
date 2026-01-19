package com.spring.backend.Controllers;

import com.spring.backend.DTOs.User.ChangeForgotPasswordRequest;
import com.spring.backend.DTOs.User.LoginRequest;
import com.spring.backend.DTOs.User.RegisterRequest;
import com.spring.backend.DTOs.User.VerifyOtpRequest;
import com.spring.backend.Exceptions.InvalidCredentialsException;
import com.spring.backend.Exceptions.ResourceAlreadyExistedException;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Services.User.AuthService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
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

    @PostMapping("/reset-password-otp")
    public ResponseEntity<?> sendOtp(@RequestBody VerifyOtpRequest request) {
        try {
            service.sendMailResetPassword(request.getEmail());
            return ResponseEntity.ok("OTP sent successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send OTP email: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/otp-verify")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        try {
            return ResponseEntity.ok(service.verifyOtp(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password-change")
    public ResponseEntity<?> changeForgotPassword(@RequestBody ChangeForgotPasswordRequest request) {
        try {
            service.changeForgotPassword(request);
            return ResponseEntity.ok("Password changed successfully");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
