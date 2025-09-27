package com.hmvsoluciones.saas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Plan getPlanSample1() {
        return new Plan().id(1L).nombre("nombre1").descripcion("descripcion1").duracionMeses(1);
    }

    public static Plan getPlanSample2() {
        return new Plan().id(2L).nombre("nombre2").descripcion("descripcion2").duracionMeses(2);
    }

    public static Plan getPlanRandomSampleGenerator() {
        return new Plan()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .duracionMeses(intCount.incrementAndGet());
    }
}
