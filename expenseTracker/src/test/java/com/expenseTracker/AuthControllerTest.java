package com.expenseTracker;

import com.expenseTracker.DTO.UserDetailDTO;
import com.expenseTracker.DTO.responseDTO.JWTResponseDTO;
import com.expenseTracker.controller.AuthController;
import com.expenseTracker.entities.RefreshToken;
import com.expenseTracker.services.JwtService;
import com.expenseTracker.services.RefreshTokenService;
import com.expenseTracker.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUpWithValidUser() {
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        userDetailDTO.setUsername("validUser");

        when(userServiceImpl.signUp(userDetailDTO)).thenReturn(true);
        when(refreshTokenService.createRefreshToken(anyString())).thenReturn(Optional.of(new RefreshToken()));
        when(jwtService.GenerateToken(anyString())).thenReturn("jwtToken");

        ResponseEntity response = authController.signUp(userDetailDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JWTResponseDTO responseBody = (JWTResponseDTO) response.getBody();
        assertEquals("jwtToken", responseBody.getAccessToken());
    }

    @Test
    void signUpWithExistingUser() {
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        userDetailDTO.setUsername("existingUser");

        when(userServiceImpl.signUp(userDetailDTO)).thenReturn(false);

        ResponseEntity response = authController.signUp(userDetailDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void signUpWithException() {
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        userDetailDTO.setUsername("exceptionUser");

        when(userServiceImpl.signUp(userDetailDTO)).thenThrow(new RuntimeException("Exception"));

        ResponseEntity response = authController.signUp(userDetailDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Exception in User Service: Exception", response.getBody());
    }
}