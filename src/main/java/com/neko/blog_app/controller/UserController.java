package com.neko.blog_app.controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neko.blog_app.common.ApiResponse;
import com.neko.blog_app.common.BadRequest;
import com.neko.blog_app.common.InternalServerError;
import com.neko.blog_app.common.NotFoundException;
import com.neko.blog_app.common.SuccessResponse;
import com.neko.blog_app.dto.RegistrationDTO;
import com.neko.blog_app.model.User;
import com.neko.blog_app.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping("/users")
  public ResponseEntity<ApiResponse> registerUser(@RequestBody RegistrationDTO object) {
    try {
      String email = object.getEmail();
      String username = object.getUsername();
      String password = object.getPassword();
      String fullName = object.getFullName();
      String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";

      ApiResponse response;
      if (email.equals("") || username.equals("") || password.equals("") || fullName.equals("")) {
        response = new SuccessResponse("Missing information!", 400);
        return ResponseEntity.status(HttpStatus.OK).body(response);
      }

      User user = userService.createUser(email, username, fullName, password);
      if (user == null) {
        response = new SuccessResponse("Email or username already exists!", 400);
        return ResponseEntity.status(HttpStatus.OK).body(response);
      }

      if (!Pattern.matches(PASSWORD_PATTERN, password)) {
        response = new SuccessResponse(
            "Password must contain at least 8 characters, including uppercase and lowercase letters, numbers, and special characters.",
            400);
        return ResponseEntity.status(HttpStatus.OK).body(response);
      }

      response = new SuccessResponse("User registered successfully!", 201);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      ApiResponse response = new InternalServerError("Internal Server Error while try to create user");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @PostMapping("/followers/{id_current_user}")
  public ResponseEntity<ApiResponse> followOrUnfollowUser(
    @PathVariable("id_current_user") Long currentUserId,
    @RequestParam("id_followed_user") Long followedUserId) {
    try {
      ApiResponse response;
      System.out.println("current: " + currentUserId);
      System.out.println("followed: " + followedUserId);
      boolean isFollowAction = userService.updateFollowStatus(currentUserId, followedUserId);
      System.out.println(isFollowAction);
      String message = isFollowAction ? "Followed user successfully!" : "Unfollowed user successfully!";
      response = new SuccessResponse(message, 200);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (NotFoundException e) {
      ApiResponse response = new BadRequest("One or both users not found!");
      return ResponseEntity.badRequest().body(response);
    } catch (Exception e) {
      ApiResponse response = new InternalServerError("Internal Server Error while try to follow/unfollow user");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

    
}
