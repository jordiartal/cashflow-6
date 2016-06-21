package org.cashflow6.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.cashflow6.app.domain.FundsAudit;
import org.cashflow6.app.repository.FundsAuditRepository;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FundsAudit.
 */
@RestController
@RequestMapping("/api")
public class FundsAuditResource {

    private final Logger log = LoggerFactory.getLogger(FundsAuditResource.class);
        
    @Inject
    private FundsAuditRepository fundsAuditRepository;
    
    /**
     * POST  /funds-audits : Create a new fundsAudit.
     *
     * @param fundsAudit the fundsAudit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fundsAudit, or with status 400 (Bad Request) if the fundsAudit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/funds-audits",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FundsAudit> createFundsAudit(@RequestBody FundsAudit fundsAudit) throws URISyntaxException {
        log.debug("REST request to save FundsAudit : {}", fundsAudit);
        if (fundsAudit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fundsAudit", "idexists", "A new fundsAudit cannot already have an ID")).body(null);
        }
        FundsAudit result = fundsAuditRepository.save(fundsAudit);
        return ResponseEntity.created(new URI("/api/funds-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fundsAudit", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /funds-audits : Updates an existing fundsAudit.
     *
     * @param fundsAudit the fundsAudit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fundsAudit,
     * or with status 400 (Bad Request) if the fundsAudit is not valid,
     * or with status 500 (Internal Server Error) if the fundsAudit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/funds-audits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FundsAudit> updateFundsAudit(@RequestBody FundsAudit fundsAudit) throws URISyntaxException {
        log.debug("REST request to update FundsAudit : {}", fundsAudit);
        if (fundsAudit.getId() == null) {
            return createFundsAudit(fundsAudit);
        }
        FundsAudit result = fundsAuditRepository.save(fundsAudit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fundsAudit", fundsAudit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /funds-audits : get all the fundsAudits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fundsAudits in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/funds-audits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FundsAudit>> getAllFundsAudits(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FundsAudits");
        Page<FundsAudit> page = fundsAuditRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/funds-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /funds-audits/:id : get the "id" fundsAudit.
     *
     * @param id the id of the fundsAudit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fundsAudit, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/funds-audits/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FundsAudit> getFundsAudit(@PathVariable Long id) {
        log.debug("REST request to get FundsAudit : {}", id);
        FundsAudit fundsAudit = fundsAuditRepository.findOne(id);
        return Optional.ofNullable(fundsAudit)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /funds-audits/:id : delete the "id" fundsAudit.
     *
     * @param id the id of the fundsAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/funds-audits/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFundsAudit(@PathVariable Long id) {
        log.debug("REST request to delete FundsAudit : {}", id);
        fundsAuditRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fundsAudit", id.toString())).build();
    }

}
