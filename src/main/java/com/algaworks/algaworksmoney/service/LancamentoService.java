package com.algaworks.algaworksmoney.service;

import com.algaworks.algaworksmoney.exception.PessoaInexistenteOuInativaException;
import com.algaworks.algaworksmoney.model.Lancamento;
import com.algaworks.algaworksmoney.model.Pessoa;
import com.algaworks.algaworksmoney.model.projection.QLancamentoEstatisticaCategoria;
import com.algaworks.algaworksmoney.model.projection.QResumoLancamento;
import com.algaworks.algaworksmoney.model.projection.ResumoLancamento;
import com.algaworks.algaworksmoney.repository.LancamentoRepository;
import com.algaworks.algaworksmoney.repository.LancamentoRepositoryQuery;
import com.algaworks.algaworksmoney.repository.PessoaRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.algaworks.algaworksmoney.model.QLancamento.lancamento;

@Service
public class LancamentoService {

    private final LancamentoRepository repository;
    private final LancamentoRepositoryQuery repositoryQuery;
    private final PessoaRepository pessoaRepository;

    public LancamentoService(LancamentoRepository repository, LancamentoRepositoryQuery repositoryQuery, PessoaRepository pessoaRepository) {
        this.repository = repository;
        this.repositoryQuery = repositoryQuery;
        this.pessoaRepository = pessoaRepository;
    }

    public Lancamento salvar(Lancamento lancamento) {
        Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());

        if(pessoa == null || pessoa.isInativa()) {
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

        return repositoryQuery.findAll(periodo, lancamento.categoria,
                new QLancamentoEstatisticaCategoria(lancamento.categoria, lancamento.valor.sum()));
    }


    public Lancamento atualizar(Long codigo, Lancamento lancamento) {
        Lancamento lancamentoSalvo = buscarLancamento(codigo);

        if(!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validarPessoa(lancamento);
        }

        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

        return repository.save(lancamentoSalvo);
    }

    private void validarPessoa(Lancamento lancamento) {
        Pessoa pessoa = null;

        if(lancamento.getPessoa().getCodigo() != null) {
            pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
        }

        if(pessoa == null || pessoa.isInativa()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    private Lancamento buscarLancamento(Long codigo) {
        Lancamento lancamento = repository.findOne(codigo);

        if(lancamento == null) {
            throw new IllegalArgumentException();
        }

        return lancamento;
    }
}
