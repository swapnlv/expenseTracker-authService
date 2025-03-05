package com.expenseTracker.repository;

import com.expenseTracker.entities.RefreshToken;
import com.expenseTracker.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RefreshTokenRepo extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    boolean existsByToken(String token);

    Optional<RefreshToken> findByUserInfo(UserInfo userInfoExtracted);
}
