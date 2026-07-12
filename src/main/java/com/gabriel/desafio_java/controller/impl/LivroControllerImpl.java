package com.gabriel.desafio_java.controller.impl;

import com.gabriel.desafio_java.controller.LivroController;
import com.gabriel.desafio_java.domain.Livro;
import com.gabriel.desafio_java.request.LivroRequest;
import com.gabriel.desafio_java.service.LivroService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class LivroControllerImpl implements LivroController {

    private final LivroService livroService;

    public ResponseEntity<List<Livro>> listar() {
        return ResponseEntity.ok(livroService.listar());
    }

    public ResponseEntity<Livro> criar(LivroRequest request) {
        Livro salvo = livroService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    public ResponseEntity<Livro> atualizar(Long id, LivroRequest request) {
        Livro atualizado = livroService.atualizar(id, request);
        return ResponseEntity.ok(atualizado);
    }

    public ResponseEntity<Void> remover(Long id) {
        livroService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
