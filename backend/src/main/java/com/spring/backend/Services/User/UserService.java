package com.spring.backend.Services.User;

import com.spring.backend.DTOs.User.ChangePasswordRequest;
import com.spring.backend.DTOs.User.RegisterRequest;
import com.spring.backend.DTOs.User.UserDTO;
import com.spring.backend.Enums.User.Gender;
import com.spring.backend.Enums.User.Provider;
import com.spring.backend.Enums.User.Role;
import com.spring.backend.Exceptions.ActionNotAllowedException;
import com.spring.backend.Exceptions.InvalidCredentialsException;
import com.spring.backend.Exceptions.ResourceAlreadyExistedException;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Models.User;
import com.spring.backend.Repositories.User.UserRepository;
import com.spring.backend.Security.JwtService;
import com.spring.backend.Services.Common.RedisService;
import com.spring.backend.Services.Common.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private StorageService storageService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void logout(String token) {
        if (token != null) {
            long expiration = jwtService.getExpirationTime(token);
            redisService.blacklistToken(token, expiration);
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public UserDTO profile() {
        return getCurrentUser().convertToDto();
    }

    public void changePassword(ChangePasswordRequest request) {
        User currentUser = getCurrentUser();
        if (currentUser.getProvider().equals(Provider.GOOGLE)) {
            throw new ActionNotAllowedException("Cannot update profile for Google authenticated users.");
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new InvalidCredentialsException("Old password does not match");
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(currentUser);
    }

    public void updateProfile(RegisterRequest updatedUser) throws IOException {
        User currentUser = getCurrentUser();

        if (updatedUser.getPhone() != null && !updatedUser.getPhone().equals(currentUser.getPhone())) {
            Optional<User> existingUser = repository.findByPhone(updatedUser.getPhone());
            if (existingUser.isPresent()) {
                throw new ResourceAlreadyExistedException("User with this phone number already exists.");
            }
            currentUser.setPhone(updatedUser.getPhone());
        }

        if (updatedUser.getFullName() != null) {
            currentUser.setFullName(updatedUser.getFullName());
        }

        if (updatedUser.getAddress() != null) {
            currentUser.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getDistrict() != null) {
            currentUser.setDistrict(updatedUser.getDistrict());
        }
        if (updatedUser.getCity() != null) {
            currentUser.setCity(updatedUser.getCity());
        }

        if (updatedUser.getDob() != null) {
            currentUser.setDob(updatedUser.getDob());
        }

        if (updatedUser.getGender() != null) {
            currentUser.setGender(Gender.valueOf(updatedUser.getGender()));
        }

        if (updatedUser.getAvatar() != null && !updatedUser.getAvatar().isEmpty()) {
            String imageUrl = storageService.uploadImage(updatedUser.getAvatar());
            currentUser.setAvatarUrl(imageUrl);
        }

        repository.save(currentUser);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = repository.findAll();
        return users.stream()
                .map(User::convertToDto)
                .toList();
    }

    public void deleteUser(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        repository.delete(user);
    }

    public void changeRoleUser(Long userId, String role) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setRole(Role.valueOf(role));
        repository.save(user);
    }
}
