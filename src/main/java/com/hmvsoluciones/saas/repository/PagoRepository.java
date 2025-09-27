package com.hmvsoluciones.saas.repository;

import com.hmvsoluciones.saas.domain.Pago;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pago entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long>, JpaSpecificationExecutor<Pago> {}
