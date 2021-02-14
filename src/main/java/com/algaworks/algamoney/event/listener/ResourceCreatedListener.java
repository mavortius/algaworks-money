package com.algaworks.algamoney.event.listener;

import com.algaworks.algamoney.event.ResourceCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Component
public class ResourceCreatedListener implements ApplicationListener<ResourceCreatedEvent> {
  @Override
  public void onApplicationEvent(ResourceCreatedEvent event) {
    HttpServletResponse response = event.getResponse();
    Long id = event.getId();
    addLocationHeader(response, id);
  }

  private void addLocationHeader(HttpServletResponse response, Long id) {
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
            .buildAndExpand(id).toUri();
    response.setHeader("Location", uri.toASCIIString());
  }
}
