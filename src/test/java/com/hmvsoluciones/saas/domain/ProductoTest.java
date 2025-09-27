package com.hmvsoluciones.saas.domain;

import static com.hmvsoluciones.saas.domain.PlanTestSamples.*;
import static com.hmvsoluciones.saas.domain.ProductoTestSamples.*;
import static com.hmvsoluciones.saas.domain.TipoProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.saas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Producto.class);
        Producto producto1 = getProductoSample1();
        Producto producto2 = new Producto();
        assertThat(producto1).isNotEqualTo(producto2);

        producto2.setId(producto1.getId());
        assertThat(producto1).isEqualTo(producto2);

        producto2 = getProductoSample2();
        assertThat(producto1).isNotEqualTo(producto2);
    }

    @Test
    void tipoProductoTest() {
        Producto producto = getProductoRandomSampleGenerator();
        TipoProducto tipoProductoBack = getTipoProductoRandomSampleGenerator();

        producto.setTipoProducto(tipoProductoBack);
        assertThat(producto.getTipoProducto()).isEqualTo(tipoProductoBack);

        producto.tipoProducto(null);
        assertThat(producto.getTipoProducto()).isNull();
    }

    @Test
    void planTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Plan planBack = getPlanRandomSampleGenerator();

        producto.addPlan(planBack);
        assertThat(producto.getPlans()).containsOnly(planBack);
        assertThat(planBack.getProducto()).isEqualTo(producto);

        producto.removePlan(planBack);
        assertThat(producto.getPlans()).doesNotContain(planBack);
        assertThat(planBack.getProducto()).isNull();

        producto.plans(new HashSet<>(Set.of(planBack)));
        assertThat(producto.getPlans()).containsOnly(planBack);
        assertThat(planBack.getProducto()).isEqualTo(producto);

        producto.setPlans(new HashSet<>());
        assertThat(producto.getPlans()).doesNotContain(planBack);
        assertThat(planBack.getProducto()).isNull();
    }
}
