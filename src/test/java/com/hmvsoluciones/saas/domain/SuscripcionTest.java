package com.hmvsoluciones.saas.domain;

import static com.hmvsoluciones.saas.domain.ClienteTestSamples.*;
import static com.hmvsoluciones.saas.domain.PagoTestSamples.*;
import static com.hmvsoluciones.saas.domain.PlanTestSamples.*;
import static com.hmvsoluciones.saas.domain.SuscripcionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.saas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SuscripcionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Suscripcion.class);
        Suscripcion suscripcion1 = getSuscripcionSample1();
        Suscripcion suscripcion2 = new Suscripcion();
        assertThat(suscripcion1).isNotEqualTo(suscripcion2);

        suscripcion2.setId(suscripcion1.getId());
        assertThat(suscripcion1).isEqualTo(suscripcion2);

        suscripcion2 = getSuscripcionSample2();
        assertThat(suscripcion1).isNotEqualTo(suscripcion2);
    }

    @Test
    void clienteTest() {
        Suscripcion suscripcion = getSuscripcionRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        suscripcion.setCliente(clienteBack);
        assertThat(suscripcion.getCliente()).isEqualTo(clienteBack);

        suscripcion.cliente(null);
        assertThat(suscripcion.getCliente()).isNull();
    }

    @Test
    void planTest() {
        Suscripcion suscripcion = getSuscripcionRandomSampleGenerator();
        Plan planBack = getPlanRandomSampleGenerator();

        suscripcion.setPlan(planBack);
        assertThat(suscripcion.getPlan()).isEqualTo(planBack);

        suscripcion.plan(null);
        assertThat(suscripcion.getPlan()).isNull();
    }

    @Test
    void pagoTest() {
        Suscripcion suscripcion = getSuscripcionRandomSampleGenerator();
        Pago pagoBack = getPagoRandomSampleGenerator();

        suscripcion.addPago(pagoBack);
        assertThat(suscripcion.getPagos()).containsOnly(pagoBack);
        assertThat(pagoBack.getSuscripcion()).isEqualTo(suscripcion);

        suscripcion.removePago(pagoBack);
        assertThat(suscripcion.getPagos()).doesNotContain(pagoBack);
        assertThat(pagoBack.getSuscripcion()).isNull();

        suscripcion.pagos(new HashSet<>(Set.of(pagoBack)));
        assertThat(suscripcion.getPagos()).containsOnly(pagoBack);
        assertThat(pagoBack.getSuscripcion()).isEqualTo(suscripcion);

        suscripcion.setPagos(new HashSet<>());
        assertThat(suscripcion.getPagos()).doesNotContain(pagoBack);
        assertThat(pagoBack.getSuscripcion()).isNull();
    }
}
