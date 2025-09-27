package com.hmvsoluciones.saas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hmvsoluciones.saas.domain.TipoProducto} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoProductoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoProductoDTO)) {
            return false;
        }

        TipoProductoDTO tipoProductoDTO = (TipoProductoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoProductoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoProductoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
