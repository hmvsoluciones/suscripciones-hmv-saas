package com.hmvsoluciones.saas.service.mapper;

import com.hmvsoluciones.saas.domain.Producto;
import com.hmvsoluciones.saas.domain.TipoProducto;
import com.hmvsoluciones.saas.service.dto.ProductoDTO;
import com.hmvsoluciones.saas.service.dto.TipoProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {
    @Mapping(target = "tipoProducto", source = "tipoProducto", qualifiedByName = "tipoProductoId")
    ProductoDTO toDto(Producto s);

    @Named("tipoProductoId")
    @Mapping(target = "id", source = "id")
    TipoProductoDTO toDtoTipoProductoId(TipoProducto tipoProducto);
}
