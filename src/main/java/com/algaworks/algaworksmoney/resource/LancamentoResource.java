package com.algaworks.algaworksmoney.resource;

import com.algaworks.algaworksmoney.event.RecursoCriadoEvent;
import com.algaworks.algaworksmoney.exception.ApiExceptionHandler;
import com.algaworks.algaworksmoney.exception.PessoaInexistenteOuInativaException;
import com.algaworks.algaworksmoney.model.Lancamento;
import com.algaworks.algaworksmoney.model.projection.LancamentoEstatisticaCategoria;
import com.algaworks.algaworksmoney.model.projection.LancamentoEstatisticaDia;
import com.algaworks.algaworksmoney.model.projection.ResumoLancamento;
import com.algaworks.algaworksmoney.repository.LancamentoRepository;
import com.algaworks.algaworksmoney.service.LancamentoService;
import com.querydsl.core.types.Predicate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
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

    @PostMapping("/anexo")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
    public String uploadAnexo(@RequestParam MultipartFile anexo) throws IOException {
        OutputStream out = new FileOutputStream(
                "/home/u0494385/Desktop/anexo--" + anexo.getOriginalFilename());
        out.write(anexo.getBytes());
        out.close();

        return "File uploaded";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Page<Lancamento> pesquisar(@QuerydslPredicate(root = Lancamento.class) Predicate predicate,
                                      Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @PostMapping("/qbe")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Page<Lancamento> pesquisar(@RequestBody Lancamento exemplo, Pageable pageable) {
        return repository.findAll(lancamento.like(exemplo), pageable);
    }

    @GetMapping(params = "resumo")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Page<ResumoLancamento> resumir(@QuerydslPredicate(root = Lancamento.class) Predicate predicate,
                                          Pageable pageable) {
        return service.obtemResumo(predicate, pageable);
    }

    @GetMapping("/estatisticas/por-categoria")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public List<LancamentoEstatisticaCategoria> totalLancamentosPorCategoria() {
        return (List<LancamentoEstatisticaCategoria>) service.obtemPorCategoria(LocalDate.now());
    }

    @GetMapping("/estatisticas/por-tipo")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public List<LancamentoEstatisticaDia> totalLancamentosPorTipo() {
        return (List<LancamentoEstatisticaDia>) service.obtemPorTipo(LocalDate.now());
    }

    @GetMapping("/relatorios/por-pessoa")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public ResponseEntity<byte[]> relatorioPorPessoa(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) throws Exception {
        byte[] relatorio = service.relatorioPorPessoa(inicio, fim);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(relatorio);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public ResponseEntity<Lancamento> obter(@PathVariable Long codigo) {
        return repository.findById(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Lancamento novoLancamento = service.salvar(lancamento);

        eventPublisher.publishEvent(new RecursoCriadoEvent(this, response, novoLancamento.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(novoLancamento);
    }

    @ExceptionHandler({PessoaInexistenteOuInativaException.class})
    public ResponseEntity<Object> handlePessoaInexistenteException(PessoaInexistenteOuInativaException ex) {
        String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();
        List<ApiExceptionHandler.MensagemErro> erros = Arrays.asList(new ApiExceptionHandler.MensagemErro(mensagemUsuario, mensagemDesenvolvedor));

        return ResponseEntity.badRequest().body(erros);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long codigo) {
        repository.deleteById(codigo);
    }

    @PutMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
    public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento) {
        try {
            Lancamento lancamentoSalvo = service.atualizar(codigo, lancamento);
            return ResponseEntity.ok(lancamentoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
