package com.example.blog.services.implementations;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Static secret key (securely store this, e.g., in environment variables or config files)
    private String secretKey = "f9b38e974cc0d8f7886b4092b9c477adf1020c39e9cf27e85faabdcd59a201d1fa8abb9ec396a94de252af57c66eee744a5854f33b4b4019f151ed5a560c9b1dd4ad5c7ebc998bb6112dfc545249f26a68c73ae32bf7cec52b60413ad4638d794a52aee6b90e456902e899fa209cd220c06ebfb322a96b47404b4b08b179b6d9941d6169ec09bff8abd40873b6ece514f0a1edd3c6384bc1e27cf072f5f6e68ca22819bd07a630182645baaa90951af02a12113fab3bed4e6b9f9270ab4ad3b5a980bf4f6cbd5b223363f7404131e6f74361df28e53261721ce791f2df506c4c3886d1fbe83c1b90b4589238a94b8b0f4064efaf092ab8c6fa79ca6ba6360a2e";

//    public JwtService() {
//
//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGen.generateKey();
//            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }

    // Generate token with claims
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims) // Set custom claims
                .setSubject(email) // Add subject (email)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Token issue time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 30)) // Expiration: 30 hours
                .signWith(getKey(), SignatureAlgorithm.HS256) // Sign using HS256 and secret key
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract username (email) from token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract specific claim from token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // Use parserBuilder instead of parser (from JJWT 0.11.0+)
                .setSigningKey(getKey()) // Set the signing key for validation
                .build() // Build the parser instance
                .parseClaimsJws(token) // Parse the token into a JWS
                .getBody(); // Extract the body (claims) from the parsed JWS
    }



    // Validate token against user details
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
