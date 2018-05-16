package com.algaworks.algaworksmoney.service;

import com.algaworks.algaworksmoney.model.Pessoa;
import com.algaworks.algaworksmoney.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    private final PessoaRepository repository;

    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    public Pessoa obterPessoaPorCodigo(Long codigo) {
        Pessoa pessoa = repository.findOne(codigo);

        if(pessoa == null) {
            throw new EmptyResultDataAccessException(1);
        } else {
            return pessoa;
        }
    }

    public Pessoa salvar(Pessoa pessoa) {
        pessoa.getContatos().forEach(c -> c.setPessoa(pessoa));

        return repository.save(pessoa);
    }

    public void atualizar(Long codigo, Boolean ativa) {
        Pessoa pessoa = obterPessoaPorCodigo(codigo);

        pessoa.setAtivo(ativa);
        repository.save(pessoa);
    }

    public Pessoa atualizar(Long codigo, Pessoa pessoa) {
        Pessoa pessoaSalva = obterPessoaPorCodigo(codigo);

        pessoaSalva.getContatos().clear();
        pessoaSalva.getContatos().addAll(pessoa.getContatos());
        pessoa.getContatos().forEach(c -> c.setPessoa(pessoaSalva));
        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo", "contatos");

        return repository.save(pessoaSalva);
    }
}
