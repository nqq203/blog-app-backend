package com.neko.blog_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.neko.blog_app.model.Blog;
import com.neko.blog_app.model.Like;
import com.neko.blog_app.model.User;
import com.neko.blog_app.repository.BlogRepository;
import com.neko.blog_app.repository.LikeRepository;
import com.neko.blog_app.repository.UserRepository;

import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BlogService {
  @Autowired
  private BlogRepository blogRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private LikeRepository likeRepository;

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  public Blog postABlog(String blogTitle, String blogContent, Long idUser) {
    try {
      Blog blog = new Blog();
      User user = userRepository.findByIdUser(idUser);
      if (user == null) {
        return null;
      }
      blog.setBlogTitle(blogTitle);
      blog.setBlogContent(blogContent);
      blog.setUser(user);
      return blogRepository.save(blog);
    } catch (Exception e) {
      logger.error("error in post a blog", e);
      return null;
    }
  }

  public Page<Blog> findBlogsByUserId(Long userId, Pageable pageable) {
    try {
      return blogRepository.findByUserIdUser(userId, pageable);
    } catch (Exception e) {
      throw new RuntimeException("Failed to fetch blogs for user " + userId, e);
    }
  }

  public Page<Blog> findFeedsByUserId(Long userId, Pageable pageable) {
    try {
      return blogRepository.findFeedsByIdUser(userId, pageable);
    } catch (Exception e) {
      throw new RuntimeException("Failed to fetch feed for user " + userId, e);
    }
  }

  public Blog findBlogById(Long idBlog){
    try {
      Optional<Blog> blog = blogRepository.findById(idBlog);
      if (blog == null) {
        return null;
      }
      return blog.get();
    } catch (Exception e) {
      throw new RuntimeException("Failed to fetch blog by id " + idBlog, e);
    }
  }

  @Transactional
  public boolean likeOrUnlikeBlog(Long idUser, Long idBlog) {
    try {
      User user = userRepository.findByIdUser(idUser);
      Blog blog = blogRepository.findById(idBlog).orElse(null);
      if (user == null || blog == null) {
        throw new IllegalArgumentException("Not found user or blog");
      }

      boolean exists = likeRepository.existsByUserAndBlog(user, blog);
      if (exists) {
        likeRepository.deleteByUserAndBlog(user, blog);
        return false;
      } else {
        Like like = new Like();
        like.setUser(user);
        like.setBlog(blog);
        likeRepository.save(like);
        return true;
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to like or unlike blog", e);
    }
  }

  @Transactional
  public void softDeleteBlog(Long idBlog) {
    try {
      blogRepository.softDeleteBlog(idBlog, new Date());
    } catch (Exception e) {
      throw new RuntimeException("Failed to soft delete blog", e);
    }
  }
}
