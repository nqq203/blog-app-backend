// package com.neko.blog_app.model;

// import java.io.Serializable;
// import java.util.Objects;

// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// public class CommentId implements Serializable {
//   private Long blog;
//   private Long user;

//   @Override
//   public boolean equals(Object o) {
//     if (this == o)
//       return true;
//     if (o == null || getClass() != o.getClass())
//       return false;
//     CommentId commentId = (CommentId) o;
//     return Objects.equals(blog, commentId.blog) &&
//         Objects.equals(user, commentId.user);
//   }

//   @Override
//   public int hashCode() {
//     return Objects.hash(blog, user);
//   }
// }
