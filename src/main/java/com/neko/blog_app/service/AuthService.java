package com.neko.blog_app.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.neko.blog_app.repository.SessionRepository;
import com.neko.blog_app.repository.UserRepository;
import com.neko.blog_app.utils.Identifier;

import jakarta.transaction.Transactional;

import com.neko.blog_app.common.NotFoundException;
import com.neko.blog_app.dto.LoginResponse;
import com.neko.blog_app.model.Session;
import com.neko.blog_app.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private SessionRepository sessionRepository;

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  public LoginResponse login(String identify, String password) throws Exception {
    try {
      boolean isValidEmail = Identifier.isValidEmail(identify);
      logger.info(" identify: ", identify);

      User user = isValidEmail ? userRepository.findByEmail(identify) : userRepository.findByUsername(identify);
      if (user == null) {
        return null;
      }
      if (!passwordEncoder.matches(password, user.getPassword())) {
        return null;
      }
      String token = this.saveSession(user);
      LoginResponse res = new LoginResponse(token, user);
      return res;
    } catch (Exception e) {
      throw new Exception("Error while login", e);
    }
  }

  public boolean logout(String token) {
    try {
      if (!jwtService.validateToken(token))
        return false;
      if (isTokenBlacklisted(token))
        return false;
      this.deleteSession(token);
      return true;
    } catch (Exception e) {
      logger.error("Error while logout", e);
      return false;
    }
  }

  public void lockAccount(Long idUser) {
    try {
      User user = userRepository.findByIdUser(idUser);
      user.setLockedAt(LocalDate.now());
      userRepository.save(user);
      return;
    } catch (NotFoundException e) {
      throw new NotFoundException("User " + idUser + " not found");
    }
  }

  public boolean isTokenBlacklisted(String token) {
    Session session = sessionRepository.findByToken(token);
    if (session == null)
      return true;
    return false;
  }

  private String saveSession(User user) {
    try {
      String token = jwtService.generateToken(user);
      logger.info("access token: " + token);
      Date expirationDate = jwtService.getExpirationDateFromToken(token);
      Session session = new Session();
      session.setToken(token);
      session.setUser(user);
      session.setCreatedAt(LocalDateTime.now());
      Instant instant = expirationDate.toInstant();
      session.setExpiredAt(instant.atZone(ZoneId.systemDefault()).toLocalDateTime());
      sessionRepository.save(session);
      return token;
    } catch (Exception e) {
      logger.error("Failed to save session for user: {}", user.getUsername(), e);
      return null;
    }
  }

  @Transactional
  private void deleteSession(String token) {
    try {
      Session session = sessionRepository.findByToken(token);
      // if (session == null) return false;
      sessionRepository.delete(session);
      logger.info("Session deleted for token: {}", token);
    } catch (Exception e) {
      logger.error("Failed to delete session for token: {}", token, e);
      throw e;
    }
  }
}