package com.hmvsoluciones.saas.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SuscripcionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Suscripcion getSuscripcionSample1() {
        return new Suscripcion().id(1L);
    }

    public static Suscripcion getSuscripcionSample2() {
        return new Suscripcion().id(2L);
    }

    public static Suscripcion getSuscripcionRandomSampleGenerator() {
        return new Suscripcion().id(longCount.incrementAndGet());
    }
}
