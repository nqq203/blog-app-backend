package com.neko.blog_app.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.neko.blog_app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @Query("SELECT u FROM User u WHERE u.idUser = :idUser AND u.lockedAt IS NULL")
  public User findByIdUser(Long idUser);

  @Query("SELECT u FROM User u WHERE u.email = :email AND u.lockedAt IS NULL")
  public User findByEmail(String email);

  @Query("SELECT u FROM User u WHERE u.username = :username AND u.lockedAt IS NULL")
  public User findByUsername(String username);

  @Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
      "FROM User u WHERE (u.email = :email OR u.username = :username) AND u.lockedAt IS NULL")
  public boolean isExistingAccount(String email, String username);
}
