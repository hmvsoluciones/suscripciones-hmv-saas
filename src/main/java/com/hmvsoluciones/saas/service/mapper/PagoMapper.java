package com.hmvsoluciones.saas.service.mapper;

import com.hmvsoluciones.saas.domain.Pago;
import com.hmvsoluciones.saas.domain.Suscripcion;
import com.hmvsoluciones.saas.service.dto.PagoDTO;
import com.hmvsoluciones.saas.service.dto.SuscripcionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pago} and its DTO {@link PagoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PagoMapper extends EntityMapper<PagoDTO, Pago> {
    @Mapping(target = "suscripcion", source = "suscripcion", qualifiedByName = "suscripcionId")
    PagoDTO toDto(Pago s);

    @Named("suscripcionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SuscripcionDTO toDtoSuscripcionId(Suscripcion suscripcion);
}
