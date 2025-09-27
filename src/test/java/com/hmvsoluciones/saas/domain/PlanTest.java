package com.hmvsoluciones.saas.domain;

import static com.hmvsoluciones.saas.domain.PlanTestSamples.*;
import static com.hmvsoluciones.saas.domain.ProductoTestSamples.*;
import static com.hmvsoluciones.saas.domain.SuscripcionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.saas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plan.class);
        Plan plan1 = getPlanSample1();
        Plan plan2 = new Plan();
        assertThat(plan1).isNotEqualTo(plan2);

        plan2.setId(plan1.getId());
        assertThat(plan1).isEqualTo(plan2);

        plan2 = getPlanSample2();
        assertThat(plan1).isNotEqualTo(plan2);
    }

    @Test
    void productoTest() {
        Plan plan = getPlanRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        plan.setProducto(productoBack);
        assertThat(plan.getProducto()).isEqualTo(productoBack);

        plan.producto(null);
        assertThat(plan.getProducto()).isNull();
    }

    @Test
    void suscripcionTest() {
        Plan plan = getPlanRandomSampleGenerator();
        Suscripcion suscripcionBack = getSuscripcionRandomSampleGenerator();

        plan.addSuscripcion(suscripcionBack);
        assertThat(plan.getSuscripcions()).containsOnly(suscripcionBack);
        assertThat(suscripcionBack.getPlan()).isEqualTo(plan);

        plan.removeSuscripcion(suscripcionBack);
        assertThat(plan.getSuscripcions()).doesNotContain(suscripcionBack);
        assertThat(suscripcionBack.getPlan()).isNull();

        plan.suscripcions(new HashSet<>(Set.of(suscripcionBack)));
        assertThat(plan.getSuscripcions()).containsOnly(suscripcionBack);
        assertThat(suscripcionBack.getPlan()).isEqualTo(plan);

        plan.setSuscripcions(new HashSet<>());
        assertThat(plan.getSuscripcions()).doesNotContain(suscripcionBack);
        assertThat(suscripcionBack.getPlan()).isNull();
    }
}
