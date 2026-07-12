package com.gabriel.desafio_java.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Year;

@Schema(description = "Dados para criação ou atualização de um livro")
public record LivroRequest(

        @Schema(description = "Título do livro", example = "Clean Code", maxLength = 200)
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
        String titulo,

        @Schema(description = "Autor do livro", example = "Robert C. Martin", maxLength = 200)
        @NotBlank(message = "O autor é obrigatório")
        @Size(max = 200, message = "O autor deve ter no máximo 200 caracteres")
        String autor,

        @Schema(description = "Ano de publicação", example = "2008")
        @NotNull(message = "O ano de publicação é obrigatório")
        Integer anoPublicacao
) {
    @JsonIgnore
    @Schema(hidden = true)
    @AssertTrue(message = "O ano não pode ser maior que o ano atual")
    public boolean isAnoValido() {
        return anoPublicacao == null || anoPublicacao <= Year.now().getValue();
    }
}
