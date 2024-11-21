package com.neko.blog_app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import com.neko.blog_app.model.User;

@Service
public class JwtService {
  private static final String SECRET_KEY = "3f4e30629e81ae5c4f3929184ff4ac33da500e290fb7c914a7575b36f6e17a6434e3601db337949c3dc494a576dd8ee56498180ad2ac19a3af86e8197d208dc38a697f97c0892f5a43449311de258d73548a541702b284c4e3a86a34138ca0a553e5b250cd141387bf0c3b3be4c1d291e3e133061ddfedb7b6683ca735262c8d9dabcaae96e5d7dd7d7d6ba5764103f76dfbccd5269814b369d56dd38226258c755239931c79f9ee3b44e1e9d99a37214ceac89e2ce4d9899f48496b95be7c941597497d0b58fc2f536a33a72b80a3d175cd71f2dba94a4adf21f43ca5742e7d8ed3a3e9be6fdd84a390193889bc90dccc477b86b64b320f493aa89c868da13f";
  private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 100 * 60 * 60 * 10))
        .signWith(key)
        .compact();
  }

  public String validateTokenAndGetUsername(String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();
      return claims.getSubject();
    } catch (Exception e) {
      logger.error("Error in JwtService: ", e);
      return null;
    }
  }

  public Date getExpirationDateFromToken(String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();
      return claims.getExpiration();
    } catch (Exception e) {
      logger.error("Error in JwtService (Get expiration): ", e);
      return null;
    }
  }

  public boolean validateToken(String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
      Jwts.parserBuilder()
         .setSigningKey(key)
         .build()
         .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      logger.error("Error in JwtService (Validate token): ", e);
      return false;
    }
  }

}
