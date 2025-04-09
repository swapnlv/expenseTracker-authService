package com.expenseTracker.controller;

import com.expenseTracker.DTO.requestDTO.AuthRequestDTO;
import com.expenseTracker.DTO.requestDTO.RefreshTokenDTO;
import com.expenseTracker.DTO.responseDTO.JWTResponseDTO;
import com.expenseTracker.entities.RefreshToken;
import com.expenseTracker.services.JwtService;
import com.expenseTracker.services.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@AllArgsConstructor
@RestController
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;


    @Operation(summary = "Logging in the user", description = "Logging in the  user and return Token Reponse")
    @ApiResponse(responseCode = "200", description = "Successfully user logged in")
    @PostMapping("auth/v1/login")
    public ResponseEntity authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){

        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(),authRequestDTO.getPassword()));

        if(authentication.isAuthenticated()){
            Optional<RefreshToken> refreshToken=refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            return new ResponseEntity<>(JWTResponseDTO.builder().
                    accessToken(jwtService.GenerateToken(authRequestDTO.getUsername())).
                    refreshToken(refreshToken.get().getToken()).build(), HttpStatus.OK);


        }else{
            return new ResponseEntity<>("Exception in User Service " , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Refreshing the token ", description = "Refreshing the token and return Token Reponse")
    @ApiResponse(responseCode = "200", description = "Refreshing the user")
    @PostMapping("auth/v1/refreshToken")
    public JWTResponseDTO refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO){
        return refreshTokenService.findByToken(refreshTokenDTO.getToken()).
                map(refreshTokenService:: verifyExpiration).
                map(RefreshToken::getUserInfo).
                map(userInfo ->{
                    String accesToken=jwtService.GenerateToken(userInfo.getUsername());
                    return JWTResponseDTO.builder().accessToken(accesToken).
                            refreshToken(refreshTokenDTO.getToken()).build();

                }).orElseThrow(()->new RuntimeException("Refresh Token is not in DB"));
    }

}
