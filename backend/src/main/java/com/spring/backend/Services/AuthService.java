package com.spring.backend.Services;

import com.spring.backend.DTOs.User.ChangeForgotPasswordRequest;
import com.spring.backend.DTOs.User.RegisterRequest;
import com.spring.backend.DTOs.User.LoginRequest;
import com.spring.backend.DTOs.User.VerifyOtpRequest;
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
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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

    @Autowired
    private MailService mailService;

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

    private String createOtpEmailBody(String otpCode) {
        return String.format("""
            <div style="font-family: Arial, sans-serif; padding: 20px; text-align: center; border: 1px solid #ddd; max-width: 500px; margin: auto;">
                <h2 style="color: #c90000;">Mã Đăng Nhập</h2>
                <p>Đây là mã đăng nhập (OTP) của bạn để đặt lại mật khẩu:</p>
                <div style="font-size: 36px; font-weight: bold; letter-spacing: 5px; margin: 30px 0; padding: 10px; border: 2px dashed #c90000;">
                    %s
                </div>
                <p style="color: #888;">Mã này sẽ sớm hết hạn. Vui lòng sử dụng ngay.</p>
            </div>
            """, otpCode);
    }

    public void sendMailResetPassword(String email) {
        Optional<User> user = reposistory.findByEmail(email);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        String otp = ThreadLocalRandom.current().nextInt(100000, 1000000) + "";
        redisService.storeOtp(email, otp);

        mailService.sendEmail(email, "Yêu cầu đặt lại mật khẩu của bạn", createOtpEmailBody(otp));
    }

    public boolean verifyOtp(VerifyOtpRequest request) {
        return redisService.verifyOtp(request.getEmail(), request.getOtp());
    }

    public void changeForgotPassword(ChangeForgotPasswordRequest request) {
        if (!redisService.verifyOtp(request.getEmail(), request.getOtp())) {
            throw new InvalidCredentialsException("Invalid OTP");
        }

        redisService.removeOtp(request.getEmail());
        User user = reposistory.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        reposistory.save(user);
    }
}
