package com.algaworks.algaworksmoney.repository;

import com.algaworks.algaworksmoney.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {


}
