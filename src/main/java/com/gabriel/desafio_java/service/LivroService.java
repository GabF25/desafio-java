package com.gabriel.desafio_java.service;

import com.gabriel.desafio_java.domain.Livro;
import com.gabriel.desafio_java.infrastructure.LivroEntity;
import com.gabriel.desafio_java.mapper.LivroMapper;
import com.gabriel.desafio_java.repository.LivroRepository;
import com.gabriel.desafio_java.request.LivroRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class LivroService {

    private LivroRepository livroRepository;
    private LivroMapper livroMapper;

    @Transactional(readOnly = true)
    public List<Livro> listar() {
        return livroRepository.findAll()
                .stream()
                .map(livroMapper::toDomain)
                .toList();
    }

    public Livro salvar(LivroRequest request) {
        if (livroRepository.existsByTituloIgnoreCase(request.titulo())) {
            throw new IllegalStateException("Já existe um livro com o título: " + request.titulo());
        }

        LivroEntity entity = livroRepository.save(livroMapper.toEntity(request));
        return livroMapper.toDomain(entity);
    }

    public Livro atualizar(Long id, LivroRequest request) {
        LivroEntity entity = livroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Livro não encontrado"));

        entity.setTitulo(request.titulo());
        entity.setAutor(request.autor());
        entity.setAnoPublicacao(request.anoPublicacao());

        LivroEntity livroAtualizado = livroRepository.save(entity);

        return livroMapper.toDomain(livroAtualizado);
    }

    public void remover(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new NoSuchElementException("Livro não encontrado: " + id);
        }

        livroRepository.deleteById(id);
    }
}
