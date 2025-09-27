package com.hmvsoluciones.saas.service.criteria;

import com.hmvsoluciones.saas.domain.enumeration.TipoPagoPlan;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hmvsoluciones.saas.domain.Plan} entity. This class is used
 * in {@link com.hmvsoluciones.saas.web.rest.PlanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoPagoPlan
     */
    public static class TipoPagoPlanFilter extends Filter<TipoPagoPlan> {

        public TipoPagoPlanFilter() {}

        public TipoPagoPlanFilter(TipoPagoPlanFilter filter) {
            super(filter);
        }

        @Override
        public TipoPagoPlanFilter copy() {
            return new TipoPagoPlanFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private BigDecimalFilter precio;

    private IntegerFilter duracionMeses;

    private TipoPagoPlanFilter tipoPago;

    private LongFilter productoId;

    private LongFilter suscripcionId;

    private Boolean distinct;

    public PlanCriteria() {}

    public PlanCriteria(PlanCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.precio = other.optionalPrecio().map(BigDecimalFilter::copy).orElse(null);
        this.duracionMeses = other.optionalDuracionMeses().map(IntegerFilter::copy).orElse(null);
        this.tipoPago = other.optionalTipoPago().map(TipoPagoPlanFilter::copy).orElse(null);
        this.productoId = other.optionalProductoId().map(LongFilter::copy).orElse(null);
        this.suscripcionId = other.optionalSuscripcionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PlanCriteria copy() {
        return new PlanCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimalFilter getPrecio() {
        return precio;
    }

    public Optional<BigDecimalFilter> optionalPrecio() {
        return Optional.ofNullable(precio);
    }

    public BigDecimalFilter precio() {
        if (precio == null) {
            setPrecio(new BigDecimalFilter());
        }
        return precio;
    }

    public void setPrecio(BigDecimalFilter precio) {
        this.precio = precio;
    }

    public IntegerFilter getDuracionMeses() {
        return duracionMeses;
    }

    public Optional<IntegerFilter> optionalDuracionMeses() {
        return Optional.ofNullable(duracionMeses);
    }

    public IntegerFilter duracionMeses() {
        if (duracionMeses == null) {
            setDuracionMeses(new IntegerFilter());
        }
        return duracionMeses;
    }

    public void setDuracionMeses(IntegerFilter duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public TipoPagoPlanFilter getTipoPago() {
        return tipoPago;
    }

    public Optional<TipoPagoPlanFilter> optionalTipoPago() {
        return Optional.ofNullable(tipoPago);
    }

    public TipoPagoPlanFilter tipoPago() {
        if (tipoPago == null) {
            setTipoPago(new TipoPagoPlanFilter());
        }
        return tipoPago;
    }

    public void setTipoPago(TipoPagoPlanFilter tipoPago) {
        this.tipoPago = tipoPago;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public Optional<LongFilter> optionalProductoId() {
        return Optional.ofNullable(productoId);
    }

    public LongFilter productoId() {
        if (productoId == null) {
            setProductoId(new LongFilter());
        }
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
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
        final PlanCriteria that = (PlanCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(precio, that.precio) &&
            Objects.equals(duracionMeses, that.duracionMeses) &&
            Objects.equals(tipoPago, that.tipoPago) &&
            Objects.equals(productoId, that.productoId) &&
            Objects.equals(suscripcionId, that.suscripcionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion, precio, duracionMeses, tipoPago, productoId, suscripcionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalPrecio().map(f -> "precio=" + f + ", ").orElse("") +
            optionalDuracionMeses().map(f -> "duracionMeses=" + f + ", ").orElse("") +
            optionalTipoPago().map(f -> "tipoPago=" + f + ", ").orElse("") +
            optionalProductoId().map(f -> "productoId=" + f + ", ").orElse("") +
            optionalSuscripcionId().map(f -> "suscripcionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
