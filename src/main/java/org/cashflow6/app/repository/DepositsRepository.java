package org.cashflow6.app.repository;

import org.cashflow6.app.domain.Deposits;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Deposits entity.
 */
@SuppressWarnings("unused")
public interface DepositsRepository extends JpaRepository<Deposits,Long> {

    @Query("select deposits from Deposits deposits where deposits.user.login = ?#{principal.username}")
    Page<Deposits> findByUserIsCurrentUser(Pageable pageable);

}
