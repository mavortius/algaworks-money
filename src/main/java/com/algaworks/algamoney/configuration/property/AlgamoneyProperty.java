package com.algaworks.algamoney.configuration.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("algamoney")
public class AlgamoneyProperty {

  private final Security security = new Security();
  private final Mail mail = new Mail();

  public Security getSecurity() {
    return security;
  }

  public Mail getMail() {
    return mail;
  }

  public static class Security {
    private boolean enableHttps;
    private String allowedOriginUrl = "http://localhost:3000";

    public boolean isEnableHttps() {
      return enableHttps;
    }

    public void setEnableHttps(boolean enableHttps) {
      this.enableHttps = enableHttps;
    }

    public String getAllowedOriginUrl() {
      return allowedOriginUrl;
    }

    public void setAllowedOriginUrl(String allowedOriginUrl) {
      this.allowedOriginUrl = allowedOriginUrl;
    }
  }

  public static class Mail {
    private String host;
    private Integer port;
    private String username;
    private String password;

    public String getHost() {
      return host;
    }

    public void setHost(String host) {
      this.host = host;
    }

    public Integer getPort() {
      return port;
    }

    public void setPort(Integer port) {
      this.port = port;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }
}
