package org.cashflow6.app.repository;

import org.cashflow6.app.domain.FundsAudit;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FundsAudit entity.
 */
@SuppressWarnings("unused")
public interface FundsAuditRepository extends JpaRepository<FundsAudit,Long> {

}
