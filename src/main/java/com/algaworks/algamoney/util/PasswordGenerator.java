package com.algaworks.algamoney.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

  public static void main(String[] args) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    //System.out.println(encoder.encode("@ngul@r"));
    boolean matches = encoder.matches("@ngul@r", "$2a$10$ahJMvM3CvCf7fijTxvDXS.7TlB1ah9AGAKCg3PRr6R.aPakhkZBtC");
    System.out.println(matches);
  }
}
