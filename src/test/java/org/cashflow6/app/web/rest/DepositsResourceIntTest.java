package org.cashflow6.app.web.rest;

import org.cashflow6.app.Cashflow6App;
import org.cashflow6.app.domain.Deposits;
import org.cashflow6.app.repository.DepositsRepository;

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
 * Test class for the DepositsResource REST controller.
 *
 * @see DepositsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Cashflow6App.class)
@WebAppConfiguration
@IntegrationTest
public class DepositsResourceIntTest {

    private static final String DEFAULT_COMPANY_NAME = "AAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBB";

    private static final Double DEFAULT_INITIAL_VALUE = 1D;
    private static final Double UPDATED_INITIAL_VALUE = 2D;

    private static final Double DEFAULT_FINAL_VALUE = 1D;
    private static final Double UPDATED_FINAL_VALUE = 2D;

    private static final LocalDate DEFAULT_INITIAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INITIAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EXP_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXP_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_INTEREST = 1D;
    private static final Double UPDATED_INTEREST = 2D;

    @Inject
    private DepositsRepository depositsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDepositsMockMvc;

    private Deposits deposits;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DepositsResource depositsResource = new DepositsResource();
        ReflectionTestUtils.setField(depositsResource, "depositsRepository", depositsRepository);
        this.restDepositsMockMvc = MockMvcBuilders.standaloneSetup(depositsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        deposits = new Deposits();
        deposits.setCompanyName(DEFAULT_COMPANY_NAME);
        deposits.setInitialValue(DEFAULT_INITIAL_VALUE);
        deposits.setFinalValue(DEFAULT_FINAL_VALUE);
        deposits.setInitialDate(DEFAULT_INITIAL_DATE);
        deposits.setExpDate(DEFAULT_EXP_DATE);
        deposits.setInterest(DEFAULT_INTEREST);
    }

    @Test
    @Transactional
    public void createDeposits() throws Exception {
        int databaseSizeBeforeCreate = depositsRepository.findAll().size();

        // Create the Deposits

        restDepositsMockMvc.perform(post("/api/deposits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deposits)))
                .andExpect(status().isCreated());

        // Validate the Deposits in the database
        List<Deposits> deposits = depositsRepository.findAll();
        assertThat(deposits).hasSize(databaseSizeBeforeCreate + 1);
        Deposits testDeposits = deposits.get(deposits.size() - 1);
        assertThat(testDeposits.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testDeposits.getInitialValue()).isEqualTo(DEFAULT_INITIAL_VALUE);
        assertThat(testDeposits.getFinalValue()).isEqualTo(DEFAULT_FINAL_VALUE);
        assertThat(testDeposits.getInitialDate()).isEqualTo(DEFAULT_INITIAL_DATE);
        assertThat(testDeposits.getExpDate()).isEqualTo(DEFAULT_EXP_DATE);
        assertThat(testDeposits.getInterest()).isEqualTo(DEFAULT_INTEREST);
    }

    @Test
    @Transactional
    public void getAllDeposits() throws Exception {
        // Initialize the database
        depositsRepository.saveAndFlush(deposits);

        // Get all the deposits
        restDepositsMockMvc.perform(get("/api/deposits?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(deposits.getId().intValue())))
                .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
                .andExpect(jsonPath("$.[*].initialValue").value(hasItem(DEFAULT_INITIAL_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].finalValue").value(hasItem(DEFAULT_FINAL_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].initialDate").value(hasItem(DEFAULT_INITIAL_DATE.toString())))
                .andExpect(jsonPath("$.[*].expDate").value(hasItem(DEFAULT_EXP_DATE.toString())))
                .andExpect(jsonPath("$.[*].interest").value(hasItem(DEFAULT_INTEREST.doubleValue())));
    }

    @Test
    @Transactional
    public void getDeposits() throws Exception {
        // Initialize the database
        depositsRepository.saveAndFlush(deposits);

        // Get the deposits
        restDepositsMockMvc.perform(get("/api/deposits/{id}", deposits.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(deposits.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.initialValue").value(DEFAULT_INITIAL_VALUE.doubleValue()))
            .andExpect(jsonPath("$.finalValue").value(DEFAULT_FINAL_VALUE.doubleValue()))
            .andExpect(jsonPath("$.initialDate").value(DEFAULT_INITIAL_DATE.toString()))
            .andExpect(jsonPath("$.expDate").value(DEFAULT_EXP_DATE.toString()))
            .andExpect(jsonPath("$.interest").value(DEFAULT_INTEREST.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDeposits() throws Exception {
        // Get the deposits
        restDepositsMockMvc.perform(get("/api/deposits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeposits() throws Exception {
        // Initialize the database
        depositsRepository.saveAndFlush(deposits);
        int databaseSizeBeforeUpdate = depositsRepository.findAll().size();

        // Update the deposits
        Deposits updatedDeposits = new Deposits();
        updatedDeposits.setId(deposits.getId());
        updatedDeposits.setCompanyName(UPDATED_COMPANY_NAME);
        updatedDeposits.setInitialValue(UPDATED_INITIAL_VALUE);
        updatedDeposits.setFinalValue(UPDATED_FINAL_VALUE);
        updatedDeposits.setInitialDate(UPDATED_INITIAL_DATE);
        updatedDeposits.setExpDate(UPDATED_EXP_DATE);
        updatedDeposits.setInterest(UPDATED_INTEREST);

        restDepositsMockMvc.perform(put("/api/deposits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDeposits)))
                .andExpect(status().isOk());

        // Validate the Deposits in the database
        List<Deposits> deposits = depositsRepository.findAll();
        assertThat(deposits).hasSize(databaseSizeBeforeUpdate);
        Deposits testDeposits = deposits.get(deposits.size() - 1);
        assertThat(testDeposits.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testDeposits.getInitialValue()).isEqualTo(UPDATED_INITIAL_VALUE);
        assertThat(testDeposits.getFinalValue()).isEqualTo(UPDATED_FINAL_VALUE);
        assertThat(testDeposits.getInitialDate()).isEqualTo(UPDATED_INITIAL_DATE);
        assertThat(testDeposits.getExpDate()).isEqualTo(UPDATED_EXP_DATE);
        assertThat(testDeposits.getInterest()).isEqualTo(UPDATED_INTEREST);
    }

    @Test
    @Transactional
    public void deleteDeposits() throws Exception {
        // Initialize the database
        depositsRepository.saveAndFlush(deposits);
        int databaseSizeBeforeDelete = depositsRepository.findAll().size();

        // Get the deposits
        restDepositsMockMvc.perform(delete("/api/deposits/{id}", deposits.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Deposits> deposits = depositsRepository.findAll();
        assertThat(deposits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
