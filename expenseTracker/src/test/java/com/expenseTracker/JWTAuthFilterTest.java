package com.expenseTracker;

import com.expenseTracker.Auth.JWTAuthFilter;
import com.expenseTracker.services.JwtService;
import com.expenseTracker.services.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JWTAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JWTAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doFilterInternalWithValidToken() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/somePath");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("validUser");
        UserDetails userDetails = mock(UserDetails.class);
        when(userServiceImpl.loadUserByUsername("validUser")).thenReturn(userDetails);
        when(jwtService.validateToken("validToken", userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, times(1)).extractUsername("validToken");
        verify(userServiceImpl, times(1)).loadUserByUsername("validUser");
        verify(jwtService, times(1)).validateToken("validToken", userDetails);
    }

//    @Test
//    void doFilterInternalWithInvalidToken() throws ServletException, IOException {
//        // Mock the request to return a path and authorization header
//        when(request.getServletPath()).thenReturn("/somePath");
//        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
//
//        // Ensure the username extraction returns the expected username
//        when(jwtService.extractUsername("invalidToken")).thenReturn("invalidUser");
//
//        // Mock UserDetails to be returned by loadUser ByUsername
//        UserDetails userDetails = mock(UserDetails.class);
//        when(userServiceImpl.loadUserByUsername("invalidUser")).thenReturn(userDetails);
//
//        // Simulate invalid token scenario
//        when(jwtService.validateToken("invalidToken", userDetails)).thenReturn(false);
//
//        // Call the filter method
//        jwtAuthFilter.doFilterInternal(request, response, filterChain);
//
//        // Ensure interactions occur as expected
//        verify(jwtService, times(1)).extractUsername("invalidToken");
//        verify(userServiceImpl, times(1)).loadUserByUsername("invalidUser");  // Confirm this is called
//        verify(jwtService, times(1)).validateToken("invalidToken", userDetails);
//        verify(filterChain, times(1)).doFilter(request, response);  // The filter chain should proceed
//    }
    @Test
    void doFilterInternalWithNoToken() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/somePath");
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(any());
        verify(userServiceImpl, never()).loadUserByUsername(any());
        verify(jwtService, never()).validateToken(any(), any());
    }

    @Test
    void doFilterInternalWithBypassedUrl() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/auth/v1/signUp");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(any());
        verify(userServiceImpl, never()).loadUserByUsername(any());
        verify(jwtService, never()).validateToken(any(), any());
    }
}