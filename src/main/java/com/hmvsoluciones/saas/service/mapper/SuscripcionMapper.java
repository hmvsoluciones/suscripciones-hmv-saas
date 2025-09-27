package com.hmvsoluciones.saas.service.mapper;

import com.hmvsoluciones.saas.domain.Cliente;
import com.hmvsoluciones.saas.domain.Plan;
import com.hmvsoluciones.saas.domain.Suscripcion;
import com.hmvsoluciones.saas.service.dto.ClienteDTO;
import com.hmvsoluciones.saas.service.dto.PlanDTO;
import com.hmvsoluciones.saas.service.dto.SuscripcionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Suscripcion} and its DTO {@link SuscripcionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SuscripcionMapper extends EntityMapper<SuscripcionDTO, Suscripcion> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteId")
    @Mapping(target = "plan", source = "plan", qualifiedByName = "planId")
    SuscripcionDTO toDto(Suscripcion s);

    @Named("clienteId")
    @Mapping(target = "id", source = "id")
    ClienteDTO toDtoClienteId(Cliente cliente);

    @Named("planId")
    @Mapping(target = "id", source = "id")
    PlanDTO toDtoPlanId(Plan plan);
}
