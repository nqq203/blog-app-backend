package com.neko.blog_app.model;

import java.time.LocalDate;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Setter
@Getter
@Table(name = "users")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
  @Id
  @Column(name = "id_user")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idUser;

  @Column(unique = true)
  private String username;

  @Column(unique = true)
  private String email;

  @JsonIgnore
  @Column
  private String password;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "avatar_url")
  private String avatarUrl;

  @ManyToMany
  @JoinTable(name = "user_followers", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "id_user"))
  private Set<User> followers = new HashSet<>();

  @Column(name = "created_at")
  private LocalDate createdAt;

  @Column(name = "locked_at")
  private LocalDate lockedAt;

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<Blog> blogs;

  @OneToMany(mappedBy = "user")
  private List<Comment> comments;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "role")
  private List<String> roles = new ArrayList<>();

  public void addRole(String role) {
    this.roles.add(role);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Optional.ofNullable(roles)
        .orElseGet(Collections::emptyList)
        .stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return true;
  }
}
