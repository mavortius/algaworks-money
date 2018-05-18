package com.algaworks.algaworksmoney.resource;

import com.algaworks.algaworksmoney.model.Cidade;
import com.algaworks.algaworksmoney.repository.CidadeRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CidadeResource {

    private final CidadeRepository repository;

    public CidadeResource(CidadeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Cidade> pesquisar(@RequestParam Long estado) {
        return repository.findByEstadoCodigo(estado);
    }
}
