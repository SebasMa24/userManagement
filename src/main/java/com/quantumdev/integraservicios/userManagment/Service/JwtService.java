package com.quantumdev.integraservicios.userManagment.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String secret = "uEnr0D/2cRjNc+JzXtkJgx/kKYlBo8Gz/4fE1RmiLfY=";

    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    private String getToken(Map<String,Object> extraClaims, UserDetails user){
        List<String> roles = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
    extraClaims.put("roles", roles); 
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
            .signWith(getKey(),SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getKey() {
        byte[] keyBytes= Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);       
    }

    public String getEmailFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean IsTokenValid(String token, UserDetails userDetails) {
        final String email=getEmailFromToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token){
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }
    public boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
}
