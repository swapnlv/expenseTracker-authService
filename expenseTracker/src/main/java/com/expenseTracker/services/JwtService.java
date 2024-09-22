package com.expenseTracker.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Data
@Service
public class JwtService {

    public static final String SECRET="4733be96086a0dd536c5b4c2d614aa5431d73a081787964ce38cfec2785704cc";

    public String extractName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimResolver) {

        final Claims claims=extractAllClaims(token);

        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {

        return Jwts.parser().
                setSigningKey(getSignInKey()).
                build().parseClaimsJws(token).getBody();
    }

    public Key getSignInKey() {

        byte[] keyBytes= Decoders.BASE64.decode(SECRET);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims :: getSubject);
    }

    public Date extractExpirationDate(String token){
        return extractClaim(token,Claims :: getExpiration);
    }

    public Boolean isTokenExpired(String token){
        return extractExpirationDate(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername()) && isTokenExpired(token));
    }

    public String createToken(Map<String, Object> claims,String username){
        return Jwts.builder().
                setClaims(claims).
                setSubject(username).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis()+1000*60+1)).
                signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public String GenerateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
}

