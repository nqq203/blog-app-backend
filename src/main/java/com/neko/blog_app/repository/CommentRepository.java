package com.neko.blog_app.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neko.blog_app.dto.CommentResponse;
import com.neko.blog_app.dto.CommentsDTO;
import com.neko.blog_app.model.Blog;
import com.neko.blog_app.model.Comment;
// import com.neko.blog_app.model.CommentId;
import com.neko.blog_app.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  @Query("SELECT new com.neko.blog_app.dto.CommentsDTO(c.id, c.blog.id, c.user.id, c.user.fullName, c.content, c.createdAt, c.deletedAt) "
      +
      "FROM Comment c WHERE c.blog.id = :idBlog AND c.deletedAt IS NULL")
  Page<CommentsDTO> findByBlogAndDeletedAtIsNull(Long idBlog, Pageable pageable);

  void deleteByUserAndBlog(User user, Blog blog);
}
