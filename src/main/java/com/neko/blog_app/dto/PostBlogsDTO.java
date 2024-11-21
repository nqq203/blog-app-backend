package com.neko.blog_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostBlogsDTO {
  private String blogTitle;
  private String blogContent;
  private Long idUser;
}
