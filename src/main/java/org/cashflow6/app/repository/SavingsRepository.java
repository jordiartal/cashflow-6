package org.cashflow6.app.repository;

import org.cashflow6.app.domain.Savings;

import org.cashflow6.app.domain.SavingsAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Savings entity.
 */
@SuppressWarnings("unused")
public interface SavingsRepository extends JpaRepository<Savings,Long> {

    @Query("select savings from Savings savings where savings.user.login = ?#{principal.username}")
    Page<Savings> findByUserIsCurrentUser(Pageable pageable);

}
