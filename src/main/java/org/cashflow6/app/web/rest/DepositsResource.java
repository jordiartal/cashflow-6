package org.cashflow6.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.cashflow6.app.domain.Deposits;
import org.cashflow6.app.repository.DepositsRepository;
import org.cashflow6.app.repository.UserRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Deposits.
 */
@RestController
@RequestMapping("/api")
public class DepositsResource {

    private final Logger log = LoggerFactory.getLogger(DepositsResource.class);

    @Inject
    private DepositsRepository depositsRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /deposits : Create a new deposits.
     *
     * @param deposits the deposits to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deposits, or with status 400 (Bad Request) if the deposits has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/deposits",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deposits> createDeposits(@RequestBody Deposits deposits) throws URISyntaxException {
        log.debug("REST request to save Deposits : {}", deposits);
        if (deposits.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("deposits", "idexists", "A new deposits cannot already have an ID")).body(null);
        }
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            deposits.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        }
        deposits.setInitialDate(LocalDate.now());
        deposits.setFinalValue(deposits.getInitialValue()*(1+(deposits.getInterest()/100)));
        Deposits result = depositsRepository.save(deposits);
        return ResponseEntity.created(new URI("/api/deposits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("deposits", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /deposits : Updates an existing deposits.
     *
     * @param deposits the deposits to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deposits,
     * or with status 400 (Bad Request) if the deposits is not valid,
     * or with status 500 (Internal Server Error) if the deposits couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/deposits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deposits> updateDeposits(@RequestBody Deposits deposits) throws URISyntaxException {
        log.debug("REST request to update Deposits : {}", deposits);
        if (deposits.getId() == null) {
            return createDeposits(deposits);
        }
        deposits.setFinalValue(deposits.getInitialValue()*(1+(deposits.getInterest()/100)));
        Deposits result = depositsRepository.save(deposits);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("deposits", deposits.getId().toString()))
            .body(result);
    }

    /**
     * GET  /deposits : get all the deposits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of deposits in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/deposits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Deposits>> getAllDeposits(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Deposits");
        Page<Deposits> page = null;
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = depositsRepository.findByUserIsCurrentUser(pageable);
        } else {
            page = depositsRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/deposits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /deposits/:id : get the "id" deposits.
     *
     * @param id the id of the deposits to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deposits, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/deposits/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deposits> getDeposits(@PathVariable Long id) {
        log.debug("REST request to get Deposits : {}", id);
        Deposits deposits = depositsRepository.findOne(id);
        return Optional.ofNullable(deposits)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /deposits/:id : delete the "id" deposits.
     *
     * @param id the id of the deposits to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/deposits/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDeposits(@PathVariable Long id) {
        log.debug("REST request to delete Deposits : {}", id);
        depositsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("deposits", id.toString())).build();
    }

}
