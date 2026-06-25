package com.java.insurance.app.service;

import com.java.insurance.app.config.security.JwtService;
import com.java.insurance.app.models.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class JwtServiceTest {


    @Autowired
    private JwtService jwtService;

    private static final String SAMPLE_USERNAME = "sample@example.com";
    private String generatedToken;

    @BeforeEach
    void setUp() {
                User user = new User();
        user.setEmail("sample@example.com");
        generatedToken = jwtService.generateToken(user);
    }

    @Test
    void generateToken_Test() {
        User user = new User();
        user.setEmail("sample@example.com");
        generatedToken = jwtService.generateToken(user); // Store the generated token
        assertNotNull(generatedToken, "Generated token should not be null");
        assertTrue(generatedToken.length() > 0, "Generated token should not be empty");
        assertTrue(generatedToken.contains("."), "Token should contain period separators");
    }

    @Test
    void extractUsername_Test() {
        String username = jwtService.extractUsername(generatedToken); // Use the generated token
        assertEquals(SAMPLE_USERNAME, username);
    }

    @Test
    void isTokenValid_Test() {
        // Given
        User user = new User();
        user.setName("sample_username");
        user.setEmail("sample@example.com");
        String validToken = jwtService.generateToken(user);
        boolean isValid = jwtService.isTokenValid(validToken, user);
        assertTrue(isValid, "Valid token should return true");
    }

    @Test
    void isTokenExpired_Test() {
        boolean isExpired = jwtService.isTokenExpired(generatedToken);
        assertFalse(isExpired, "Token should not be expired");
    }

    @Test
    void extractExpirationDate_Test() {

        Date expirationDate = jwtService.extractExpirationDate(generatedToken);
        assertNotNull(expirationDate, "Expiration date should not be null");

    }

    @Test
    void extractAllClaims_Test() {
        Claims claims = jwtService.extractAllClaims(generatedToken);
        assertNotNull(claims, "Claims should not be null");
    }

    @Test
    void extractClaim_Test() {
        Function<Claims, String> claimResolver = Claims::getSubject;
        String subject = jwtService.extractClaim(generatedToken, claimResolver);
        assertNotNull(subject, "Claim should not be null");
    }

    @Test
    void getSignInKey_Test() {
        Key key = jwtService.getSignInKey();

        assertNotNull(key, "Signing key should not be null");
    }

}
