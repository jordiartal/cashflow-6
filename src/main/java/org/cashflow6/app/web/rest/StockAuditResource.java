package org.cashflow6.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.cashflow6.app.domain.Savings;
import org.cashflow6.app.domain.SavingsAudit;
import org.cashflow6.app.domain.Stock;
import org.cashflow6.app.domain.StockAudit;
import org.cashflow6.app.repository.SavingsAuditRepository;
import org.cashflow6.app.repository.StockAuditRepository;
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
 * REST controller for managing StockAudit.
 */
@RestController
@RequestMapping("/api")
public class StockAuditResource {

    private final Logger log = LoggerFactory.getLogger(StockAuditResource.class);

    @Inject
    private StockAuditRepository stockAuditRepository;

    /**
     * POST  /stock-audits : Create a new stockAudit.
     *
     * @param stockAudit the stockAudit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockAudit, or with status 400 (Bad Request) if the stockAudit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stock-audits",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockAudit> createStockAudit(@RequestBody StockAudit stockAudit) throws URISyntaxException {
        log.debug("REST request to save StockAudit : {}", stockAudit);
        if (stockAudit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stockAudit", "idexists", "A new stockAudit cannot already have an ID")).body(null);
        }
        StockAudit result = stockAuditRepository.save(stockAudit);
        return ResponseEntity.created(new URI("/api/stock-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stockAudit", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-audits : Updates an existing stockAudit.
     *
     * @param stockAudit the stockAudit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockAudit,
     * or with status 400 (Bad Request) if the stockAudit is not valid,
     * or with status 500 (Internal Server Error) if the stockAudit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stock-audits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockAudit> updateStockAudit(@RequestBody StockAudit stockAudit) throws URISyntaxException {
        log.debug("REST request to update StockAudit : {}", stockAudit);
        if (stockAudit.getId() == null) {
            return createStockAudit(stockAudit);
        }
        StockAudit result = stockAuditRepository.save(stockAudit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stockAudit", stockAudit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-audits : get all the stockAudits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockAudits in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/stock-audits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<StockAudit>> getAllStockAudits(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockAudits");
        Page<StockAudit> page = stockAuditRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-audits/:id : get the "id" stockAudit.
     *
     * @param id the id of the stockAudit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockAudit, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/stock-audits/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockAudit> getStockAudit(@PathVariable Long id) {
        log.debug("REST request to get StockAudit : {}", id);
        StockAudit stockAudit = stockAuditRepository.findOne(id);
        return Optional.ofNullable(stockAudit)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping ( value = "/stock/findAudit/{id}" ,
        method = RequestMethod. GET ,
        produces = MediaType. APPLICATION_JSON_VALUE )
    @Timed
    public ResponseEntity<List<StockAudit>> findStockAuditByStock (@PathVariable Stock id, Pageable pageable)throws URISyntaxException{
        Page<StockAudit> page = stockAuditRepository.findStockAuditByStock(id, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,"api/stockAudit/findAudit");


        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * DELETE  /stock-audits/:id : delete the "id" stockAudit.
     *
     * @param id the id of the stockAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/stock-audits/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockAudit(@PathVariable Long id) {
        log.debug("REST request to delete StockAudit : {}", id);
        stockAuditRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockAudit", id.toString())).build();
    }

}
