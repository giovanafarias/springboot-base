package com.aula.springapp.domain;

import static com.aula.springapp.domain.CursoTestSamples.*;
import static com.aula.springapp.domain.DisciplinaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aula.springapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisciplinaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disciplina.class);
        Disciplina disciplina1 = getDisciplinaSample1();
        Disciplina disciplina2 = new Disciplina();
        assertThat(disciplina1).isNotEqualTo(disciplina2);

        disciplina2.setId(disciplina1.getId());
        assertThat(disciplina1).isEqualTo(disciplina2);

        disciplina2 = getDisciplinaSample2();
        assertThat(disciplina1).isNotEqualTo(disciplina2);
    }

    @Test
    void cursoTest() {
        Disciplina disciplina = getDisciplinaRandomSampleGenerator();
        Curso cursoBack = getCursoRandomSampleGenerator();

        disciplina.setCurso(cursoBack);
        assertThat(disciplina.getCurso()).isEqualTo(cursoBack);

        disciplina.curso(null);
        assertThat(disciplina.getCurso()).isNull();
    }
}
