package com.expenseTracker.controller;

import com.expenseTracker.DTO.UserDetailDTO;
import com.expenseTracker.DTO.responseDTO.JWTResponseDTO;
import com.expenseTracker.entities.RefreshToken;
import com.expenseTracker.services.JwtService;
import com.expenseTracker.services.RefreshTokenService;
import com.expenseTracker.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserServiceImpl userServiceImpl;




    @Operation(summary = "Signing Up the user", description = "SignUp the  user and return Token Reponse")
    @ApiResponse(responseCode = "200", description = "Successfully user signed Up")
    @PostMapping("/auth/v1/signUp")
    public ResponseEntity signUp(@RequestBody UserDetailDTO userDetailDTO) {
        System.out.println("Hi from signup controller");
        try {
            System.out.println("Hi from try");
            Boolean isUserSignedUp = userServiceImpl.signUp(userDetailDTO);

            // If the user already exists, return a bad request
            if (!isUserSignedUp) {
                return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
            }

            // Create JWT and refresh token upon successful sign-up
            Optional<RefreshToken> refreshToken = refreshTokenService.createRefreshToken(userDetailDTO.getUsername());
            String  jwtToken = jwtService.GenerateToken(userDetailDTO.getUsername());

            // Return the JWT and refresh token
            return new ResponseEntity<>(JWTResponseDTO.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken.get().getToken())
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            // Log the full stack trace for debugging
            e.printStackTrace();  // Replace this with proper logging in production
            return new ResponseEntity<>("Exception in User Service: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
