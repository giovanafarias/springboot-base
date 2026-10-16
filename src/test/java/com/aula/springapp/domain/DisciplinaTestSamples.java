package com.aula.springapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class DisciplinaTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Disciplina getDisciplinaSample1() {
        return new Disciplina().id("id1").nome("nome1").cargaHoraria(1);
    }

    public static Disciplina getDisciplinaSample2() {
        return new Disciplina().id("id2").nome("nome2").cargaHoraria(2);
    }

    public static Disciplina getDisciplinaRandomSampleGenerator() {
        return new Disciplina()
            .id(UUID.randomUUID().toString())
            .nome(UUID.randomUUID().toString())
            .cargaHoraria(intCount.incrementAndGet());
    }
}
