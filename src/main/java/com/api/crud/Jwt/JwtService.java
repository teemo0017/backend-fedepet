package com.api.crud.Jwt;

import com.api.crud.models.UserInfo;
import com.api.crud.repositories.repo.IUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    private static final String SECRET_KEY = "NKL68GJI123671213SADA2UG3UI12786SDADGUIOHASUDAUJISBDUJ213312131HJSAD6A7S6D";


    @Autowired
    IUserRepository userRepository;

    public String getToken(UserInfo user) {
        Map<String, Object> eClaims = new HashMap<>();
        eClaims.put("id", user.getId());
        eClaims.put("name", user.getName());
        eClaims.put("phone", user.getPhone());
        eClaims.put("role", user.getRole());
        if (null != user.getDoctor()) {
            eClaims.put("doctorPhoto", user.getDoctor().getPhoto());
        }
        return getToken(eClaims, user);
    }


    private String getToken(Map<String, Object> extraClaims, UserInfo user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
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

    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
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
