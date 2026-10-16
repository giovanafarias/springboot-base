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
 * A Curso.
 */
@Document(collection = "curso")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Curso implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nome")
    private String nome;

    @NotNull
    @Field("duracao")
    private Integer duracao;

    @DBRef
    @Field("disciplina")
    @JsonIgnoreProperties(value = { "curso" }, allowSetters = true)
    private Set<Disciplina> disciplinas = new HashSet<>();

    @DBRef
    @Field("aluno")
    @JsonIgnoreProperties(value = { "matriculas", "curso" }, allowSetters = true)
    private Set<Aluno> alunos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Curso id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Curso nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getDuracao() {
        return this.duracao;
    }

    public Curso duracao(Integer duracao) {
        this.setDuracao(duracao);
        return this;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public Set<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }

    public void setDisciplinas(Set<Disciplina> disciplinas) {
        if (this.disciplinas != null) {
            this.disciplinas.forEach(i -> i.setCurso(null));
        }
        if (disciplinas != null) {
            disciplinas.forEach(i -> i.setCurso(this));
        }
        this.disciplinas = disciplinas;
    }

    public Curso disciplinas(Set<Disciplina> disciplinas) {
        this.setDisciplinas(disciplinas);
        return this;
    }

    public Curso addDisciplina(Disciplina disciplina) {
        this.disciplinas.add(disciplina);
        disciplina.setCurso(this);
        return this;
    }

    public Curso removeDisciplina(Disciplina disciplina) {
        this.disciplinas.remove(disciplina);
        disciplina.setCurso(null);
        return this;
    }

    public Set<Aluno> getAlunos() {
        return this.alunos;
    }

    public void setAlunos(Set<Aluno> alunos) {
        if (this.alunos != null) {
            this.alunos.forEach(i -> i.setCurso(null));
        }
        if (alunos != null) {
            alunos.forEach(i -> i.setCurso(this));
        }
        this.alunos = alunos;
    }

    public Curso alunos(Set<Aluno> alunos) {
        this.setAlunos(alunos);
        return this;
    }

    public Curso addAluno(Aluno aluno) {
        this.alunos.add(aluno);
        aluno.setCurso(this);
        return this;
    }

    public Curso removeAluno(Aluno aluno) {
        this.alunos.remove(aluno);
        aluno.setCurso(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curso)) {
            return false;
        }
        return getId() != null && getId().equals(((Curso) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Curso{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", duracao=" + getDuracao() +
            "}";
    }
}
