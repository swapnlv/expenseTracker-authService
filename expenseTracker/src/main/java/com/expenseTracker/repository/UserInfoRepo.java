package com.expenseTracker.repository;

import com.expenseTracker.entities.RefreshToken;
import com.expenseTracker.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo, Long> {

        public UserInfo findByUsername(String username);
}
