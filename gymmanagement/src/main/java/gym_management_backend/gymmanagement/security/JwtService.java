package gym_management_backend.gymmanagement.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;



@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.expiration = expiration;
    }

    public String generateToken(
            UUID userId,
            String email,
            String role,
            UUID gymId
    ) {

        Date now = new Date();

        Date expiryDate =
                new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("role", role)
                .claim(
                        "gymId",
                        gymId != null
                                ? gymId.toString()
                                : null
                )
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }
    public String generateMemberQrToken(
            UUID memberId,
            UUID gymId
    ) {

        Date now = new Date();

        Date expiryDate =
                new Date(
                        now.getTime()
                                + 30L * 24 * 60 * 60 * 1000
                );

        return Jwts.builder()
                .subject(memberId.toString())
                .claim("type", "MEMBER_QR")
                .claim("gymId", gymId.toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public Claims validateMemberQrToken(
            String token
    ) {

        Claims claims =
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        if (!"MEMBER_QR".equals(
                claims.get("type", String.class)
        )) {
            throw new IllegalArgumentException(
                    "Invalid QR token"
            );
        }

        return claims;
    }
    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID extractUserId(String token) {

        return UUID.fromString(
                extractAllClaims(token).getSubject()
        );
    }

    public String extractEmail(String token) {

        return extractAllClaims(token)
                .get("email", String.class);
    }

    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    public UUID extractGymId(String token) {

        String gymId =
                extractAllClaims(token)
                        .get("gymId", String.class);

        if (gymId == null) {
            return null;
        }

        return UUID.fromString(gymId);
    }

    public boolean isTokenValid(String token) {

        try {

            Claims claims = extractAllClaims(token);

            return claims.getExpiration()
                    .after(new Date());

        } catch (Exception e) {

            return false;
        }
    }
}