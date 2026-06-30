package com.api.crud.jwt;

import com.api.crud.user.UserInfo;
import com.api.crud.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${secret.key.jwt}")
    private static String SECRET_KEY;

    @Autowired
    UserRepository userRepository;

    public String getToken(UserInfo user) {
        Map<String, Object> eClaims = new HashMap<>();
        eClaims.put("id", user.getId());
        eClaims.put("name", user.getName());
        eClaims.put("phone", user.getPhone());
        eClaims.put("role", user.getRole());
        if (user.getClinic() != null) {
            eClaims.put("clinicId", user.getClinic().getId().toString());
        }
        if (user.getDoctor() != null) {
            eClaims.put("doctorPhoto", user.getDoctor().getPhoto());
        }
        return getToken(eClaims, user);
    }

    private String getToken(Map<String, Object> extraClaims, UserInfo user) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserInfo userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    public UUID getClinicIdFromToken(String token) {
        return getClaim(token, claims -> {
            Object clinicId = claims.get("clinicId");
            if (clinicId instanceof String) return UUID.fromString((String) clinicId);
            return null;
        });
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
