package com.spring.backend.Services;

import com.spring.backend.DTOs.User.RegisterRequest;
import com.spring.backend.DTOs.User.LoginRequest;
import com.spring.backend.Enums.User.Gender;
import com.spring.backend.Enums.User.Role;
import com.spring.backend.Exceptions.InvalidCredentialsException;
import com.spring.backend.Exceptions.MissingRequiredFieldException;
import com.spring.backend.Exceptions.ResourceAlreadyExistedException;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Models.User;
import com.spring.backend.Repositories.UserRepository;
import com.spring.backend.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
    @Autowired
    private UserRepository reposistory;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private StorageService storageService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void register(RegisterRequest request) throws IOException {
        if (request.getEmail() == null || request.getEmail().isEmpty()
                || request.getFullName() == null || request.getFullName().isEmpty()) {
            throw new MissingRequiredFieldException("Email and Full Name are required");
        }

        if (reposistory.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistedException("Email already exists");
        }

        if (request.getPhone() != null && !request.getPhone().isEmpty()
                && reposistory.findByPhone(request.getPhone()).isPresent()) {
            throw new ResourceAlreadyExistedException("Email already exists");
        }

        String avatarUrl = "";

        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            avatarUrl = storageService.uploadImage(request.getAvatar());
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .gender(Gender.valueOf(request.getGender()))
                .dob(request.getDob())
                .address(request.getAddress())
                .district(request.getDistrict())
                .city(request.getCity())
                .role(Role.USER)
                .avatarUrl(avatarUrl)
                .build();

        reposistory.save(newUser);
    }

    public String login(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty()
                || request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new MissingRequiredFieldException("Username and Password are required");
        }

        User user = reposistory.findByEmailOrPhone(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return jwtService.generateToken(user);
    }
}
