package com.hmvsoluciones.saas.service.criteria;

import com.hmvsoluciones.saas.domain.enumeration.MetodoPago;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hmvsoluciones.saas.domain.Pago} entity. This class is used
 * in {@link com.hmvsoluciones.saas.web.rest.PagoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pagos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PagoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MetodoPago
     */
    public static class MetodoPagoFilter extends Filter<MetodoPago> {

        public MetodoPagoFilter() {}

        public MetodoPagoFilter(MetodoPagoFilter filter) {
            super(filter);
        }

        @Override
        public MetodoPagoFilter copy() {
            return new MetodoPagoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaPago;

    private BigDecimalFilter monto;

    private MetodoPagoFilter metodoPago;

    private StringFilter referencia;

    private LongFilter suscripcionId;

    private Boolean distinct;

    public PagoCriteria() {}

    public PagoCriteria(PagoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fechaPago = other.optionalFechaPago().map(LocalDateFilter::copy).orElse(null);
        this.monto = other.optionalMonto().map(BigDecimalFilter::copy).orElse(null);
        this.metodoPago = other.optionalMetodoPago().map(MetodoPagoFilter::copy).orElse(null);
        this.referencia = other.optionalReferencia().map(StringFilter::copy).orElse(null);
        this.suscripcionId = other.optionalSuscripcionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PagoCriteria copy() {
        return new PagoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getFechaPago() {
        return fechaPago;
    }

    public Optional<LocalDateFilter> optionalFechaPago() {
        return Optional.ofNullable(fechaPago);
    }

    public LocalDateFilter fechaPago() {
        if (fechaPago == null) {
            setFechaPago(new LocalDateFilter());
        }
        return fechaPago;
    }

    public void setFechaPago(LocalDateFilter fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimalFilter getMonto() {
        return monto;
    }

    public Optional<BigDecimalFilter> optionalMonto() {
        return Optional.ofNullable(monto);
    }

    public BigDecimalFilter monto() {
        if (monto == null) {
            setMonto(new BigDecimalFilter());
        }
        return monto;
    }

    public void setMonto(BigDecimalFilter monto) {
        this.monto = monto;
    }

    public MetodoPagoFilter getMetodoPago() {
        return metodoPago;
    }

    public Optional<MetodoPagoFilter> optionalMetodoPago() {
        return Optional.ofNullable(metodoPago);
    }

    public MetodoPagoFilter metodoPago() {
        if (metodoPago == null) {
            setMetodoPago(new MetodoPagoFilter());
        }
        return metodoPago;
    }

    public void setMetodoPago(MetodoPagoFilter metodoPago) {
        this.metodoPago = metodoPago;
    }

    public StringFilter getReferencia() {
        return referencia;
    }

    public Optional<StringFilter> optionalReferencia() {
        return Optional.ofNullable(referencia);
    }

    public StringFilter referencia() {
        if (referencia == null) {
            setReferencia(new StringFilter());
        }
        return referencia;
    }

    public void setReferencia(StringFilter referencia) {
        this.referencia = referencia;
    }

    public LongFilter getSuscripcionId() {
        return suscripcionId;
    }

    public Optional<LongFilter> optionalSuscripcionId() {
        return Optional.ofNullable(suscripcionId);
    }

    public LongFilter suscripcionId() {
        if (suscripcionId == null) {
            setSuscripcionId(new LongFilter());
        }
        return suscripcionId;
    }

    public void setSuscripcionId(LongFilter suscripcionId) {
        this.suscripcionId = suscripcionId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PagoCriteria that = (PagoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaPago, that.fechaPago) &&
            Objects.equals(monto, that.monto) &&
            Objects.equals(metodoPago, that.metodoPago) &&
            Objects.equals(referencia, that.referencia) &&
            Objects.equals(suscripcionId, that.suscripcionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaPago, monto, metodoPago, referencia, suscripcionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFechaPago().map(f -> "fechaPago=" + f + ", ").orElse("") +
            optionalMonto().map(f -> "monto=" + f + ", ").orElse("") +
            optionalMetodoPago().map(f -> "metodoPago=" + f + ", ").orElse("") +
            optionalReferencia().map(f -> "referencia=" + f + ", ").orElse("") +
            optionalSuscripcionId().map(f -> "suscripcionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
