package com.algaworks.algaworksmoney.resource;

import com.algaworks.algaworksmoney.model.Estado;
import com.algaworks.algaworksmoney.repository.EstadoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estados")
public class EstadoResource {

    private final EstadoRepository repository;

    public EstadoResource(EstadoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Estado> listar() {
        return repository.findAll();
    }
}
