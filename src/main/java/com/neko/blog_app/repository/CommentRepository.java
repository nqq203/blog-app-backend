package com.neko.blog_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neko.blog_app.model.Blog;
import com.neko.blog_app.model.Comment;
import com.neko.blog_app.model.CommentId;
import com.neko.blog_app.model.User;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CommentRepository extends JpaRepository<Comment, CommentId> {
  Page<Comment> findByBlogAndDeletedAtIsNull(Blog blog, Pageable pageable);
  @Transactional
  void deleteByUserAndBlog(User user, Blog blog);
}
