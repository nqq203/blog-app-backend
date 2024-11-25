package com.neko.blog_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.Date;

import org.springframework.data.domain.Page;

import com.neko.blog_app.model.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
  @Query("SELECT b FROM Blog b WHERE b.user.id = :idUser AND b.deletedDate IS NULL")
  Page<Blog> findByUserIdUser(Long idUser, Pageable pageable);

  @Query("SELECT b FROM Blog b JOIN b.user.followers f WHERE f.idUser = :idUser AND b.deletedDate IS NULL")
  Page<Blog> findFeedsByIdUser(Long idUser, Pageable pageable);

  @Modifying
  @Query("UPDATE Blog b SET b.deletedDate = :deletedDate WHERE b.idBlog = :idBlog")
  void softDeleteBlog(@Param("idBlog") Long idBlog, @Param("deletedDate") Date deletedDate);

  // @Query
  // Blog findBlogByIdBlog(Long idBlog);
}
