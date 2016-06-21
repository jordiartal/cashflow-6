package org.cashflow6.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import javafx.scene.control.Pagination;
import org.cashflow6.app.domain.Savings;
import org.cashflow6.app.domain.SavingsAudit;
import org.cashflow6.app.repository.SavingsRepository;
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
 * REST controller for managing Savings.
 */
@RestController
@RequestMapping("/api")
public class SavingsResource {

    private final Logger log = LoggerFactory.getLogger(SavingsResource.class);

    @Inject
    private SavingsRepository savingsRepository;

    @Inject
    private SavingsAuditResource savingsAuditResource;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /savings : Create a new savings.
     *
     * @param savings the savings to create
     * @return the ResponseEntity with status 201 (Created) and with body the new savings, or with status 400 (Bad Request) if the savings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/savings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Savings> createSavings(@RequestBody Savings savings) throws URISyntaxException {
        log.debug("REST request to save Savings : {}", savings);
        if (savings.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("savings", "idexists", "A new savings cannot already have an ID")).body(null);
        }
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            savings.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        }
        savings.setInitialDate(LocalDate.now());
        savings.setInitialValue(savings.getNewValue());
        savings.setActualValue(savings.getNewValue());
        Savings result = savingsRepository.save(savings);

        SavingsAudit va = new SavingsAudit();
        va.setOpDate(LocalDate.now());
        va.setAntValue(savings.getActualValue());
        va.setNewValue(savings.getNewValue());
        va.setSavings(savings);
        savingsAuditResource.createSavingsAudit(va);

        return ResponseEntity.created(new URI("/api/savings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("savings", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /savings : Updates an existing savings.
     *
     * @param savings the savings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated savings,
     * or with status 400 (Bad Request) if the savings is not valid,
     * or with status 500 (Internal Server Error) if the savings couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/savings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Savings> updateSavings(@RequestBody Savings savings) throws URISyntaxException {
        log.debug("REST request to update Savings : {}", savings);
        if (savings.getId() == null) {
            return createSavings(savings);
        }
        SavingsAudit va = new SavingsAudit();
        va.setOpDate(LocalDate.now());
        va.setAntValue(savings.getActualValue());
        va.setNewValue(savings.getNewValue());
        va.setSavings(savings);
        savingsAuditResource.createSavingsAudit(va);

        savings.setActualValue(savings.getNewValue());
        Savings result = savingsRepository.save(savings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("savings", savings.getId().toString()))
            .body(result);
    }

    /**
     * GET  /savings : get all the savings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of savings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/savings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Savings>> getAllSavings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Savings");
        Page<Savings> page = null;
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = savingsRepository.findByUserIsCurrentUser(pageable);
        } else {
            page = savingsRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/savings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /savings/:id : get the "id" savings.
     *
     * @param id the id of the savings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the savings, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/savings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Savings> getSavings(@PathVariable Long id) {
        log.debug("REST request to get Savings : {}", id);
        Savings savings = savingsRepository.findOne(id);
        return Optional.ofNullable(savings)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    /**
     * DELETE  /savings/:id : delete the "id" savings.
     *
     * @param id the id of the savings to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/savings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSavings(@PathVariable Long id) {
        log.debug("REST request to delete Savings : {}", id);
        savingsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("savings", id.toString())).build();
    }

}
