package com.algaworks.algaworksmoney.resource;

import com.algaworks.algaworksmoney.event.RecursoCriadoEvent;
import com.algaworks.algaworksmoney.model.Categoria;
import com.algaworks.algaworksmoney.repository.CategoriaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

    private final CategoriaRepository repository;

    private final ApplicationEventPublisher eventPublisher;

    public CategoriaResource(CategoriaRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public List<Categoria> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
        Categoria novaCategoria = repository.save(categoria);

        eventPublisher.publishEvent(new RecursoCriadoEvent(this, response, novaCategoria.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Categoria> obter(@PathVariable Long codigo) {
        Categoria categoria = repository.findOne(codigo);

        return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
    }
}

