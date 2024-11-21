package com.neko.blog_app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "comments")
@Data
@Entity
@IdClass(CommentId.class)
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
  @Id
  @ManyToOne
  @JoinColumn(name = "id_blog")
  private Blog blog;

  @Id
  @ManyToOne
  @JoinColumn(name = "id_user")
  private User user;

  @Column
  private String content;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;
}
