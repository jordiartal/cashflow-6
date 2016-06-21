package org.cashflow6.app.repository;

import org.cashflow6.app.domain.Savings;
import org.cashflow6.app.domain.SavingsAudit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the SavingsAudit entity.
 */
@SuppressWarnings("unused")
public interface SavingsAuditRepository extends JpaRepository<SavingsAudit,Long> {

    Page<SavingsAudit> findSavingsAuditBySavings(@Param("id") Savings id, Pageable pageable);
}
