package com.gabriel.desafio_java.controller;

import com.gabriel.desafio_java.controller.impl.LivroControllerImpl;
import com.gabriel.desafio_java.domain.Livro;
import com.gabriel.desafio_java.request.LivroRequest;
import com.gabriel.desafio_java.service.LivroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LivroControllerImpl.class)
class LivroControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LivroService livroService;

    // ---------- GET /livro ----------

    @Test
    @DisplayName("GET /livro deve retornar 200 com a lista de livros")
    void listar_deveRetornar200ComOsLivros() throws Exception {
        when(livroService.listar()).thenReturn(List.of(
                new Livro(1L, "Clean Code", "Robert C. Martin", 2008),
                new Livro(2L, "Refatoração", "Martin Fowler", 1999)
        ));

        mockMvc.perform(get("/livro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Clean Code"))
                .andExpect(jsonPath("$[1].autor").value("Martin Fowler"));

        verify(livroService).listar();
    }

    @Test
    @DisplayName("GET /livro deve retornar 200 com lista vazia quando não há livros")
    void listar_deveRetornar200ComListaVazia() throws Exception {
        when(livroService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/livro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(0)));
    }

    // ---------- POST /livro ----------

    @Test
    @DisplayName("POST /livro deve retornar 201 e o livro criado quando o payload é válido")
    void criar_deveRetornar201() throws Exception {
        LivroRequest request = new LivroRequest("Clean Code", "Robert C. Martin", 2008);
        when(livroService.salvar(any(LivroRequest.class)))
                .thenReturn(new Livro(1L, "Clean Code", "Robert C. Martin", 2008));

        mockMvc.perform(post("/livro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Clean Code"))
                .andExpect(jsonPath("$.autor").value("Robert C. Martin"))
                .andExpect(jsonPath("$.anoPublicacao").value(2008));

        verify(livroService).salvar(any(LivroRequest.class));
    }

    @Test
    @DisplayName("POST /livro deve retornar 400 quando o payload é inválido")
    void criar_deveRetornar400QuandoPayloadInvalido() throws Exception {
        // título em branco e ano nulo violam as validações do request
        LivroRequest request = new LivroRequest("", "Robert C. Martin", null);

        mockMvc.perform(post("/livro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.titulo").exists())
                .andExpect(jsonPath("$.anoPublicacao").exists());
    }

    @Test
    @DisplayName("POST /livro deve retornar 409 quando já existe livro com o mesmo título")
    void criar_deveRetornar409QuandoTituloDuplicado() throws Exception {
        LivroRequest request = new LivroRequest("Clean Code", "Robert C. Martin", 2008);
        when(livroService.salvar(any(LivroRequest.class)))
                .thenThrow(new IllegalStateException("Já existe um livro com o título: Clean Code"));

        mockMvc.perform(post("/livro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erro").value("Já existe um livro com o título: Clean Code"));
    }

    // ---------- PUT /livro/{id} ----------

    @Test
    @DisplayName("PUT /livro/{id} deve retornar 200 e o livro atualizado")
    void atualizar_deveRetornar200() throws Exception {
        LivroRequest request = new LivroRequest("Clean Code (2ª ed.)", "Robert C. Martin", 2010);
        when(livroService.atualizar(eq(1L), any(LivroRequest.class)))
                .thenReturn(new Livro(1L, "Clean Code (2ª ed.)", "Robert C. Martin", 2010));

        mockMvc.perform(put("/livro/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Clean Code (2ª ed.)"))
                .andExpect(jsonPath("$.anoPublicacao").value(2010));

        verify(livroService).atualizar(eq(1L), any(LivroRequest.class));
    }

    @Test
    @DisplayName("PUT /livro/{id} deve retornar 404 quando o livro não existe")
    void atualizar_deveRetornar404QuandoNaoEncontrado() throws Exception {
        LivroRequest request = new LivroRequest("Clean Code", "Robert C. Martin", 2008);
        when(livroService.atualizar(eq(99L), any(LivroRequest.class)))
                .thenThrow(new NoSuchElementException("Livro não encontrado"));

        mockMvc.perform(put("/livro/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Livro não encontrado"));
    }

    @Test
    @DisplayName("PUT /livro/{id} deve retornar 400 quando o ano é maior que o ano atual")
    void atualizar_deveRetornar400QuandoAnoInvalido() throws Exception {
        LivroRequest request = new LivroRequest("Clean Code", "Robert C. Martin", 9999);

        mockMvc.perform(put("/livro/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.anoValido").exists());
    }

    // ---------- DELETE /livro/{id} ----------

    @Test
    @DisplayName("DELETE /livro/{id} deve retornar 204 quando o livro é removido")
    void remover_deveRetornar204() throws Exception {
        doNothing().when(livroService).remover(1L);

        mockMvc.perform(delete("/livro/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(livroService).remover(1L);
    }

    @Test
    @DisplayName("DELETE /livro/{id} deve retornar 404 quando o livro não existe")
    void remover_deveRetornar404QuandoNaoEncontrado() throws Exception {
        doThrow(new NoSuchElementException("Livro não encontrado: 99"))
                .when(livroService).remover(99L);

        mockMvc.perform(delete("/livro/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Livro não encontrado: 99"));
    }
}
