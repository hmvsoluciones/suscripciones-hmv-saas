package com.hmvsoluciones.saas.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PagoCriteriaTest {

    @Test
    void newPagoCriteriaHasAllFiltersNullTest() {
        var pagoCriteria = new PagoCriteria();
        assertThat(pagoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pagoCriteriaFluentMethodsCreatesFiltersTest() {
        var pagoCriteria = new PagoCriteria();

        setAllFilters(pagoCriteria);

        assertThat(pagoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pagoCriteriaCopyCreatesNullFilterTest() {
        var pagoCriteria = new PagoCriteria();
        var copy = pagoCriteria.copy();

        assertThat(pagoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pagoCriteria)
        );
    }

    @Test
    void pagoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pagoCriteria = new PagoCriteria();
        setAllFilters(pagoCriteria);

        var copy = pagoCriteria.copy();

        assertThat(pagoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pagoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pagoCriteria = new PagoCriteria();

        assertThat(pagoCriteria).hasToString("PagoCriteria{}");
    }

    private static void setAllFilters(PagoCriteria pagoCriteria) {
        pagoCriteria.id();
        pagoCriteria.fechaPago();
        pagoCriteria.monto();
        pagoCriteria.metodoPago();
        pagoCriteria.referencia();
        pagoCriteria.suscripcionId();
        pagoCriteria.distinct();
    }

    private static Condition<PagoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFechaPago()) &&
                condition.apply(criteria.getMonto()) &&
                condition.apply(criteria.getMetodoPago()) &&
                condition.apply(criteria.getReferencia()) &&
                condition.apply(criteria.getSuscripcionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PagoCriteria> copyFiltersAre(PagoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFechaPago(), copy.getFechaPago()) &&
                condition.apply(criteria.getMonto(), copy.getMonto()) &&
                condition.apply(criteria.getMetodoPago(), copy.getMetodoPago()) &&
                condition.apply(criteria.getReferencia(), copy.getReferencia()) &&
                condition.apply(criteria.getSuscripcionId(), copy.getSuscripcionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
