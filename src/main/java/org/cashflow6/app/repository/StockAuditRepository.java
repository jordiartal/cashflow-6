package org.cashflow6.app.repository;

import org.cashflow6.app.domain.StockAudit;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockAudit entity.
 */
@SuppressWarnings("unused")
public interface StockAuditRepository extends JpaRepository<StockAudit,Long> {

}
