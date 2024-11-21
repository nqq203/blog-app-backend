package com.neko.blog_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neko.blog_app.dto.LoginDTO;
import com.neko.blog_app.dto.LoginResponse;
import com.neko.blog_app.service.AuthService;
import com.neko.blog_app.common.ApiResponse;
import com.neko.blog_app.common.AuthFailureResponse;
import com.neko.blog_app.common.BadRequest;
import com.neko.blog_app.common.InternalServerError;
import com.neko.blog_app.common.NotFoundException;
import com.neko.blog_app.common.NotFoundResponse;
import com.neko.blog_app.common.SuccessResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
  @Autowired
  private AuthService authService;

  // Implement login and logout in this endpoint => logout=true (logout), login=false (login)
  @PostMapping("")
  public ResponseEntity<ApiResponse> login(
      @RequestHeader(value = "Authorization", required = false) String token,
      @RequestBody LoginDTO object,
      @RequestParam(value = "logout", required = false, defaultValue = "false") boolean logout) {
    token = token != null && token.startsWith("Bearer ") ? token.substring(7) : null;
    if (logout && token != null) {
      boolean isLoggedOut = authService.logout(token);
      if (isLoggedOut) {
        ApiResponse response = new SuccessResponse("Logged out successfully!", 200);
        return ResponseEntity.status(HttpStatus.OK).body(response);
      }
      ApiResponse response = new AuthFailureResponse("Invalid or expired token!");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    } else if (logout) {
      ApiResponse response = new BadRequest("Invalid logout request!");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    try {
      String email = object.getEmail();
      String username = object.getUsername();
      String password = object.getPassword();

      ApiResponse response;
      if (email.equals("") && username.equals("")) {
        response = new BadRequest("Username or email is required!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }
      if (password.equals("")) {
        response = new BadRequest("Password is required!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }


      String identify = email.equals("") ? username : email;
      LoginResponse res = authService.login(identify, password);
      if (res == null) {
        response = new AuthFailureResponse("Wrong identify (username or email) or password!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
      }
      response = new SuccessResponse("Login successfully!", HttpStatus.OK, res);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
      ApiResponse response = new InternalServerError("Internal Server Error!");
      return ResponseEntity.internalServerError().body(response);
    }
  }

  @DeleteMapping("/{id_user}")
  public ResponseEntity<ApiResponse> lockAccount(@PathVariable("id_user") Long idUser) {
    ApiResponse response;
    try {
      authService.lockAccount(idUser);
      response = new SuccessResponse("Lock user account successfully", 200);
      return ResponseEntity.ok(response);
    } catch (NotFoundException e) {
      response = new NotFoundResponse("Not found " + idUser);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } catch(Exception e) {
      response = new InternalServerError("Internal server error while try to lock user's account");
      return ResponseEntity.internalServerError().body(response);
    }
  }
}
