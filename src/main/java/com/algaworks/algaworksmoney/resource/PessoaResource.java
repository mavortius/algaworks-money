package com.algaworks.algaworksmoney.resource;

import com.algaworks.algaworksmoney.event.RecursoCriadoEvent;
import com.algaworks.algaworksmoney.model.Pessoa;
import com.algaworks.algaworksmoney.repository.PessoaRepository;
import com.algaworks.algaworksmoney.service.PessoaService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    private final PessoaRepository repository;

    private final PessoaService service;

    private final ApplicationEventPublisher eventPublisher;

    public PessoaResource(PessoaRepository repository, PessoaService service, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.service = service;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public List<Pessoa> listar() {
        return repository.findAll();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Pessoa> obter(@PathVariable Long codigo) {
        Pessoa pessoa = repository.findOne(codigo);
        return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
        Pessoa pessoaSalva = repository.save(pessoa);

        eventPublisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long codigo) {
        repository.delete(codigo);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
        return ResponseEntity.ok(service.atualizar(codigo, pessoa));
    }

    @PutMapping("/{codigo}/ativa")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Long codigo, @RequestBody Boolean ativa) {
        service.atualizar(codigo, ativa);
    }
}