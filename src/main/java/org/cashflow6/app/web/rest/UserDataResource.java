package org.cashflow6.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.cashflow6.app.domain.UserData;
import org.cashflow6.app.repository.UserDataRepository;
import org.cashflow6.app.security.AuthoritiesConstants;
import org.cashflow6.app.security.SecurityUtils;
import org.cashflow6.app.web.rest.util.HeaderUtil;
import org.cashflow6.app.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserData.
 */
@RestController
@RequestMapping("/api")
public class UserDataResource {

    private final Logger log = LoggerFactory.getLogger(UserDataResource.class);

    @Inject
    private UserDataRepository userDataRepository;

    /**
     * POST  /user-data : Create a new userData.
     *
     * @param userData the userData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userData, or with status 400 (Bad Request) if the userData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-data",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserData> createUserData(@Valid @RequestBody UserData userData) throws URISyntaxException {
        log.debug("REST request to save UserData : {}", userData);
        if (userData.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userData", "idexists", "A new userData cannot already have an ID")).body(null);
        }
        UserData result = userDataRepository.save(userData);
        return ResponseEntity.created(new URI("/api/user-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userData", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-data : Updates an existing userData.
     *
     * @param userData the userData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userData,
     * or with status 400 (Bad Request) if the userData is not valid,
     * or with status 500 (Internal Server Error) if the userData couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-data",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserData> updateUserData(@Valid @RequestBody UserData userData) throws URISyntaxException {
        log.debug("REST request to update UserData : {}", userData);
        if (userData.getId() == null) {
            return createUserData(userData);
        }
        UserData result = userDataRepository.save(userData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userData", userData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-data : get all the userData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userData in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/user-data",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserData>> getAllUserData(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserData");
        Page<UserData> page = null;
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = userDataRepository.findByUserIsCurrentUser(pageable);
        } else {
            page = userDataRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-data/:id : get the "id" userData.
     *
     * @param id the id of the userData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userData, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/user-data/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserData> getUserData(@PathVariable Long id) {
        log.debug("REST request to get UserData : {}", id);
        UserData userData = userDataRepository.findOne(id);
        return Optional.ofNullable(userData)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-data/:id : delete the "id" userData.
     *
     * @param id the id of the userData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/user-data/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserData(@PathVariable Long id) {
        log.debug("REST request to delete UserData : {}", id);
        userDataRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userData", id.toString())).build();
    }

}
