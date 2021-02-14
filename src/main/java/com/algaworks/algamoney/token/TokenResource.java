package com.algaworks.algamoney.token;

import com.algaworks.algamoney.configuration.property.AlgamoneyProperty;
import com.algaworks.algamoney.configuration.property.ProfileType;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Profile(ProfileType.OAUTH_SECURITY)
@RestController
@RequestMapping("/tokens")
public class TokenResource {

  private final AlgamoneyProperty property;

  public TokenResource(AlgamoneyProperty property) {
    this.property = property;
  }

  @DeleteMapping("/revoke")
  public void revoke(HttpServletRequest request, HttpServletResponse response) {
    Cookie cookie = new Cookie("refreshToken", null);

    cookie.setHttpOnly(true);
    cookie.setSecure(property.getSecurity().isEnableHttps());
    cookie.setPath(request.getContextPath() + "/oauth/token");
    cookie.setMaxAge(0);

    response.addCookie(cookie);
    response.setStatus(HttpStatus.NO_CONTENT.value());
  }
}
