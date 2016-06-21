package org.cashflow6.app.web.rest;

import org.cashflow6.app.Cashflow6App;
import org.cashflow6.app.domain.Stock;
import org.cashflow6.app.repository.StockRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StockResource REST controller.
 *
 * @see StockResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Cashflow6App.class)
@WebAppConfiguration
@IntegrationTest
public class StockResourceIntTest {

    private static final String DEFAULT_TICKER = "AAAAA";
    private static final String UPDATED_TICKER = "BBBBB";
    private static final String DEFAULT_COMPANY_NAME = "AAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBB";

    private static final Integer DEFAULT_ACTUAL_SHARES = 1;
    private static final Integer UPDATED_ACTUAL_SHARES = 2;

    private static final Integer DEFAULT_NEW_SHARES = 1;
    private static final Integer UPDATED_NEW_SHARES = 2;

    private static final LocalDate DEFAULT_INITIAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INITIAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_ACTUAL_VALUE = 1D;
    private static final Double UPDATED_ACTUAL_VALUE = 2D;

    private static final Double DEFAULT_INITIAL_VALUE = 1D;
    private static final Double UPDATED_INITIAL_VALUE = 2D;

    private static final Double DEFAULT_NEW_VALUE = 1D;
    private static final Double UPDATED_NEW_VALUE = 2D;

    private static final Integer DEFAULT_INITIAL_SHARES = 1;
    private static final Integer UPDATED_INITIAL_SHARES = 2;

    @Inject
    private StockRepository stockRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockMockMvc;

    private Stock stock;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockResource stockResource = new StockResource();
        ReflectionTestUtils.setField(stockResource, "stockRepository", stockRepository);
        this.restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stock = new Stock();
        stock.setTicker(DEFAULT_TICKER);
        stock.setCompanyName(DEFAULT_COMPANY_NAME);
        stock.setActualShares(DEFAULT_ACTUAL_SHARES);
        stock.setNewShares(DEFAULT_NEW_SHARES);
        stock.setInitialDate(DEFAULT_INITIAL_DATE);
        stock.setActualValue(DEFAULT_ACTUAL_VALUE);
        stock.setInitialValue(DEFAULT_INITIAL_VALUE);
        stock.setNewValue(DEFAULT_NEW_VALUE);
        stock.setInitialShares(DEFAULT_INITIAL_SHARES);
    }

    @Test
    @Transactional
    public void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock

        restStockMockMvc.perform(post("/api/stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stock)))
                .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stocks.get(stocks.size() - 1);
        assertThat(testStock.getTicker()).isEqualTo(DEFAULT_TICKER);
        assertThat(testStock.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testStock.getActualShares()).isEqualTo(DEFAULT_ACTUAL_SHARES);
        assertThat(testStock.getNewShares()).isEqualTo(DEFAULT_NEW_SHARES);
        assertThat(testStock.getInitialDate()).isEqualTo(DEFAULT_INITIAL_DATE);
        assertThat(testStock.getActualValue()).isEqualTo(DEFAULT_ACTUAL_VALUE);
        assertThat(testStock.getInitialValue()).isEqualTo(DEFAULT_INITIAL_VALUE);
        assertThat(testStock.getNewValue()).isEqualTo(DEFAULT_NEW_VALUE);
        assertThat(testStock.getInitialShares()).isEqualTo(DEFAULT_INITIAL_SHARES);
    }

    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stocks
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
                .andExpect(jsonPath("$.[*].ticker").value(hasItem(DEFAULT_TICKER.toString())))
                .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
                .andExpect(jsonPath("$.[*].actualShares").value(hasItem(DEFAULT_ACTUAL_SHARES)))
                .andExpect(jsonPath("$.[*].newShares").value(hasItem(DEFAULT_NEW_SHARES)))
                .andExpect(jsonPath("$.[*].initialDate").value(hasItem(DEFAULT_INITIAL_DATE.toString())))
                .andExpect(jsonPath("$.[*].actualValue").value(hasItem(DEFAULT_ACTUAL_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].initialValue").value(hasItem(DEFAULT_INITIAL_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].initialShares").value(hasItem(DEFAULT_INITIAL_SHARES)));
    }

    @Test
    @Transactional
    public void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.ticker").value(DEFAULT_TICKER.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.actualShares").value(DEFAULT_ACTUAL_SHARES))
            .andExpect(jsonPath("$.newShares").value(DEFAULT_NEW_SHARES))
            .andExpect(jsonPath("$.initialDate").value(DEFAULT_INITIAL_DATE.toString()))
            .andExpect(jsonPath("$.actualValue").value(DEFAULT_ACTUAL_VALUE.doubleValue()))
            .andExpect(jsonPath("$.initialValue").value(DEFAULT_INITIAL_VALUE.doubleValue()))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE.doubleValue()))
            .andExpect(jsonPath("$.initialShares").value(DEFAULT_INITIAL_SHARES));
    }

    @Test
    @Transactional
    public void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = new Stock();
        updatedStock.setId(stock.getId());
        updatedStock.setTicker(UPDATED_TICKER);
        updatedStock.setCompanyName(UPDATED_COMPANY_NAME);
        updatedStock.setActualShares(UPDATED_ACTUAL_SHARES);
        updatedStock.setNewShares(UPDATED_NEW_SHARES);
        updatedStock.setInitialDate(UPDATED_INITIAL_DATE);
        updatedStock.setActualValue(UPDATED_ACTUAL_VALUE);
        updatedStock.setInitialValue(UPDATED_INITIAL_VALUE);
        updatedStock.setNewValue(UPDATED_NEW_VALUE);
        updatedStock.setInitialShares(UPDATED_INITIAL_SHARES);

        restStockMockMvc.perform(put("/api/stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStock)))
                .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stocks.get(stocks.size() - 1);
        assertThat(testStock.getTicker()).isEqualTo(UPDATED_TICKER);
        assertThat(testStock.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testStock.getActualShares()).isEqualTo(UPDATED_ACTUAL_SHARES);
        assertThat(testStock.getNewShares()).isEqualTo(UPDATED_NEW_SHARES);
        assertThat(testStock.getInitialDate()).isEqualTo(UPDATED_INITIAL_DATE);
        assertThat(testStock.getActualValue()).isEqualTo(UPDATED_ACTUAL_VALUE);
        assertThat(testStock.getInitialValue()).isEqualTo(UPDATED_INITIAL_VALUE);
        assertThat(testStock.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);
        assertThat(testStock.getInitialShares()).isEqualTo(UPDATED_INITIAL_SHARES);
    }

    @Test
    @Transactional
    public void deleteStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);
        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Get the stock
        restStockMockMvc.perform(delete("/api/stocks/{id}", stock.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
