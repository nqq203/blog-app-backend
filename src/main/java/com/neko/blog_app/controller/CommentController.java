package com.neko.blog_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neko.blog_app.common.ApiResponse;
import com.neko.blog_app.common.BadRequest;
import com.neko.blog_app.common.InternalServerError;
import com.neko.blog_app.common.NotFoundResponse;
import com.neko.blog_app.common.SuccessResponse;
import com.neko.blog_app.dto.CommentDTO;
import com.neko.blog_app.dto.CommentResponse;
import com.neko.blog_app.dto.CommentsDTO;
import com.neko.blog_app.model.Comment;
import com.neko.blog_app.service.CommentService;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin
public class CommentController {
  @Autowired
  private CommentService commentService;

  @PostMapping("")
  public ResponseEntity<ApiResponse> postComment(@RequestBody CommentDTO commentDTO) {
    ApiResponse response;
    try {
      Comment comment = commentService.createComment(commentDTO.getIdUser(), commentDTO.getIdBlog(),
          commentDTO.getContent());
      response = new SuccessResponse("Comment successfully!", HttpStatus.OK, comment);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      response = new NotFoundResponse("Invalid data or not found arguments!");
      return ResponseEntity.badRequest().body(response);
    } catch (RuntimeException e) {
      response = new InternalServerError("Internal server error while commenting");
      return ResponseEntity.internalServerError().body(response);
    }
  }

  @GetMapping("")
  public ResponseEntity<ApiResponse> getComments(
      @RequestParam("id_blog") Long idBlog,
      @RequestParam(value = "amount", defaultValue = "5") int size,
      @RequestParam(value = "pag", defaultValue = "0") int page,
      @RequestParam(value = "sort", defaultValue = "createdAt,desc") String sort) {
    ApiResponse response;
    try {
      String[] sortParams = sort.split(",");
      Pageable pageable = PageRequest.of(page, size,
          Sort.by(new Sort.Order(Sort.Direction.valueOf(sortParams[1].toUpperCase()), sortParams[0])));
      Page<CommentsDTO> comments = commentService.getCommentByIdBlog(idBlog, pageable);
      response = new SuccessResponse("Get comments successfully", HttpStatus.OK, comments);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      response = new BadRequest("Illegal request parameters");
      return ResponseEntity.badRequest().body(response);
    } catch (RuntimeException e) {
      response = new InternalServerError("Internal server error while getting comments");
      System.out.println("ERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR" + e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }
  
  @DeleteMapping("")
  public ResponseEntity<ApiResponse> deleteComment(
    @RequestParam("id_user") Long idUser,
    @RequestParam("id_blog") Long idBlog
  ) {
    ApiResponse response;
    try {
      commentService.deleteComment(idUser, idBlog);
      response = new SuccessResponse("Delete comment successfully", 200);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      response = new BadRequest("Invalid request deleting");
      return ResponseEntity.badRequest().body(response);
    } catch (Exception e) {
      response = new InternalServerError("Internal server error while deleting comment");
      return ResponseEntity.internalServerError().body(response);
    }
  }
}
