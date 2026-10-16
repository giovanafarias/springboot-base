package com.aula.springapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MatriculaTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Matricula getMatriculaSample1() {
        return new Matricula().id("id1").anoLetivo(1);
    }

    public static Matricula getMatriculaSample2() {
        return new Matricula().id("id2").anoLetivo(2);
    }

    public static Matricula getMatriculaRandomSampleGenerator() {
        return new Matricula().id(UUID.randomUUID().toString()).anoLetivo(intCount.incrementAndGet());
    }
}
