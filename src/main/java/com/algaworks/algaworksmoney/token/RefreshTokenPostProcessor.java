package com.algaworks.algaworksmoney.token;

import com.algaworks.algaworksmoney.configuration.property.AlgamoneyProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

    private final AlgamoneyProperty property;

    public RefreshTokenPostProcessor(AlgamoneyProperty property) {
        this.property = property;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getMethod().getName().equals("postAccessToken");
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
                                             MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                             ServerHttpRequest request, ServerHttpResponse response) {
        addRefreshTokenToCookie(body, request, response);
        removeRefreshTokenFromBody(body);

        return body;
    }

    private void addRefreshTokenToCookie(OAuth2AccessToken body, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse res = ((ServletServerHttpResponse) response).getServletResponse();
        String refreshToken = body.getRefreshToken().getValue();
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(property.getSecurity().isEnableHttps());
        refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
        refreshTokenCookie.setMaxAge(2592000);
        refreshTokenCookie.setDomain("localhost");
        res.addCookie(refreshTokenCookie);
    }

    private void removeRefreshTokenFromBody(OAuth2AccessToken body) {
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;

        token.setRefreshToken(null);
    }
}
