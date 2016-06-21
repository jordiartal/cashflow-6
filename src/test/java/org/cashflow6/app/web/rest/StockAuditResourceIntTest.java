package org.cashflow6.app.web.rest;

import org.cashflow6.app.Cashflow6App;
import org.cashflow6.app.domain.StockAudit;
import org.cashflow6.app.repository.StockAuditRepository;

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
 * Test class for the StockAuditResource REST controller.
 *
 * @see StockAuditResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Cashflow6App.class)
@WebAppConfiguration
@IntegrationTest
public class StockAuditResourceIntTest {


    private static final LocalDate DEFAULT_OP_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OP_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_ANT_VALUE = 1D;
    private static final Double UPDATED_ANT_VALUE = 2D;

    private static final Double DEFAULT_NEW_VALUE = 1D;
    private static final Double UPDATED_NEW_VALUE = 2D;

    private static final Integer DEFAULT_ANT_SHARES = 1;
    private static final Integer UPDATED_ANT_SHARES = 2;

    private static final Integer DEFAULT_NEW_SHARES = 1;
    private static final Integer UPDATED_NEW_SHARES = 2;

    @Inject
    private StockAuditRepository stockAuditRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockAuditMockMvc;

    private StockAudit stockAudit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockAuditResource stockAuditResource = new StockAuditResource();
        ReflectionTestUtils.setField(stockAuditResource, "stockAuditRepository", stockAuditRepository);
        this.restStockAuditMockMvc = MockMvcBuilders.standaloneSetup(stockAuditResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockAudit = new StockAudit();
        stockAudit.setOpDate(DEFAULT_OP_DATE);
        stockAudit.setAntValue(DEFAULT_ANT_VALUE);
        stockAudit.setNewValue(DEFAULT_NEW_VALUE);
        stockAudit.setAntShares(DEFAULT_ANT_SHARES);
        stockAudit.setNewShares(DEFAULT_NEW_SHARES);
    }

    @Test
    @Transactional
    public void createStockAudit() throws Exception {
        int databaseSizeBeforeCreate = stockAuditRepository.findAll().size();

        // Create the StockAudit

        restStockAuditMockMvc.perform(post("/api/stock-audits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockAudit)))
                .andExpect(status().isCreated());

        // Validate the StockAudit in the database
        List<StockAudit> stockAudits = stockAuditRepository.findAll();
        assertThat(stockAudits).hasSize(databaseSizeBeforeCreate + 1);
        StockAudit testStockAudit = stockAudits.get(stockAudits.size() - 1);
        assertThat(testStockAudit.getOpDate()).isEqualTo(DEFAULT_OP_DATE);
        assertThat(testStockAudit.getAntValue()).isEqualTo(DEFAULT_ANT_VALUE);
        assertThat(testStockAudit.getNewValue()).isEqualTo(DEFAULT_NEW_VALUE);
        assertThat(testStockAudit.getAntShares()).isEqualTo(DEFAULT_ANT_SHARES);
        assertThat(testStockAudit.getNewShares()).isEqualTo(DEFAULT_NEW_SHARES);
    }

    @Test
    @Transactional
    public void getAllStockAudits() throws Exception {
        // Initialize the database
        stockAuditRepository.saveAndFlush(stockAudit);

        // Get all the stockAudits
        restStockAuditMockMvc.perform(get("/api/stock-audits?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockAudit.getId().intValue())))
                .andExpect(jsonPath("$.[*].opDate").value(hasItem(DEFAULT_OP_DATE.toString())))
                .andExpect(jsonPath("$.[*].antValue").value(hasItem(DEFAULT_ANT_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].antShares").value(hasItem(DEFAULT_ANT_SHARES)))
                .andExpect(jsonPath("$.[*].newShares").value(hasItem(DEFAULT_NEW_SHARES)));
    }

    @Test
    @Transactional
    public void getStockAudit() throws Exception {
        // Initialize the database
        stockAuditRepository.saveAndFlush(stockAudit);

        // Get the stockAudit
        restStockAuditMockMvc.perform(get("/api/stock-audits/{id}", stockAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockAudit.getId().intValue()))
            .andExpect(jsonPath("$.opDate").value(DEFAULT_OP_DATE.toString()))
            .andExpect(jsonPath("$.antValue").value(DEFAULT_ANT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE.doubleValue()))
            .andExpect(jsonPath("$.antShares").value(DEFAULT_ANT_SHARES))
            .andExpect(jsonPath("$.newShares").value(DEFAULT_NEW_SHARES));
    }

    @Test
    @Transactional
    public void getNonExistingStockAudit() throws Exception {
        // Get the stockAudit
        restStockAuditMockMvc.perform(get("/api/stock-audits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockAudit() throws Exception {
        // Initialize the database
        stockAuditRepository.saveAndFlush(stockAudit);
        int databaseSizeBeforeUpdate = stockAuditRepository.findAll().size();

        // Update the stockAudit
        StockAudit updatedStockAudit = new StockAudit();
        updatedStockAudit.setId(stockAudit.getId());
        updatedStockAudit.setOpDate(UPDATED_OP_DATE);
        updatedStockAudit.setAntValue(UPDATED_ANT_VALUE);
        updatedStockAudit.setNewValue(UPDATED_NEW_VALUE);
        updatedStockAudit.setAntShares(UPDATED_ANT_SHARES);
        updatedStockAudit.setNewShares(UPDATED_NEW_SHARES);

        restStockAuditMockMvc.perform(put("/api/stock-audits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStockAudit)))
                .andExpect(status().isOk());

        // Validate the StockAudit in the database
        List<StockAudit> stockAudits = stockAuditRepository.findAll();
        assertThat(stockAudits).hasSize(databaseSizeBeforeUpdate);
        StockAudit testStockAudit = stockAudits.get(stockAudits.size() - 1);
        assertThat(testStockAudit.getOpDate()).isEqualTo(UPDATED_OP_DATE);
        assertThat(testStockAudit.getAntValue()).isEqualTo(UPDATED_ANT_VALUE);
        assertThat(testStockAudit.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);
        assertThat(testStockAudit.getAntShares()).isEqualTo(UPDATED_ANT_SHARES);
        assertThat(testStockAudit.getNewShares()).isEqualTo(UPDATED_NEW_SHARES);
    }

    @Test
    @Transactional
    public void deleteStockAudit() throws Exception {
        // Initialize the database
        stockAuditRepository.saveAndFlush(stockAudit);
        int databaseSizeBeforeDelete = stockAuditRepository.findAll().size();

        // Get the stockAudit
        restStockAuditMockMvc.perform(delete("/api/stock-audits/{id}", stockAudit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StockAudit> stockAudits = stockAuditRepository.findAll();
        assertThat(stockAudits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
