package com.neko.blog_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FollowerDTO {
  private Long idUser;
  private String username;
  private String fullName;
  private String email;
  private String avatarUrl;
}
