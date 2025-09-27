package com.hmvsoluciones.saas.service.mapper;

import com.hmvsoluciones.saas.domain.Plan;
import com.hmvsoluciones.saas.domain.Producto;
import com.hmvsoluciones.saas.service.dto.PlanDTO;
import com.hmvsoluciones.saas.service.dto.ProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plan} and its DTO {@link PlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlanMapper extends EntityMapper<PlanDTO, Plan> {
    @Mapping(target = "producto", source = "producto", qualifiedByName = "productoId")
    PlanDTO toDto(Plan s);

    @Named("productoId")
    @Mapping(target = "id", source = "id")
    ProductoDTO toDtoProductoId(Producto producto);
}
