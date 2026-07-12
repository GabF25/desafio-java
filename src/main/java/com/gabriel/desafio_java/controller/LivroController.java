package com.gabriel.desafio_java.controller;

import com.gabriel.desafio_java.domain.Livro;
import com.gabriel.desafio_java.request.LivroRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livro")
@Tag(name = "Livros", description = "Operações para gerenciamento de livros")
public interface LivroController {

    @GetMapping
    @Operation(summary = "Listar livros", description = "Retorna todos os livros cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    ResponseEntity<List<Livro>> listar();

    @PostMapping
    @Operation(summary = "Criar livro", description = "Cadastra um novo livro.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload inválido",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "409", description = "Já existe um livro com o mesmo título",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    ResponseEntity<Livro> criar(@Valid @RequestBody LivroRequest request);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", description = "Atualiza um livro existente pelo id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload inválido (ex.: ano maior que o atual)",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    ResponseEntity<Livro> atualizar(
            @Parameter(description = "Id do livro", example = "1") @PathVariable Long id,
            @Valid @RequestBody LivroRequest request);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover livro", description = "Remove um livro existente pelo id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    ResponseEntity<Void> remover(
            @Parameter(description = "Id do livro", example = "1") @PathVariable Long id);
}
