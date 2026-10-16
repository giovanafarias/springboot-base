package com.aula.springapp.domain;

import java.util.UUID;

public class AlunoTestSamples {

    public static Aluno getAlunoSample1() {
        return new Aluno().id("id1").nome("nome1");
    }

    public static Aluno getAlunoSample2() {
        return new Aluno().id("id2").nome("nome2");
    }

    public static Aluno getAlunoRandomSampleGenerator() {
        return new Aluno().id(UUID.randomUUID().toString()).nome(UUID.randomUUID().toString());
    }
}
