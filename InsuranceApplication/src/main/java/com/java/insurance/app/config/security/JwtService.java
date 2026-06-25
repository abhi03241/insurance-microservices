package com.java.insurance.app.config.security;

import com.java.insurance.app.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token from which to extract the username.
     * @return The extracted username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generates a JWT token with additional claims and expiration for the given user.
     *
     * @param extraClaims Additional claims to include in the token.
     * @param user        The user for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts.builder().setClaims(extraClaims).setSubject(user.getEmail()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 1000)).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        return generateToken(extraClaims, user);
    }

    /**
     * Checks if a JWT token is valid for the given user.
     *
     * @param token The JWT token to validate.
     * @param user  The user against whom to validate the token.
     * @return True if the token is valid for the user, false otherwise.
     */
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Checks if a JWT token is expired.
     *
     * @param token The JWT token to check for expiration.
     * @return True if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date(System.currentTimeMillis()));
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token from which to extract the expiration date.
     * @return The expiration date of the token.
     */
    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token from which to extract the claims.
     * @return All claims extracted from the token.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Extracts a specific claim from the JWT token using the given resolver function.
     *
     * @param token          The JWT token from which to extract the claim.
     * @param claimsResolver The function to resolve the desired claim from the claims set.
     * @param <T>            The type of the claim value.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * Retrieves the signing key used for JWT token verification.
     *
     * @return The signing key.
     */
    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
