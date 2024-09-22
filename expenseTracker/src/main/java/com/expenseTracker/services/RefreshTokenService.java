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


    public RefreshToken createRefreshToken(String username) {
        UserInfo userInfoExtracted = userInforepo.findByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder().
                userInfo(userInfoExtracted).
                token(UUID.randomUUID().toString()).
                expiry_time(Instant.now().plusMillis(600000)).
                build();
        return refreshTokenRepo.save(refreshToken);
    }
        public RefreshToken verifyExpiration(RefreshToken refreshToken){
            if(refreshToken.getExpiry_time().compareTo(Instant.now())<0){
                refreshTokenRepo.delete(refreshToken);
                throw new RuntimeException(refreshToken.getToken()+"Refresh Token is expired please create a new Login");
            }

            return refreshToken;
        }

        public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepo.findByToken(token);
        }
    }

