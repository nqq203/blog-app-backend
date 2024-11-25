package com.neko.blog_app.dto;

import com.neko.blog_app.model.Like;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO {
  private Long id;
  private Long idUser;
  private Long idBlog;

  public LikeDTO(Like like) {
    this.id = like.getId();
    this.idUser = like.getUser().getIdUser();
    this.idBlog = like.getBlog().getIdBlog();
  }
}
