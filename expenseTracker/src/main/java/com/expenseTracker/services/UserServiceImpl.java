package com.expenseTracker.services;

import com.expenseTracker.DTO.UserDetailDTO;
import com.expenseTracker.entities.UserInfo;
import com.expenseTracker.eventProducer.UserInfoProducer;
import com.expenseTracker.repository.UserInfoRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Service
@Data
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    ValidateUserService validateUserService;
    UserInfoRepo userInfoRepo;

    PasswordEncoder passwordEncoder;

    @Autowired
    UserInfoProducer userInfoProducer;

    @Autowired
    public UserServiceImpl(UserInfoRepo userInfoRepo, PasswordEncoder passwordEncoder) {
        this.userInfoRepo = userInfoRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user= userInfoRepo.findByUsername(username);

        if(user==null){
            throw new UsernameNotFoundException("user not found");
        }
        else{
            return new CustomUserDetailService(user);
        }
    }

    public UserInfo checkIfUserisPresent(UserDetailDTO userDetails){
        return userInfoRepo.findByUsername(userDetails.getUsername());


    }
    public Boolean signUp(UserDetailDTO userDetailDTO){
        System.out.println("Hi this is from signUp");

        if(validateUserService.validateUser(userDetailDTO)){
            userDetailDTO.setPassword(passwordEncoder.encode(userDetailDTO.getPassword()));

            System.out.println("Hi this is validated user");
        }else{

            return false;
        }
        if(Objects.isNull(checkIfUserisPresent(userDetailDTO))){
            System.out.println("Hi this is checking if user is present");
            String userId= String.valueOf(UUID.randomUUID());
            userInfoRepo.save(new UserInfo(userId, userDetailDTO.getUsername(),userDetailDTO.getPassword(),
                    new HashSet<>()));

            userInfoProducer.sendEventToKafka(userDetailDTO);
            return true;
        }
        return false;
    }
}
