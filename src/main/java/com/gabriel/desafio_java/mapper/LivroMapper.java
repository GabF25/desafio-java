package com.gabriel.desafio_java.mapper;

import com.gabriel.desafio_java.domain.Livro;
import com.gabriel.desafio_java.infrastructure.LivroEntity;
import com.gabriel.desafio_java.request.LivroRequest;
import org.springframework.stereotype.Component;

@Component
public class LivroMapper {

    public LivroEntity toEntity(LivroRequest request) {
        return new LivroEntity(
                request.titulo(),
                request.autor(),
                request.anoPublicacao()
        );
    }

    public Livro toDomain(LivroEntity livroEntity) {
        return new Livro(
                livroEntity.getId(),
                livroEntity.getTitulo(),
                livroEntity.getAutor(),
                livroEntity.getAnoPublicacao()
        );
    }
}