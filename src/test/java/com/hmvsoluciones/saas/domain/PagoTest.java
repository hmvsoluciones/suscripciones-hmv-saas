package com.hmvsoluciones.saas.domain;

import static com.hmvsoluciones.saas.domain.PagoTestSamples.*;
import static com.hmvsoluciones.saas.domain.SuscripcionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.saas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PagoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pago.class);
        Pago pago1 = getPagoSample1();
        Pago pago2 = new Pago();
        assertThat(pago1).isNotEqualTo(pago2);

        pago2.setId(pago1.getId());
        assertThat(pago1).isEqualTo(pago2);

        pago2 = getPagoSample2();
        assertThat(pago1).isNotEqualTo(pago2);
    }

    @Test
    void suscripcionTest() {
        Pago pago = getPagoRandomSampleGenerator();
        Suscripcion suscripcionBack = getSuscripcionRandomSampleGenerator();

        pago.setSuscripcion(suscripcionBack);
        assertThat(pago.getSuscripcion()).isEqualTo(suscripcionBack);

        pago.suscripcion(null);
        assertThat(pago.getSuscripcion()).isNull();
    }
}
