package org.cashflow6.app.repository;

import org.cashflow6.app.domain.Funds;
import org.cashflow6.app.domain.FundsAudit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the FundsAudit entity.
 */
@SuppressWarnings("unused")
public interface FundsAuditRepository extends JpaRepository<FundsAudit,Long> {

    Page<FundsAudit> findFundsAuditByFunds(@Param("id") Funds id, Pageable pageable);
}
