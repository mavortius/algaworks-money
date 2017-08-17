package com.algaworks.algaworksmoney.resource;

import com.algaworks.algaworksmoney.event.RecursoCriadoEvent;
import com.algaworks.algaworksmoney.exception.ApiExceptionHandler;
import com.algaworks.algaworksmoney.exception.PessoaInexistenteException;
import com.algaworks.algaworksmoney.model.Lancamento;
import com.algaworks.algaworksmoney.repository.LancamentoRepository;
import com.algaworks.algaworksmoney.service.LancamentoService;
import com.querydsl.core.types.Predicate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static com.algaworks.algaworksmoney.model.QLancamento.lancamento;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

    private final LancamentoRepository repository;
    private final LancamentoService service;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSource messageSource;

    public LancamentoResource(LancamentoRepository repository, LancamentoService service,
                              ApplicationEventPublisher eventPublisher, MessageSource messageSource) {
        this.repository = repository;
        this.service = service;
        this.eventPublisher = eventPublisher;
        this.messageSource = messageSource;
    }

    @GetMapping
    public Iterable<Lancamento> pesquisar(@QuerydslPredicate(root = Lancamento.class) Predicate predicate) {
        return repository.findAll(predicate);
    }

    @PostMapping("/qbe")
    public Iterable<Lancamento> pesquisar(@RequestBody Lancamento exemplo) {
        return repository.findAll(lancamento.like(exemplo));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> obter(@PathVariable Long codigo) {
        Lancamento lancamento = repository.findOne(codigo);

        return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Lancamento novoLancamento = service.salvar(lancamento);

        eventPublisher.publishEvent(new RecursoCriadoEvent(this, response, novoLancamento.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(novoLancamento);
    }

    @ExceptionHandler({PessoaInexistenteException.class})
    public ResponseEntity<Object> handlePessoaInexistenteException(PessoaInexistenteException ex) {
        String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();
        List<ApiExceptionHandler.MensagemErro> erros = Arrays.asList(new ApiExceptionHandler.MensagemErro(mensagemUsuario, mensagemDesenvolvedor));

        return ResponseEntity.badRequest().body(erros);
    }
}