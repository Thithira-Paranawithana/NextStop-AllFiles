
package com.busticket.user_service.service;

import com.busticket.user_service.dto.*;
import com.busticket.user_service.entity.*;
import com.busticket.user_service.exception.UserServiceException;
import com.busticket.user_service.repository.*;
import com.busticket.user_service.security.CustomUserDetails;
import com.busticket.user_service.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final VerificationTokenService tokenService;
    private final EmailService emailService;
    private final NotificationRestClient notificationRestClient;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    @Transactional
    public void register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserServiceException("Email already registered");
        }

        Role userRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new UserServiceException("Default role not found"));

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .emailVerified(false)
                .active(true)
                .build();

        userRepository.save(user);

        notificationRestClient.sendUserRegisteredNotification(user.getEmail());

        String token = tokenService.createVerificationToken(user.getEmail());
        emailService.sendVerificationEmail(user.getEmail(), token);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmailWithRoles(request.getEmail())
                .orElseThrow(() -> new UserServiceException("Invalid credentials"));

        if (!user.isEmailVerified()) {
            throw new UserServiceException("Email not verified. Please verify your email to log in.");
        }

        UserDetails userDetails = new CustomUserDetails(user);
        String jwt = jwtTokenProvider.generateToken(userDetails);
        String refreshToken = generateRefreshToken(user);

        String role = user.getRoles().stream()
                .findFirst()
                .map(Role::getName)
                .orElseThrow(() -> new UserServiceException("User has no roles assigned"));

        return new LoginResponse(user.getId(), jwt, role, user.getFullName(), user.getEmail(), refreshToken);
    }

    private String generateRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .email(user.getEmail())
                .user(user)
                .expiryDate(LocalDateTime.now().plusSeconds(refreshExpiration / 1000))
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Transactional
    public LoginResponse refreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new UserServiceException("Invalid refresh token"));
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new UserServiceException("Refresh token expired");
        }

        User user = userRepository.findByEmailWithRoles(token.getEmail())
                .orElseThrow(() -> new UserServiceException("User not found"));

        UserDetails userDetails = new CustomUserDetails(user);
        String newJwt = jwtTokenProvider.generateToken(userDetails);
        String newRefreshToken = generateRefreshToken(user);
        refreshTokenRepository.delete(token); // Invalidate old refresh token

        String role = user.getRoles().stream()
                .findFirst()
                .map(Role::getName)
                .orElseThrow(() -> new UserServiceException("User has no roles assigned"));

        return new LoginResponse(user.getId(), newJwt, role, user.getFullName(), user.getEmail(), newRefreshToken);
    }

    public void requestPasswordReset(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserServiceException("User not found"));

        String token = tokenService.createPasswordResetToken(user.getEmail());
        emailService.sendPasswordResetEmail(user.getEmail(), token);

        notificationRestClient.sendPasswordResetNotification(user.getEmail());
    }

    public void verifyEmail(String token) {
        String email = tokenService.getEmailFromToken(token, false);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserServiceException("User not found"));

        user.setEmailVerified(true);
        userRepository.save(user);
    }

    @Transactional
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserServiceException("User not found"));
        if (user.isEmailVerified()) {
            throw new UserServiceException("Email already verified");
        }
        String token = tokenService.createVerificationToken(user.getEmail());
        emailService.sendVerificationEmail(user.getEmail(), token);
    }


}