package com.neko.blog_app.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDTO {
  private Long id;
  private Long idBlog;
  private Long idUser;
  private String fullName;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime deletedAt;
}
