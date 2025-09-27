package com.hmvsoluciones.saas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.saas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SuscripcionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuscripcionDTO.class);
        SuscripcionDTO suscripcionDTO1 = new SuscripcionDTO();
        suscripcionDTO1.setId(1L);
        SuscripcionDTO suscripcionDTO2 = new SuscripcionDTO();
        assertThat(suscripcionDTO1).isNotEqualTo(suscripcionDTO2);
        suscripcionDTO2.setId(suscripcionDTO1.getId());
        assertThat(suscripcionDTO1).isEqualTo(suscripcionDTO2);
        suscripcionDTO2.setId(2L);
        assertThat(suscripcionDTO1).isNotEqualTo(suscripcionDTO2);
        suscripcionDTO1.setId(null);
        assertThat(suscripcionDTO1).isNotEqualTo(suscripcionDTO2);
    }
}
