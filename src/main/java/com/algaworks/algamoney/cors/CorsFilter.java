package com.algaworks.algamoney.cors;

import com.algaworks.algamoney.configuration.property.AlgamoneyProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter needed for enabling CORS because there isn't still support for Oauth2
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

  private final AlgamoneyProperty property;

  public CorsFilter(AlgamoneyProperty property) {
    this.property = property;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String allowedOriginUrl = property.getSecurity().getAllowedOriginUrl();

    res.setHeader("Access-Control-Allow-Origin", allowedOriginUrl);
    res.setHeader("Access-Control-Allow-Credentials", "true");

    if ("OPTIONS".equals(req.getMethod()) && allowedOriginUrl.equals(req.getHeader("Origin"))) {
      res.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
      res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
      res.setHeader("Access-Control-Maz-Age", "3600");

    } else {
      chain.doFilter(request, response);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void destroy() {
  }
}
