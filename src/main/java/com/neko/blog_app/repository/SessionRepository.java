package com.neko.blog_app.repository;

import com.neko.blog_app.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
  Session findByToken(String token);
  // void deleteByToken(String token);
}
