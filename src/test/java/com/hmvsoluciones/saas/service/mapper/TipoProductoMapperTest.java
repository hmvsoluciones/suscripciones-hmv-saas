package com.hmvsoluciones.saas.service.mapper;

import static com.hmvsoluciones.saas.domain.TipoProductoAsserts.*;
import static com.hmvsoluciones.saas.domain.TipoProductoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoProductoMapperTest {

    private TipoProductoMapper tipoProductoMapper;

    @BeforeEach
    void setUp() {
        tipoProductoMapper = new TipoProductoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTipoProductoSample1();
        var actual = tipoProductoMapper.toEntity(tipoProductoMapper.toDto(expected));
        assertTipoProductoAllPropertiesEquals(expected, actual);
    }
}
