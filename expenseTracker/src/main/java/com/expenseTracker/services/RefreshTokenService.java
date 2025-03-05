package com.expenseTracker.services;

import com.expenseTracker.entities.RefreshToken;
import com.expenseTracker.entities.UserInfo;
import com.expenseTracker.repository.RefreshTokenRepo;
import com.expenseTracker.repository.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepo refreshTokenRepo;

    @Autowired
    UserInfoRepo userInforepo;

    public Optional<RefreshToken> createRefreshToken(String username) {
        UserInfo userInfoExtracted = userInforepo.findByUsername(username);

        // Check if the user already has a valid refresh token
        Optional<RefreshToken> existingToken = refreshTokenRepo.findByUserInfo(userInfoExtracted);
        if (existingToken.isPresent() && existingToken.get().getExpiry_time().isAfter(Instant.now())) {
            // Return existing token if it's not expired
            return Optional.of(existingToken.get());
        }

        // If no valid token exists, create a new one
        String token;
        RefreshToken refreshToken;
        do {
            token = UUID.randomUUID().toString(); // Generate a unique token
        } while (refreshTokenRepo.existsByToken(token)); // Ensure uniqueness

        refreshToken = RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .token(token)
                .expiry_time(Instant.now().plusMillis(600000)) // Token expiration logic
                .build();

        return Optional.of(refreshTokenRepo.save(refreshToken));
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiry_time().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + " Refresh Token is expired. Please create a new Login.");
        }
        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }
}
