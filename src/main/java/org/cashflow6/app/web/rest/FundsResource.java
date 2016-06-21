package org.cashflow6.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.cashflow6.app.domain.Funds;
import org.cashflow6.app.domain.FundsAudit;
import org.cashflow6.app.repository.FundsRepository;
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
 * REST controller for managing Funds.
 */
@RestController
@RequestMapping("/api")
public class FundsResource {

    private final Logger log = LoggerFactory.getLogger(FundsResource.class);

    @Inject
    private FundsRepository fundsRepository;

    @Inject
    private FundsAuditResource fundsAuditResource;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /funds : Create a new funds.
     *
     * @param funds the funds to create
     * @return the ResponseEntity with status 201 (Created) and with body the new funds, or with status 400 (Bad Request) if the funds has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/funds",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funds> createFunds(@RequestBody Funds funds) throws URISyntaxException {
        log.debug("REST request to save Funds : {}", funds);
        if (funds.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("funds", "idexists", "A new funds cannot already have an ID")).body(null);
        }
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            funds.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        }
        funds.setInitialDate(LocalDate.now());
        funds.setInitialValue(funds.getNewValue());
        funds.setActualValue(funds.getNewValue());
        funds.setInitialShares(funds.getNewShares());
        funds.setActualShares(funds.getNewShares());
        Funds result = fundsRepository.save(funds);

        FundsAudit va = new FundsAudit();
        va.setOpDate(LocalDate.now());
        va.setAntValue(funds.getActualValue());
        va.setNewValue(funds.getNewValue());
        va.setAntShares(funds.getActualShares());
        va.setNewShares(funds.getNewShares());
        va.setFunds(funds);
        fundsAuditResource.createFundsAudit(va);

        return ResponseEntity.created(new URI("/api/funds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("funds", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /funds : Updates an existing funds.
     *
     * @param funds the funds to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated funds,
     * or with status 400 (Bad Request) if the funds is not valid,
     * or with status 500 (Internal Server Error) if the funds couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/funds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funds> updateFunds(@RequestBody Funds funds) throws URISyntaxException {
        log.debug("REST request to update Funds : {}", funds);
        if (funds.getId() == null) {
            return createFunds(funds);
        }
        FundsAudit va = new FundsAudit();
        va.setOpDate(LocalDate.now());
        va.setAntValue(funds.getActualValue());
        va.setNewValue(funds.getNewValue());
        va.setAntShares(funds.getActualShares());
        va.setNewShares(funds.getNewShares());
        va.setFunds(funds);
        fundsAuditResource.createFundsAudit(va);

        funds.setActualValue(funds.getNewValue());
        funds.setActualShares(funds.getNewShares());
        Funds result = fundsRepository.save(funds);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("funds", funds.getId().toString()))
            .body(result);
    }

    /**
     * GET  /funds : get all the funds.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of funds in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/funds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Funds>> getAllFunds(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Funds");
        Page<Funds> page = null;
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = fundsRepository.findByUserIsCurrentUser(pageable);
        } else {
            page = fundsRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/funds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /funds/:id : get the "id" funds.
     *
     * @param id the id of the funds to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the funds, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/funds/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funds> getFunds(@PathVariable Long id) {
        log.debug("REST request to get Funds : {}", id);
        Funds funds = fundsRepository.findOne(id);
        return Optional.ofNullable(funds)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /funds/:id : delete the "id" funds.
     *
     * @param id the id of the funds to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/funds/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFunds(@PathVariable Long id) {
        log.debug("REST request to delete Funds : {}", id);
        fundsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("funds", id.toString())).build();
    }

}
