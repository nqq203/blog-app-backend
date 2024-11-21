package com.neko.blog_app.common;

public class NotFoundException extends RuntimeException {
  public NotFoundException() {}

  public NotFoundException(String message) {
      super(message);
  }
}