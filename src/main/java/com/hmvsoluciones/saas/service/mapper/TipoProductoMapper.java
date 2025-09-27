package com.hmvsoluciones.saas.service.mapper;

import com.hmvsoluciones.saas.domain.TipoProducto;
import com.hmvsoluciones.saas.service.dto.TipoProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoProducto} and its DTO {@link TipoProductoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoProductoMapper extends EntityMapper<TipoProductoDTO, TipoProducto> {}
