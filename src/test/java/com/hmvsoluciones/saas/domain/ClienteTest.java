package com.hmvsoluciones.saas.domain;

import static com.hmvsoluciones.saas.domain.ClienteTestSamples.*;
import static com.hmvsoluciones.saas.domain.SuscripcionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.saas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cliente.class);
        Cliente cliente1 = getClienteSample1();
        Cliente cliente2 = new Cliente();
        assertThat(cliente1).isNotEqualTo(cliente2);

        cliente2.setId(cliente1.getId());
        assertThat(cliente1).isEqualTo(cliente2);

        cliente2 = getClienteSample2();
        assertThat(cliente1).isNotEqualTo(cliente2);
    }

    @Test
    void suscripcionTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Suscripcion suscripcionBack = getSuscripcionRandomSampleGenerator();

        cliente.addSuscripcion(suscripcionBack);
        assertThat(cliente.getSuscripcions()).containsOnly(suscripcionBack);
        assertThat(suscripcionBack.getCliente()).isEqualTo(cliente);

        cliente.removeSuscripcion(suscripcionBack);
        assertThat(cliente.getSuscripcions()).doesNotContain(suscripcionBack);
        assertThat(suscripcionBack.getCliente()).isNull();

        cliente.suscripcions(new HashSet<>(Set.of(suscripcionBack)));
        assertThat(cliente.getSuscripcions()).containsOnly(suscripcionBack);
        assertThat(suscripcionBack.getCliente()).isEqualTo(cliente);

        cliente.setSuscripcions(new HashSet<>());
        assertThat(cliente.getSuscripcions()).doesNotContain(suscripcionBack);
        assertThat(suscripcionBack.getCliente()).isNull();
    }
}
