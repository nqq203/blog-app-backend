package com.neko.blog_app.utils;

import java.util.regex.Pattern;

public class Identifier {
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

  public static boolean isValidEmail(String input) {
    if (EMAIL_PATTERN.matcher(input).matches()) {
      return true;
    }
    return false;
  }
}
