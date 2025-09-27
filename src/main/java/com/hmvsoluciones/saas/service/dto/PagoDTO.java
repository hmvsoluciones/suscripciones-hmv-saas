package com.hmvsoluciones.saas.service.dto;

import com.hmvsoluciones.saas.domain.enumeration.MetodoPago;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.hmvsoluciones.saas.domain.Pago} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PagoDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fechaPago;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal monto;

    @NotNull
    private MetodoPago metodoPago;

    @Size(max = 200)
    private String referencia;

    private SuscripcionDTO suscripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public SuscripcionDTO getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(SuscripcionDTO suscripcion) {
        this.suscripcion = suscripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PagoDTO)) {
            return false;
        }

        PagoDTO pagoDTO = (PagoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pagoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagoDTO{" +
            "id=" + getId() +
            ", fechaPago='" + getFechaPago() + "'" +
            ", monto=" + getMonto() +
            ", metodoPago='" + getMetodoPago() + "'" +
            ", referencia='" + getReferencia() + "'" +
            ", suscripcion=" + getSuscripcion() +
            "}";
    }
}
