package com.aula.springapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Aluno.
 */
@Document(collection = "aluno")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Aluno implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nome")
    private String nome;

    @DBRef
    @Field("matricula")
    @JsonIgnoreProperties(value = { "aluno" }, allowSetters = true)
    private Set<Matricula> matriculas = new HashSet<>();

    @DBRef
    @Field("curso")
    @JsonIgnoreProperties(value = { "disciplinas", "alunos" }, allowSetters = true)
    private Curso curso;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Aluno id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Aluno nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Matricula> getMatriculas() {
        return this.matriculas;
    }

    public void setMatriculas(Set<Matricula> matriculas) {
        if (this.matriculas != null) {
            this.matriculas.forEach(i -> i.setAluno(null));
        }
        if (matriculas != null) {
            matriculas.forEach(i -> i.setAluno(this));
        }
        this.matriculas = matriculas;
    }

    public Aluno matriculas(Set<Matricula> matriculas) {
        this.setMatriculas(matriculas);
        return this;
    }

    public Aluno addMatricula(Matricula matricula) {
        this.matriculas.add(matricula);
        matricula.setAluno(this);
        return this;
    }

    public Aluno removeMatricula(Matricula matricula) {
        this.matriculas.remove(matricula);
        matricula.setAluno(null);
        return this;
    }

    public Curso getCurso() {
        return this.curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Aluno curso(Curso curso) {
        this.setCurso(curso);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aluno)) {
            return false;
        }
        return getId() != null && getId().equals(((Aluno) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Aluno{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
