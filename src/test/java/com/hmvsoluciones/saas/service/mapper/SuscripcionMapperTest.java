package com.hmvsoluciones.saas.service.mapper;

import static com.hmvsoluciones.saas.domain.SuscripcionAsserts.*;
import static com.hmvsoluciones.saas.domain.SuscripcionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SuscripcionMapperTest {

    private SuscripcionMapper suscripcionMapper;

    @BeforeEach
    void setUp() {
        suscripcionMapper = new SuscripcionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSuscripcionSample1();
        var actual = suscripcionMapper.toEntity(suscripcionMapper.toDto(expected));
        assertSuscripcionAllPropertiesEquals(expected, actual);
    }
}
