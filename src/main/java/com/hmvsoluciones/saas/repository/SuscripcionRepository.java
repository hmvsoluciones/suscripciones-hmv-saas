package com.hmvsoluciones.saas.repository;

import com.hmvsoluciones.saas.domain.Suscripcion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Suscripcion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long>, JpaSpecificationExecutor<Suscripcion> {}
