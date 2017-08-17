package com.algaworks.algaworksmoney.service;

import com.algaworks.algaworksmoney.exception.PessoaInexistenteException;
import com.algaworks.algaworksmoney.model.Lancamento;
import com.algaworks.algaworksmoney.model.Pessoa;
import com.algaworks.algaworksmoney.repository.LancamentoRepository;
import com.algaworks.algaworksmoney.repository.PessoaRepository;
import org.springframework.stereotype.Service;

@Service
public class LancamentoService {

    private final LancamentoRepository repository;
    private final PessoaRepository pessoaRepository;

    public LancamentoService(LancamentoRepository repository, PessoaRepository pessoaRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
    }

    public Lancamento salvar(Lancamento lancamento) {
        Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());

        if(pessoa == null || pessoa.isInativa()) {
            throw new PessoaInexistenteException();
        } else {
            return repository.save(lancamento);
        }
    }
}
