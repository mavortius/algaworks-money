package com.algaworks.algaworksmoney.repository;

import com.algaworks.algaworksmoney.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByPermissoesDescricao(String permissaoDescricao);
}
