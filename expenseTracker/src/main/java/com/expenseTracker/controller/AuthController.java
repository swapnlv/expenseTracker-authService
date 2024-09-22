package com.expenseTracker.controller;

import com.expenseTracker.DTO.UserDetailDTO;
import com.expenseTracker.DTO.responseDTO.JWTResponseDTO;
import com.expenseTracker.entities.RefreshToken;
import com.expenseTracker.services.JwtService;
import com.expenseTracker.services.RefreshTokenService;
import com.expenseTracker.services.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserServiceImpl userServiceImpl;


    @PostMapping("/auth/v1/signUp")
    public ResponseEntity signUp(@RequestBody UserDetailDTO userDetailDTO){
        try{
            Boolean isUserSignedUp = userServiceImpl.signUp(userDetailDTO);
            if (isUserSignedUp) {
                return new ResponseEntity<>("Already existing User", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetailDTO.getUserName());
            String jwtToken = jwtService.GenerateToken(userDetailDTO.getUserName());

            return new ResponseEntity<>(JWTResponseDTO.builder().accessToken(jwtToken).
                    refreshToken(refreshToken.getToken()).build(), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Exception in User Service ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
