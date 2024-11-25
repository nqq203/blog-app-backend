package com.neko.blog_app.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neko.blog_app.model.Like;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.neko.blog_app.model.Blog;
import com.neko.blog_app.model.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogDTO {
  private Long idBlog;
  private String blogTitle;
  private String blogContent;
  private List<LikeDTO> likes;
  private List<Comment> comments;
  private Date createdDate;
  private Date deletedDate;

  public BlogDTO(Blog blog) {
    this.idBlog = blog.getIdBlog();
    this.blogTitle = blog.getBlogTitle();
    this.blogContent = blog.getBlogContent();
    this.likes = blog.getLikes().stream()
        .map(LikeDTO::new)
        .collect(Collectors.toList());
    this.comments = blog.getComments();
    this.createdDate = blog.getCreatedDate();
    this.deletedDate = blog.getDeletedDate();
  }
}
