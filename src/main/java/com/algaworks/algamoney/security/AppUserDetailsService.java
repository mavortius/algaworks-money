package com.algaworks.algamoney.security;

import com.algaworks.algamoney.repository.UserRepository;
import com.algaworks.algamoney.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.algaworks.algamoney.repository.query.UserQueryExpressions.emailEquals;

@Service
public class AppUserDetailsService implements UserDetailsService {

  private final UserRepository repository;

  public AppUserDetailsService(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = repository.findOne(emailEquals(email))
            .orElseThrow(() -> new UsernameNotFoundException("Wrong username or password"));
    return new SystemUser(user, getPermissions(user));
  }

  private Collection<? extends GrantedAuthority> getPermissions(User user) {
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    user.getPermissions().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getDescription().toUpperCase())));
    return authorities;
  }
}
