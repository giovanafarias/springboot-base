package com.aula.springapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CursoTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Curso getCursoSample1() {
        return new Curso().id("id1").nome("nome1").duracao(1);
    }

    public static Curso getCursoSample2() {
        return new Curso().id("id2").nome("nome2").duracao(2);
    }

    public static Curso getCursoRandomSampleGenerator() {
        return new Curso().id(UUID.randomUUID().toString()).nome(UUID.randomUUID().toString()).duracao(intCount.incrementAndGet());
    }
}
