package com.algaworks.algaworksmoney.configuration.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("algamoney")
public class AlgamoneyProperty {

    private final Security security = new Security();

    public Security getSecurity() {
        return security;
    }

    public static class Security {
        private boolean enableHttps;
        private String allowedOriginUrl = "http://localhost:8080";

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
}
