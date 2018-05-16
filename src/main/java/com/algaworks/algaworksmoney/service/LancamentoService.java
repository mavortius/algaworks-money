package com.algaworks.algaworksmoney.service;

import com.algaworks.algaworksmoney.exception.PessoaInexistenteOuInativaException;
import com.algaworks.algaworksmoney.mail.Mailer;
import com.algaworks.algaworksmoney.model.Lancamento;
import com.algaworks.algaworksmoney.model.Pessoa;
import com.algaworks.algaworksmoney.model.Usuario;
import com.algaworks.algaworksmoney.model.projection.*;
import com.algaworks.algaworksmoney.repository.LancamentoRepository;
import com.algaworks.algaworksmoney.repository.LancamentoRepositoryQuery;
import com.algaworks.algaworksmoney.repository.PessoaRepository;
import com.algaworks.algaworksmoney.repository.UsuarioRepository;
import com.querydsl.core.types.Predicate;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.algaworks.algaworksmoney.model.QLancamento.lancamento;

@Service
public class LancamentoService {
    private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);

    private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";
    private static final String AVISO_LANCAMENTOS_VENCIDOS_TEMPLATE = "mail/aviso-lancamentos-vencidos";

    private final LancamentoRepository repository;
    private final LancamentoRepositoryQuery repositoryQuery;
    private final PessoaRepository pessoaRepository;
    private final UsuarioRepository usuarioRepository;
    private final Mailer mailer;

    public LancamentoService(LancamentoRepository repository, LancamentoRepositoryQuery repositoryQuery,
                             PessoaRepository pessoaRepository, UsuarioRepository usuarioRepository, Mailer mailer) {
        this.repository = repository;
        this.repositoryQuery = repositoryQuery;
        this.pessoaRepository = pessoaRepository;
        this.usuarioRepository = usuarioRepository;
        this.mailer = mailer;
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void avisarSobreLancamentosVencidos() {
        if(logger.isDebugEnabled()) {
            logger.debug("Preparando envio de emails de aviso de vencimentos.");
        }

        List<Lancamento> vencidos = repository
                .findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

        if(vencidos.isEmpty()) {
            logger.info("Sem lançamentos vencidos para aviso.");
            return;
        }

        logger.info("Existem {} lançamentos vencidos.", vencidos.size());

        List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);

        if(destinatarios.isEmpty()) {
            logger.warn("Existem lançamentos vencidos, mas o sistema não encontrou destinários.");
            return;
        }

        Map<String, Object> valores = new HashMap<>();

        valores.put("lancamentos", vencidos);

        List<String> emails = destinatarios.stream()
                .map(Usuario::getEmail)
                .collect(Collectors.toList());

        mailer.send("avisos@algamoney.com", emails,
                "Lançamentos vencidos",
                AVISO_LANCAMENTOS_VENCIDOS_TEMPLATE,
                valores);

        logger.info("Envio de email de aviso concluído.");
    }

    public Lancamento salvar(Lancamento lancamento) {
        Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());

        if (pessoa == null || pessoa.isInativa()) {
            throw new PessoaInexistenteOuInativaException();
        } else {
            return repository.save(lancamento);
        }
    }

    public Page<ResumoLancamento> obtemResumo(Predicate predicate, Pageable pageable) {
        return (Page<ResumoLancamento>) repositoryQuery.findAll(predicate, pageable,
                new QResumoLancamento(lancamento.codigo, lancamento.descricao, lancamento.dataVencimento,
                        lancamento.dataPagamento, lancamento.valor, lancamento.tipo, lancamento.categoria.nome, lancamento.pessoa.nome));
    }

    public List<?> obtemPorCategoria(LocalDate mesReferencia) {
        LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
        LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
        Predicate periodo = lancamento.dataVencimento.between(primeiroDia, ultimoDia);

        return repositoryQuery.findAll(periodo,
                new QLancamentoEstatisticaCategoria(lancamento.categoria, lancamento.valor.sum()),
                lancamento.categoria);
    }

    public List<?> obtemPorTipo(LocalDate mesReferencia) {
        LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
        LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
        Predicate periodo = lancamento.dataVencimento.between(primeiroDia, ultimoDia);

        return repositoryQuery.findAll(periodo,
                new QLancamentoEstatisticaDia(lancamento.tipo, lancamento.dataVencimento, lancamento.valor.sum()),
                lancamento.tipo, lancamento.dataVencimento);
    }

    public List<?> obtemPorPessoa(LocalDate inicio, LocalDate fim) {
        Predicate periodo = lancamento.dataVencimento.between(inicio, fim);

        return repositoryQuery.findAll(periodo,
                new QLancamentoEstatisticaPessoa(lancamento.tipo, lancamento.pessoa, lancamento.valor.sum()),
                lancamento.tipo, lancamento.pessoa);
    }

    public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
        List<LancamentoEstatisticaPessoa> dados = (List<LancamentoEstatisticaPessoa>) obtemPorPessoa(inicio, fim);
        Map<String, Object> parametros = new HashMap<>();

        parametros.put("DT_INICIO", Date.valueOf(inicio));
        parametros.put("DT_FIM", Date.valueOf(fim));
        parametros.put("REPORT_LOCALE", LocaleContextHolder.getLocale());

        InputStream inputStream = this.getClass().getResourceAsStream(
                "/relatorios/lancamentos-por-pessoa.jasper");
        JasperPrint print = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(dados));

        return JasperExportManager.exportReportToPdf(print);
    }

    public Lancamento atualizar(Long codigo, Lancamento lancamento) {
        Lancamento lancamentoSalvo = buscarLancamento(codigo);

        if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validarPessoa(lancamento);
        }

        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

        return repository.save(lancamentoSalvo);
    }

    private void validarPessoa(Lancamento lancamento) {
        Pessoa pessoa = null;

        if (lancamento.getPessoa().getCodigo() != null) {
            pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
        }

        if (pessoa == null || pessoa.isInativa()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    private Lancamento buscarLancamento(Long codigo) {
        Lancamento lancamento = repository.findOne(codigo);

        if (lancamento == null) {
            throw new IllegalArgumentException();
        }

        return lancamento;
    }
}
