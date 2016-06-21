package org.cashflow6.app.repository;

import org.cashflow6.app.domain.UserData;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserData entity.
 */
@SuppressWarnings("unused")
public interface UserDataRepository extends JpaRepository<UserData,Long> {

    @Query("select userData from UserData userData where userData.user.login = ?#{principal.username}")
    Page<UserData> findByUserIsCurrentUser(Pageable pageable);

}
