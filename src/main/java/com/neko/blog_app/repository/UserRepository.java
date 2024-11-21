package com.neko.blog_app.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.neko.blog_app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @Query("SELECT u FROM User u WHERE u.idUser = ?1 AND u.lockedAt IS NULL")
  public User findByIdUser(Long idUser);

  @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.lockedAt IS NULL")
  public User findByEmail(String email);

  @Query("SELECT u FROM User u WHERE u.username = ?1 AND u.lockedAt IS NULL")
  public User findByUsername(String username);

  @Query(value = "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
      "FROM User u WHERE (u.email = ?1 OR u.username = ?2) AND u.lockedAt IS NULL", nativeQuery = true)
  public boolean isExistingAccount(String email, String username);
}
