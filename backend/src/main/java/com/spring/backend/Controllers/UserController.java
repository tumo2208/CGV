package com.spring.backend.Controllers;


import com.spring.backend.DTOs.User.ChangePasswordRequest;
import com.spring.backend.DTOs.User.RegisterRequest;
import com.spring.backend.Exceptions.ActionNotAllowedException;
import com.spring.backend.Exceptions.InvalidCredentialsException;
import com.spring.backend.Exceptions.ResourceAlreadyExistedException;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Services.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/logout")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            // Get token from header Authorization
            String header = request.getHeader("Authorization");
            String token = null;

            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
            }

            if (token == null) {
                return ResponseEntity.badRequest().body("No Bearer token found");
            }

            service.logout(token);

            return ResponseEntity.ok("Logout successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Logout failed: " + e.getMessage());
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getProfile() {
        try {
            return ResponseEntity.ok(service.profile());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> updateProfile(@ModelAttribute RegisterRequest request) {
        try {
            service.updateProfile(request);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ResourceAlreadyExistedException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading avatar image");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/password")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            service.changePassword(request);
            return ResponseEntity.ok("Password changed successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ActionNotAllowedException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(service.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        try {
            service.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{userId}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable("userId") Long userId, @RequestBody String role) {
        try {
            service.changeRoleUser(userId, role);
            return ResponseEntity.ok("User role changed successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/comment-like/{commentId}")
    public ResponseEntity<?> likeOrUnlikeComment(@PathVariable("commentId") Long commentId) {
        try {
            service.likeOrUnlikeComment(commentId);
            return ResponseEntity.ok("Action completed successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
