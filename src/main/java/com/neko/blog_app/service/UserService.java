package com.neko.blog_app.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.neko.blog_app.common.NotFoundException;
import com.neko.blog_app.model.User;
import com.neko.blog_app.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  public User createUser(String email, String username, String fullName, String password) throws Exception {
    try {
      boolean isExistingAccount = userRepository.isExistingAccount(email, username);

      if (isExistingAccount) {
        return null;
      }

      String encodedPassword = passwordEncoder.encode(password);

      User user = new User();
      user.setUsername(username);
      user.setFullName(fullName);
      user.setEmail(email);
      user.setPassword(encodedPassword);
      user.setAvatarUrl("https://th.bing.com/th/id/OIP.b0QZlgB-xbYYDKt9V8QbDwHaHa?w=173&h=180&c=7&r=0&o=5&pid=1.7");
      user.setFollowers(new HashSet<>());
      user.setCreatedAt(LocalDate.now());

      // Setting default roles or other properties
      user.setRoles(Arrays.asList("USER"));

      return userRepository.save(user);
    } catch (Exception e) {
      throw new Exception("Error while register: ", e);
    }
  }

  public User loadUserByUsername(String username) {
    try {
      User user = userRepository.findByUsername(username);
      return user != null ? user : null;
    } catch (Exception e) {
      logger.error("Load username failed: ", e);
      throw e;
    }
  }

  @Transactional
  public boolean updateFollowStatus(Long currentUserId, Long followedUserId) throws Exception {
    try {
      User currentUser = userRepository.findByIdUser(currentUserId);
      User followedUser = userRepository.findByIdUser(followedUserId);
      System.out.println("Find both user and followed user successfully");
      if (currentUser != null && followedUser != null) {
        boolean isCurrentFollowing = followedUser.getFollowers().contains(currentUser);
        if (isCurrentFollowing) {
          followedUser.getFollowers().remove(currentUser);
        } else {
          followedUser.getFollowers().add(currentUser);
        }

        userRepository.saveAndFlush(followedUser);
        return !isCurrentFollowing;
      } else {
        throw new NotFoundException("One or both users not found");
      }
    } catch (Exception e) {
      throw new Exception("Error updating follow status: ", e);
    }
  }
}
