package com.hmvsoluciones.saas.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SuscripcionCriteriaTest {

    @Test
    void newSuscripcionCriteriaHasAllFiltersNullTest() {
        var suscripcionCriteria = new SuscripcionCriteria();
        assertThat(suscripcionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void suscripcionCriteriaFluentMethodsCreatesFiltersTest() {
        var suscripcionCriteria = new SuscripcionCriteria();

        setAllFilters(suscripcionCriteria);

        assertThat(suscripcionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void suscripcionCriteriaCopyCreatesNullFilterTest() {
        var suscripcionCriteria = new SuscripcionCriteria();
        var copy = suscripcionCriteria.copy();

        assertThat(suscripcionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(suscripcionCriteria)
        );
    }

    @Test
    void suscripcionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var suscripcionCriteria = new SuscripcionCriteria();
        setAllFilters(suscripcionCriteria);

        var copy = suscripcionCriteria.copy();

        assertThat(suscripcionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(suscripcionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var suscripcionCriteria = new SuscripcionCriteria();

        assertThat(suscripcionCriteria).hasToString("SuscripcionCriteria{}");
    }

    private static void setAllFilters(SuscripcionCriteria suscripcionCriteria) {
        suscripcionCriteria.id();
        suscripcionCriteria.fechaInicio();
        suscripcionCriteria.fechaFin();
        suscripcionCriteria.estado();
        suscripcionCriteria.clienteId();
        suscripcionCriteria.planId();
        suscripcionCriteria.pagoId();
        suscripcionCriteria.distinct();
    }

    private static Condition<SuscripcionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFechaInicio()) &&
                condition.apply(criteria.getFechaFin()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getPlanId()) &&
                condition.apply(criteria.getPagoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SuscripcionCriteria> copyFiltersAre(SuscripcionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFechaInicio(), copy.getFechaInicio()) &&
                condition.apply(criteria.getFechaFin(), copy.getFechaFin()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getPlanId(), copy.getPlanId()) &&
                condition.apply(criteria.getPagoId(), copy.getPagoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
