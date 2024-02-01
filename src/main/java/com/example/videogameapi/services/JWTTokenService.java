package com.example.videogameapi.services;

import com.example.videogameapi.Exceptions.InvalidTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

import static com.example.videogameapi.configuration.DebugConfiguration.getLogger;


@Component
public class JWTTokenService {

    private static final int EXPIRATION = 900_000;
    @Value("${jwt.secret}")
    private String secret;


    public String createToken(String username) {
        byte[] decodedString = Base64.getDecoder().decode(secret.getBytes());
        SecretKey secretKey = new SecretKeySpec(decodedString, 0, decodedString.length, "HMACSHA256");

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION);
        Map<String, String> userClaims = new HashMap<>();
        userClaims.put("username", username);
        JwtBuilder token = Jwts.builder()
                .setClaims(userClaims)
                .setSubject(username)
                .setExpiration(expirationDate)
                .setIssuedAt(now)
                .signWith(secretKey, SignatureAlgorithm.HS256);
        return token.compact();
    }

    public String generateToken(String username) {
        String token = createToken(username);
        try {
            if (validateToken(token, username)) {
                return token;
            } else {
                throw new InvalidTokenException("Token is Invalid.");
            }
        } catch (InvalidTokenException e) {
            getLogger().error("The token provided did not work!");
            return e.getMessage();
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    public String getTokenUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Date getTokenExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    public boolean tokenExpired(Date expiration) {
        return expiration.after(new Date());
    }

    public boolean validateToken(String token, String username) {

        // Confirm the username is the same as what we have on file.
        String currUsername = getTokenUsername(token);
        Date expiration = getTokenExpiration(token);
        // Check if its expired or not.
        return (currUsername.equals(username) && tokenExpired(expiration));
    }

}
