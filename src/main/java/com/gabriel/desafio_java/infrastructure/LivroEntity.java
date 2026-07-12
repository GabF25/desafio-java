package com.gabriel.desafio_java.infrastructure;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "livros")
@EntityListeners(AuditingEntityListener.class)
public class LivroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "titulo", nullable = false, length = 200, unique = true)
    String titulo;

    @Column(name = "autor", nullable = false, length = 200)
    String autor;

    @Column(name = "ano_publicacao", nullable = false)
    Integer anoPublicacao;

    @CreatedDate
    @Column(name = "criado_em", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    LocalDateTime criadoEm;

    @LastModifiedDate
    @Column(name = "atualizado_em")
    @Setter(AccessLevel.NONE)
    LocalDateTime atualizadoEm;

    public LivroEntity(String titulo, String autor, Integer anoPublicacao) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
    }
}
