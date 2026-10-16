package com.aula.springapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.aula.springapp.domain.Curso} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CursoDTO implements Serializable {

    private String id;

    @NotNull
    private String nome;

    @NotNull
    private Integer duracao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CursoDTO)) {
            return false;
        }

        CursoDTO cursoDTO = (CursoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cursoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CursoDTO{" +
            "id='" + getId() + "'" +
            ", nome='" + getNome() + "'" +
            ", duracao=" + getDuracao() +
            "}";
    }
}
