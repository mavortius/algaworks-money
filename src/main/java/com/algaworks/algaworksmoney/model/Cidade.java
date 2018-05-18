package com.algaworks.algaworksmoney.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Cidade {

    @Id
    private Long codigo;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "codigo_estado")
    private Estado estado;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cidade cidade = (Cidade) o;
        return Objects.equals(nome, cidade.nome) &&
                Objects.equals(estado, cidade.estado);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nome, estado);
    }
}
