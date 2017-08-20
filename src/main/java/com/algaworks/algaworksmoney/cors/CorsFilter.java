package com.algaworks.algaworksmoney.cors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro necessario para habilitaçao do CORS, pois ainda nao ha suporte para Oauth2
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    private String allowedOrigin = "http://localhost:8081"; // TODO: Configurar para diferentes ambientes

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Access-Control-Allow-Origin", allowedOrigin);
        res.setHeader("Access-Control-Allow-Credentials", "true");

        if("OPTIONS".equals(req.getMethod()) && allowedOrigin.equals(req.getHeader("Origin"))) {
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
