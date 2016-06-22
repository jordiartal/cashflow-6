package org.cashflow6.app.repository;

import org.cashflow6.app.domain.Stock;
import org.cashflow6.app.domain.StockAudit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the StockAudit entity.
 */
@SuppressWarnings("unused")
public interface StockAuditRepository extends JpaRepository<StockAudit,Long> {
    Page<StockAudit> findStockAuditByStock(@Param("id") Stock id, Pageable pageable);
}
