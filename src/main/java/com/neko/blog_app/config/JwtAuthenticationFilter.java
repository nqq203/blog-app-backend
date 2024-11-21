package com.neko.blog_app.config;

import java.io.IOException;
// import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
// import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.neko.blog_app.model.User;
import com.neko.blog_app.service.AuthService;
import com.neko.blog_app.service.UserService;
import com.neko.blog_app.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
  // private final AntPathMatcher  pathMatcher= new AntPathMatcher();
  @Autowired
  private AuthService authService;
  @Autowired
  private UserService userService;
  @Autowired
  private JwtService jwtService;

  // private static final List<String> WHITE_LIST = List.of();

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
    try {
      // String requestPath = req.getRequestURI();
      // boolean isWhiteListed = WHITE_LIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
      // if (isWhiteListed) {
      //   filterChain.doFilter(req, res);
      //   return;
      // }
      
      String token = req.getHeader("Authorization");
      if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
        boolean isBlackListed = authService.isTokenBlacklisted(token);
        String username = jwtService.validateTokenAndGetUsername(token);
        if (username == null || isBlackListed) {
          res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          res.setContentType("application/json");
          String jsonResponse = "{\"message\":\"Invalid or expired token!\", \"code\":401}";
          res.getWriter().write(jsonResponse);
          res.getWriter().flush();
          return;
        }
        User user = userService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      filterChain.doFilter(req, res);
    } catch (IOException e) {
      throw new RuntimeException("Authentication error (IOException): ", e.getCause());
    } catch (ServletException e) {
      throw new RuntimeException("Authentication error (ServletException): ", e);
    }
  }
}
