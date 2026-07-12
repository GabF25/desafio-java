package com.gabriel.desafio_java.domain;

public record Livro(
        Long id,
        String titulo,
        String autor,
        Integer anoPublicacao
) {
}
