package org.cashflow6.app.repository;

import org.cashflow6.app.domain.Funds;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Funds entity.
 */
@SuppressWarnings("unused")
public interface FundsRepository extends JpaRepository<Funds,Long> {

    @Query("select funds from Funds funds where funds.user.login = ?#{principal.username}")
    Page<Funds> findByUserIsCurrentUser(Pageable pageable);

}
