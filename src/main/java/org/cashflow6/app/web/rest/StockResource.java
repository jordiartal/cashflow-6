package org.cashflow6.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.cashflow6.app.domain.Stock;
import org.cashflow6.app.domain.StockAudit;
import org.cashflow6.app.repository.StockRepository;
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
 * REST controller for managing Stock.
 */
@RestController
@RequestMapping("/api")
public class StockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    @Inject
    private StockRepository stockRepository;

    @Inject
    private StockAuditResource stockAuditResource;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /stocks : Create a new stock.
     *
     * @param stock the stock to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stock, or with status 400 (Bad Request) if the stock has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stocks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stock);
        if (stock.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stock", "idexists", "A new stock cannot already have an ID")).body(null);
        }
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            stock.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        }
        stock.setInitialDate(LocalDate.now());
        stock.setInitialValue(stock.getNewValue());
        stock.setActualValue(stock.getNewValue());
        stock.setInitialShares(stock.getNewShares());
        stock.setActualShares(stock.getNewShares());
        Stock result = stockRepository.save(stock);

        StockAudit va = new StockAudit();
        va.setOpDate(LocalDate.now());
        va.setAntValue(stock.getActualValue());
        va.setNewValue(stock.getNewValue());
        va.setAntShares(stock.getActualShares());
        va.setNewShares(stock.getNewShares());
        va.setStock(stock);
        stockAuditResource.createStockAudit(va);

        return ResponseEntity.created(new URI("/api/stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stock", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stocks : Updates an existing stock.
     *
     * @param stock the stock to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stock,
     * or with status 400 (Bad Request) if the stock is not valid,
     * or with status 500 (Internal Server Error) if the stock couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stocks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stock> updateStock(@RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to update Stock : {}", stock);
        if (stock.getId() == null) {
            return createStock(stock);
        }
        StockAudit va = new StockAudit();
        va.setOpDate(LocalDate.now());
        va.setAntValue(stock.getActualValue());
        va.setNewValue(stock.getNewValue());
        va.setAntShares(stock.getActualShares());
        va.setNewShares(stock.getNewShares());
        va.setStock(stock);
        stockAuditResource.createStockAudit(va);

        stock.setActualShares(stock.getNewShares());
        stock.setActualValue(stock.getNewValue());
        Stock result = stockRepository.save(stock);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stock", stock.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stocks : get all the stocks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stocks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/stocks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Stock>> getAllStocks(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Stocks");
        Page<Stock> page = null;
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = stockRepository.findByUserIsCurrentUser(pageable);
        } else {
            page = stockRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stocks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stocks/:id : get the "id" stock.
     *
     * @param id the id of the stock to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stock, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/stocks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stock> getStock(@PathVariable Long id) {
        log.debug("REST request to get Stock : {}", id);
        Stock stock = stockRepository.findOne(id);
        return Optional.ofNullable(stock)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stocks/:id : delete the "id" stock.
     *
     * @param id the id of the stock to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/stocks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        log.debug("REST request to delete Stock : {}", id);
        stockRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stock", id.toString())).build();
    }

}
