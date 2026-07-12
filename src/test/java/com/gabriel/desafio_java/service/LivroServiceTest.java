package com.gabriel.desafio_java.service;

import com.gabriel.desafio_java.domain.Livro;
import com.gabriel.desafio_java.infrastructure.LivroEntity;
import com.gabriel.desafio_java.mapper.LivroMapper;
import com.gabriel.desafio_java.repository.LivroRepository;
import com.gabriel.desafio_java.request.LivroRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    private LivroService livroService;

    @BeforeEach
    void setUp() {
        livroService = new LivroService(livroRepository, new LivroMapper());
    }

    private LivroEntity entity(Long id, String titulo, String autor, Integer ano) {
        LivroEntity entity = new LivroEntity(titulo, autor, ano);
        entity.setId(id);
        return entity;
    }

    // ---------- listar ----------

    @Test
    @DisplayName("listar deve converter as entidades do repositório para o domínio")
    void listar_deveRetornarLivrosDoRepositorio() {
        when(livroRepository.findAll()).thenReturn(List.of(
                entity(1L, "Clean Code", "Robert C. Martin", 2008),
                entity(2L, "Refatoração", "Martin Fowler", 1999)
        ));

        List<Livro> livros = livroService.listar();

        assertEquals(2, livros.size());
        assertEquals(new Livro(1L, "Clean Code", "Robert C. Martin", 2008), livros.get(0));
        assertEquals(new Livro(2L, "Refatoração", "Martin Fowler", 1999), livros.get(1));
    }

    @Test
    @DisplayName("listar deve retornar lista vazia quando não há livros")
    void listar_deveRetornarListaVazia() {
        when(livroRepository.findAll()).thenReturn(List.of());

        assertTrue(livroService.listar().isEmpty());
    }

    // ---------- salvar ----------

    @Test
    @DisplayName("salvar deve persistir o livro e retornar o domínio com id quando o título é inédito")
    void salvar_devePersistirQuandoTituloInedito() {
        LivroRequest request = new LivroRequest("Clean Code", "Robert C. Martin", 2008);
        when(livroRepository.existsByTituloIgnoreCase("Clean Code")).thenReturn(false);
        when(livroRepository.save(any(LivroEntity.class))).thenAnswer(invocation -> {
            LivroEntity salvo = invocation.getArgument(0);
            salvo.setId(1L);
            return salvo;
        });

        Livro livro = livroService.salvar(request);

        assertEquals(new Livro(1L, "Clean Code", "Robert C. Martin", 2008), livro);

        ArgumentCaptor<LivroEntity> captor = ArgumentCaptor.forClass(LivroEntity.class);
        verify(livroRepository).save(captor.capture());
        assertEquals("Clean Code", captor.getValue().getTitulo());
        assertEquals("Robert C. Martin", captor.getValue().getAutor());
        assertEquals(2008, captor.getValue().getAnoPublicacao());
    }

    @Test
    @DisplayName("salvar deve rejeitar título duplicado e não persistir nada")
    void salvar_deveRejeitarTituloDuplicado() {
        LivroRequest request = new LivroRequest("Clean Code", "Outro Autor", 2010);
        when(livroRepository.existsByTituloIgnoreCase("Clean Code")).thenReturn(true);

        IllegalStateException excecao = assertThrows(IllegalStateException.class,
                () -> livroService.salvar(request));

        assertEquals("Já existe um livro com o título: Clean Code", excecao.getMessage());
        verify(livroRepository, never()).save(any());
    }

    // ---------- atualizar ----------

    @Test
    @DisplayName("atualizar deve alterar os campos do livro existente e persistir")
    void atualizar_deveAlterarCamposEPersistir() {
        LivroRequest request = new LivroRequest("Clean Code (2ª ed.)", "Robert C. Martin", 2010);
        when(livroRepository.findById(1L))
                .thenReturn(Optional.of(entity(1L, "Clean Code", "Robert C. Martin", 2008)));
        when(livroRepository.save(any(LivroEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Livro livro = livroService.atualizar(1L, request);

        assertEquals(new Livro(1L, "Clean Code (2ª ed.)", "Robert C. Martin", 2010), livro);

        ArgumentCaptor<LivroEntity> captor = ArgumentCaptor.forClass(LivroEntity.class);
        verify(livroRepository).save(captor.capture());
        assertEquals("Clean Code (2ª ed.)", captor.getValue().getTitulo());
        assertEquals(2010, captor.getValue().getAnoPublicacao());
    }

    @Test
    @DisplayName("atualizar deve lançar NoSuchElementException quando o livro não existe")
    void atualizar_deveFalharQuandoLivroNaoExiste() {
        LivroRequest request = new LivroRequest("Clean Code", "Robert C. Martin", 2008);
        when(livroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> livroService.atualizar(99L, request));
        verify(livroRepository, never()).save(any());
    }

    // ---------- remover ----------

    @Test
    @DisplayName("remover deve excluir o livro quando ele existe")
    void remover_deveExcluirQuandoExiste() {
        when(livroRepository.existsById(1L)).thenReturn(true);

        livroService.remover(1L);

        verify(livroRepository).deleteById(1L);
    }

    @Test
    @DisplayName("remover deve lançar NoSuchElementException quando o livro não existe")
    void remover_deveFalharQuandoLivroNaoExiste() {
        when(livroRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> livroService.remover(99L));
        verify(livroRepository, never()).deleteById(any());
    }
}
