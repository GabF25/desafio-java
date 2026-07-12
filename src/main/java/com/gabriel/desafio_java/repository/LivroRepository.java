package com.gabriel.desafio_java.repository;

import com.gabriel.desafio_java.infrastructure.LivroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<LivroEntity, Long> {

    boolean existsByTituloIgnoreCase(String titulo);
}
