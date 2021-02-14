package com.algaworks.algamoney.security;

import com.algaworks.algamoney.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SystemUser extends org.springframework.security.core.userdetails.User {
  private final User user;

  public SystemUser(User user, Collection<? extends GrantedAuthority> authorities) {
    super(user.getEmail(), user.getPassword(), authorities);
    this.user = user;
  }

  public User getUser() {
    return user;
  }
}
