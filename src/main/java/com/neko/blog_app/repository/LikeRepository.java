package com.neko.blog_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neko.blog_app.model.Blog;
import com.neko.blog_app.model.Like;
import com.neko.blog_app.model.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndBlog(User user, Blog blog);
    void deleteByUserAndBlog(User user, Blog blog);
}
