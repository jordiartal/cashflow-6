package org.cashflow6.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.cashflow6.app.domain.Savings;
import org.cashflow6.app.domain.SavingsAudit;
import org.cashflow6.app.repository.SavingsAuditRepository;
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
 * REST controller for managing SavingsAudit.
 */
@RestController
@RequestMapping("/api")
public class SavingsAuditResource {

    private final Logger log = LoggerFactory.getLogger(SavingsAuditResource.class);

    @Inject
    private SavingsAuditRepository savingsAuditRepository;

    /**
     * POST  /savings-audits : Create a new savingsAudit.
     *
     * @param savingsAudit the savingsAudit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new savingsAudit, or with status 400 (Bad Request) if the savingsAudit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/savings-audits",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SavingsAudit> createSavingsAudit(@RequestBody SavingsAudit savingsAudit) throws URISyntaxException {
        log.debug("REST request to save SavingsAudit : {}", savingsAudit);
        if (savingsAudit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("savingsAudit", "idexists", "A new savingsAudit cannot already have an ID")).body(null);
        }
        SavingsAudit result = savingsAuditRepository.save(savingsAudit);
        return ResponseEntity.created(new URI("/api/savings-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("savingsAudit", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /savings-audits : Updates an existing savingsAudit.
     *
     * @param savingsAudit the savingsAudit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated savingsAudit,
     * or with status 400 (Bad Request) if the savingsAudit is not valid,
     * or with status 500 (Internal Server Error) if the savingsAudit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/savings-audits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SavingsAudit> updateSavingsAudit(@RequestBody SavingsAudit savingsAudit) throws URISyntaxException {
        log.debug("REST request to update SavingsAudit : {}", savingsAudit);
        if (savingsAudit.getId() == null) {
            return createSavingsAudit(savingsAudit);
        }
        SavingsAudit result = savingsAuditRepository.save(savingsAudit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("savingsAudit", savingsAudit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /savings-audits : get all the savingsAudits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of savingsAudits in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/savings-audits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SavingsAudit>> getAllSavingsAudits(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SavingsAudits");
        Page<SavingsAudit> page = savingsAuditRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/savings-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /savings-audits/:id : get the "id" savingsAudit.
     *
     * @param id the id of the savingsAudit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the savingsAudit, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/savings-audits/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SavingsAudit> getSavingsAudit(@PathVariable Long id) {
        log.debug("REST request to get SavingsAudit : {}", id);
        SavingsAudit savingsAudit = savingsAuditRepository.findOne(id);
        return Optional.ofNullable(savingsAudit)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping ( value = "/savings/findAudit/{id}" ,
        method = RequestMethod. GET ,
        produces = MediaType. APPLICATION_JSON_VALUE )
    @Timed
    public ResponseEntity<List<SavingsAudit>> findSavingsAuditBySavings (@PathVariable Savings id, Pageable pageable)throws URISyntaxException{
        Page<SavingsAudit> page = savingsAuditRepository.findSavingsAuditBySavings(id, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,"api/savingsAudit/findAudit");


        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * DELETE  /savings-audits/:id : delete the "id" savingsAudit.
     *
     * @param id the id of the savingsAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/savings-audits/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSavingsAudit(@PathVariable Long id) {
        log.debug("REST request to delete SavingsAudit : {}", id);
        savingsAuditRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("savingsAudit", id.toString())).build();
    }

}
