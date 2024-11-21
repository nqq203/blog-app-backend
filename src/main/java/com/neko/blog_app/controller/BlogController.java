package com.neko.blog_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neko.blog_app.common.ApiResponse;
import com.neko.blog_app.common.BadRequest;
import com.neko.blog_app.common.CreatedResponse;
import com.neko.blog_app.common.InternalServerError;
import com.neko.blog_app.common.NotFoundResponse;
import com.neko.blog_app.common.SuccessResponse;
import com.neko.blog_app.dto.PostBlogsDTO;
import com.neko.blog_app.model.Blog;
import com.neko.blog_app.service.BlogService;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin
public class BlogController {
  @Autowired
  private BlogService blogService;

  @PostMapping("")
  public ResponseEntity<ApiResponse> postABlog(@RequestBody PostBlogsDTO object) {
    try {
      String blogTitle = object.getBlogTitle();
      String blogContent = object.getBlogContent();
      Long idUser = object.getIdUser();

      ApiResponse response;
      if (blogTitle.equals("") || blogContent.equals("")) {
        response = new BadRequest("Missing blog title or blog content");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }
      if (idUser == null) {
        response = new BadRequest("Can not find the blog's owner");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }
      Blog blog = blogService.postABlog(blogTitle, blogContent, idUser);
      if (blog == null) {
        response = new BadRequest("Create blog failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }
      response = new CreatedResponse("Blog posted successfully", blog);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
      ApiResponse response = new InternalServerError(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  @GetMapping("")
  public ResponseEntity<ApiResponse> getBlogsByUser(
      @RequestParam(value = "id_user", required = false) Long idUser,
      @RequestParam(value = "pag", defaultValue = "0") int page,
      @RequestParam(value = "amount", defaultValue = "10") int size,
      @RequestParam(value = "feeds", defaultValue = "false") boolean feeds,
      @RequestParam(value = "sort", defaultValue = "createdDate,desc") String sort) {

    try {
      String[] sortParams = sort.split(",");
      Pageable pageable = PageRequest.of(page, size,
          Sort.by(new Sort.Order(Sort.Direction.valueOf(sortParams[1].toUpperCase()), sortParams[0])));
      Page<Blog> blogs;

      if (feeds) {
        blogs = blogService.findFeedsByUserId(idUser, pageable);
      } else {
        blogs = blogService.findBlogsByUserId(idUser, pageable);
      }

      ApiResponse response = new SuccessResponse("Get blogs successfully", HttpStatus.OK, blogs);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      ApiResponse response = new InternalServerError("Error while getting blogs");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @GetMapping("/{id_blog}")
  public ResponseEntity<ApiResponse> getDetailBlog(@PathVariable("id_blog") Long idBlog) {
    try {
      ApiResponse response;
      Blog blog = blogService.findBlogById(idBlog);
      if (blog == null) {
        response = new NotFoundResponse("Not found blog detail");
      } else {
        response = new SuccessResponse("Fetch blog detail successfully!", HttpStatus.OK, blog);
      }

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      ApiResponse response = new InternalServerError("Internal server error while get a blog detail");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @DeleteMapping("/{id_blog}")
  public ResponseEntity<ApiResponse> softDeleteBlog(@PathVariable("id_blog") Long idBlog) {
    ApiResponse response;
    try {
      blogService.softDeleteBlog(idBlog);
      response = new SuccessResponse("Soft delete blog successfully", 200);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      response = new InternalServerError("Internal server error while try to delete blog");
      return ResponseEntity.internalServerError().body(response);
    }
  }
}
