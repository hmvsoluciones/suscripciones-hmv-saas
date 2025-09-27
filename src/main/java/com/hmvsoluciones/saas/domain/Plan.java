package com.hmvsoluciones.saas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hmvsoluciones.saas.domain.enumeration.TipoPagoPlan;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Plan.
 */
@Entity
@Table(name = "plan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 500)
    @Column(name = "descripcion", length = 500, nullable = false)
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio", precision = 21, scale = 2, nullable = false)
    private BigDecimal precio;

    @NotNull
    @Min(value = 1)
    @Column(name = "duracion_meses", nullable = false)
    private Integer duracionMeses;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private TipoPagoPlan tipoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tipoProducto", "plans" }, allowSetters = true)
    private Producto producto;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
    @JsonIgnoreProperties(value = { "cliente", "plan", "pagos" }, allowSetters = true)
    private Set<Suscripcion> suscripcions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Plan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Plan nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Plan descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return this.precio;
    }

    public Plan precio(BigDecimal precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getDuracionMeses() {
        return this.duracionMeses;
    }

    public Plan duracionMeses(Integer duracionMeses) {
        this.setDuracionMeses(duracionMeses);
        return this;
    }

    public void setDuracionMeses(Integer duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public TipoPagoPlan getTipoPago() {
        return this.tipoPago;
    }

    public Plan tipoPago(TipoPagoPlan tipoPago) {
        this.setTipoPago(tipoPago);
        return this;
    }

    public void setTipoPago(TipoPagoPlan tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Plan producto(Producto producto) {
        this.setProducto(producto);
        return this;
    }

    public Set<Suscripcion> getSuscripcions() {
        return this.suscripcions;
    }

    public void setSuscripcions(Set<Suscripcion> suscripcions) {
        if (this.suscripcions != null) {
            this.suscripcions.forEach(i -> i.setPlan(null));
        }
        if (suscripcions != null) {
            suscripcions.forEach(i -> i.setPlan(this));
        }
        this.suscripcions = suscripcions;
    }

    public Plan suscripcions(Set<Suscripcion> suscripcions) {
        this.setSuscripcions(suscripcions);
        return this;
    }

    public Plan addSuscripcion(Suscripcion suscripcion) {
        this.suscripcions.add(suscripcion);
        suscripcion.setPlan(this);
        return this;
    }

    public Plan removeSuscripcion(Suscripcion suscripcion) {
        this.suscripcions.remove(suscripcion);
        suscripcion.setPlan(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plan)) {
            return false;
        }
        return getId() != null && getId().equals(((Plan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plan{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precio=" + getPrecio() +
            ", duracionMeses=" + getDuracionMeses() +
            ", tipoPago='" + getTipoPago() + "'" +
            "}";
    }
}
