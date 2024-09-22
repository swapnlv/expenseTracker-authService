package com.expenseTracker.services;

import com.expenseTracker.DTO.UserDetailDTO;
import com.expenseTracker.entities.UserInfo;
import com.expenseTracker.repository.UserInfoRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Service
@Data
public class UserServiceImpl implements UserService{

    @Autowired
    ValidateUserService validateUserService;
    @Autowired
    UserInfoRepo userInfoRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserInfoRepo userInfoRepo, PasswordEncoder passwordEncoder) {
        this.userInfoRepo = userInfoRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loasUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user= userInfoRepo.findByUsername(username);

        if(user==null){
            throw new UsernameNotFoundException("user not found");
        }
        else{
            return new CustomUserDetailService(user);
        }
    }

    public UserInfo checkIfUserisPresent(UserDetailDTO userDetails){
        return userInfoRepo.findByUsername(userDetails.getUserName());


    }
    public Boolean signUp(UserDetailDTO userDetailDTO){


        if(validateUserService.validateUser(userDetailDTO)){
            userDetailDTO.setPassword(passwordEncoder.encode(userDetailDTO.getPassword()));
        }else{
            return false;
        }
        if(Objects.isNull(checkIfUserisPresent(userDetailDTO))){

            return false;
        }

        String userId=UUID.randomUUID().toString();
        userInfoRepo.save(new UserInfo(userId, userDetailDTO.getUserName(),userDetailDTO.getPassword(),
                new HashSet<>()));
        return true;
    }
}
