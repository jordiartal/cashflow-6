package org.cashflow6.app.repository;

import org.cashflow6.app.domain.Stock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Stock entity.
 */
@SuppressWarnings("unused")
public interface StockRepository extends JpaRepository<Stock,Long> {

    @Query("select stock from Stock stock where stock.user.login = ?#{principal.username}")
    Page<Stock> findByUserIsCurrentUser(Pageable pageable);

}
