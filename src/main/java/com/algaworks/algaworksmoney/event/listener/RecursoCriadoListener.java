package com.algaworks.algaworksmoney.event.listener;

import com.algaworks.algaworksmoney.event.RecursoCriadoEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> {
    @Override
    public void onApplicationEvent(RecursoCriadoEvent event) {
        HttpServletResponse response = event.getResponse();
        Long codigo = event.getCodigo();

        addLocationHeader(response, codigo);
    }

    private void addLocationHeader(HttpServletResponse response, Long codigo) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(codigo).toUri();

        response.setHeader("Location", uri.toASCIIString());
    }
}
