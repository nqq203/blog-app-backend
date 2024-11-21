package com.neko.blog_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neko.blog_app.common.ApiResponse;
import com.neko.blog_app.common.InternalServerError;
import com.neko.blog_app.common.SuccessResponse;
import com.neko.blog_app.service.BlogService;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin
public class LikeController {
  @Autowired
  private BlogService blogService;

  @PostMapping("/{id_blog}")
  public ResponseEntity<ApiResponse> likeOrUnlikeBlog(@PathVariable("id_blog") Long idBlog,
      @RequestParam("id_user") Long idUser) {
    ApiResponse response;
    try {
      boolean liked = blogService.likeOrUnlikeBlog(idUser, idBlog);
      String message = liked ? "Blog liked successfully" : "Blog unliked successfully";
      response = new SuccessResponse(message,200);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      response = new InternalServerError("Internal server error while trying to get like blog");
      return ResponseEntity.internalServerError().body(response);
    }
  }

  
}
