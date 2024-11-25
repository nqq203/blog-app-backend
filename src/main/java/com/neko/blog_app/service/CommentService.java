package com.neko.blog_app.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.neko.blog_app.dto.CommentResponse;
import com.neko.blog_app.dto.CommentsDTO;
import com.neko.blog_app.model.Blog;
import com.neko.blog_app.model.Comment;
import com.neko.blog_app.model.User;
import com.neko.blog_app.repository.BlogRepository;
import com.neko.blog_app.repository.CommentRepository;
import com.neko.blog_app.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private BlogRepository blogRepository;
  @Autowired
  private UserRepository userRepository;

  @Transactional
  public Comment createComment(Long idUser, Long idBlog, String content) {
    try {
      User user = userRepository.findByIdUser(idUser);
      Blog blog = blogRepository.findById(idBlog).orElse(null);
      if (user == null || blog == null) {
        throw new IllegalStateException("User or blog not found");
      }

      Comment comment = new Comment();
      comment.setBlog(blog);
      comment.setUser(user);
      comment.setContent(content);
      comment.setCreatedAt(LocalDateTime.now());

      return commentRepository.save(comment);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create comment", e);
    }
  }

  @Transactional
  public Page<CommentsDTO> getCommentByIdBlog(Long idBlog, Pageable pageable) {
    try {
      Blog blog = blogRepository.findById(idBlog).orElse(null);
      if (blog == null) {
        throw new IllegalStateException("Blog not found");
      }
      System.out.println("Qua dayyy");
      return commentRepository.findByBlogAndDeletedAtIsNull(idBlog, pageable);
    } catch (Exception e) {
      System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEee" + e.getMessage());
      throw new RuntimeException("Failed to fetch comments for blog " + idBlog, e);
    }
  }

  @Transactional
  public void deleteComment(Long idUser, Long idBlog) throws IllegalArgumentException {
    try {
      User user = userRepository.findByIdUser(idUser);
      Blog blog = blogRepository.findById(idBlog).orElse(null);
      if (user == null || blog == null) {
        throw new IllegalArgumentException("Not found arguments");
      }

      commentRepository.deleteByUserAndBlog(user, blog);
    } catch (Exception e) {
      throw new RuntimeException("Error deleting comment");
    }
  }
}
