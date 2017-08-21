package com.algaworks.algaworksmoney.service;

import com.algaworks.algaworksmoney.exception.PessoaInexistenteException;
import com.algaworks.algaworksmoney.model.Lancamento;
import com.algaworks.algaworksmoney.model.Pessoa;
import com.algaworks.algaworksmoney.model.projection.QResumoLancamento;
import com.algaworks.algaworksmoney.model.projection.ResumoLancamento;
import com.algaworks.algaworksmoney.repository.LancamentoRepository;
import com.algaworks.algaworksmoney.repository.LancamentoRepositoryQuery;
import com.algaworks.algaworksmoney.repository.PessoaRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
            throw new PessoaInexistenteException();
        } else {
            return repository.save(lancamento);
        }po
    }

    public Page<ResumoLancamento> obtemResumo(Predicate predicate, Pageable pageable) {
        return (Page<ResumoLancamento>) repositoryQuery.findAll(predicate, pageable,
                new QResumoLancamento(lancamento.codigo, lancamento.descricao, lancamento.dataVencimento,
                        lancamento.dataPagamento, lancamento.valor, lancamento.tipo, lancamento.categoria.nome, lancamento.pessoa.nome));
    }
}
