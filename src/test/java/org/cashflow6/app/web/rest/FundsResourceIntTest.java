package org.cashflow6.app.web.rest;

import org.cashflow6.app.Cashflow6App;
import org.cashflow6.app.domain.Funds;
import org.cashflow6.app.repository.FundsRepository;

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
 * Test class for the FundsResource REST controller.
 *
 * @see FundsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Cashflow6App.class)
@WebAppConfiguration
@IntegrationTest
public class FundsResourceIntTest {

    private static final String DEFAULT_ISIN = "AAAAA";
    private static final String UPDATED_ISIN = "BBBBB";
    private static final String DEFAULT_NAME_FUND = "AAAAA";
    private static final String UPDATED_NAME_FUND = "BBBBB";
    private static final String DEFAULT_COMPANY_NAME = "AAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBB";

    private static final Integer DEFAULT_ACTUAL_SHARES = 1;
    private static final Integer UPDATED_ACTUAL_SHARES = 2;

    private static final LocalDate DEFAULT_INITIAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INITIAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NEW_SHARES = 1;
    private static final Integer UPDATED_NEW_SHARES = 2;

    private static final Double DEFAULT_ACTUAL_VALUE = 1D;
    private static final Double UPDATED_ACTUAL_VALUE = 2D;

    private static final Double DEFAULT_INITIAL_VALUE = 1D;
    private static final Double UPDATED_INITIAL_VALUE = 2D;

    private static final Double DEFAULT_NEW_VALUE = 1D;
    private static final Double UPDATED_NEW_VALUE = 2D;

    private static final Integer DEFAULT_INITIAL_SHARES = 1;
    private static final Integer UPDATED_INITIAL_SHARES = 2;

    @Inject
    private FundsRepository fundsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFundsMockMvc;

    private Funds funds;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FundsResource fundsResource = new FundsResource();
        ReflectionTestUtils.setField(fundsResource, "fundsRepository", fundsRepository);
        this.restFundsMockMvc = MockMvcBuilders.standaloneSetup(fundsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        funds = new Funds();
        funds.setIsin(DEFAULT_ISIN);
        funds.setNameFund(DEFAULT_NAME_FUND);
        funds.setCompanyName(DEFAULT_COMPANY_NAME);
        funds.setActualShares(DEFAULT_ACTUAL_SHARES);
        funds.setInitialDate(DEFAULT_INITIAL_DATE);
        funds.setNewShares(DEFAULT_NEW_SHARES);
        funds.setActualValue(DEFAULT_ACTUAL_VALUE);
        funds.setInitialValue(DEFAULT_INITIAL_VALUE);
        funds.setNewValue(DEFAULT_NEW_VALUE);
        funds.setInitialShares(DEFAULT_INITIAL_SHARES);
    }

    @Test
    @Transactional
    public void createFunds() throws Exception {
        int databaseSizeBeforeCreate = fundsRepository.findAll().size();

        // Create the Funds

        restFundsMockMvc.perform(post("/api/funds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funds)))
                .andExpect(status().isCreated());

        // Validate the Funds in the database
        List<Funds> funds = fundsRepository.findAll();
        assertThat(funds).hasSize(databaseSizeBeforeCreate + 1);
        Funds testFunds = funds.get(funds.size() - 1);
        assertThat(testFunds.getIsin()).isEqualTo(DEFAULT_ISIN);
        assertThat(testFunds.getNameFund()).isEqualTo(DEFAULT_NAME_FUND);
        assertThat(testFunds.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testFunds.getActualShares()).isEqualTo(DEFAULT_ACTUAL_SHARES);
        assertThat(testFunds.getInitialDate()).isEqualTo(DEFAULT_INITIAL_DATE);
        assertThat(testFunds.getNewShares()).isEqualTo(DEFAULT_NEW_SHARES);
        assertThat(testFunds.getActualValue()).isEqualTo(DEFAULT_ACTUAL_VALUE);
        assertThat(testFunds.getInitialValue()).isEqualTo(DEFAULT_INITIAL_VALUE);
        assertThat(testFunds.getNewValue()).isEqualTo(DEFAULT_NEW_VALUE);
        assertThat(testFunds.getInitialShares()).isEqualTo(DEFAULT_INITIAL_SHARES);
    }

    @Test
    @Transactional
    public void getAllFunds() throws Exception {
        // Initialize the database
        fundsRepository.saveAndFlush(funds);

        // Get all the funds
        restFundsMockMvc.perform(get("/api/funds?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(funds.getId().intValue())))
                .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN.toString())))
                .andExpect(jsonPath("$.[*].nameFund").value(hasItem(DEFAULT_NAME_FUND.toString())))
                .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
                .andExpect(jsonPath("$.[*].actualShares").value(hasItem(DEFAULT_ACTUAL_SHARES)))
                .andExpect(jsonPath("$.[*].initialDate").value(hasItem(DEFAULT_INITIAL_DATE.toString())))
                .andExpect(jsonPath("$.[*].newShares").value(hasItem(DEFAULT_NEW_SHARES)))
                .andExpect(jsonPath("$.[*].actualValue").value(hasItem(DEFAULT_ACTUAL_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].initialValue").value(hasItem(DEFAULT_INITIAL_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].initialShares").value(hasItem(DEFAULT_INITIAL_SHARES)));
    }

    @Test
    @Transactional
    public void getFunds() throws Exception {
        // Initialize the database
        fundsRepository.saveAndFlush(funds);

        // Get the funds
        restFundsMockMvc.perform(get("/api/funds/{id}", funds.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(funds.getId().intValue()))
            .andExpect(jsonPath("$.isin").value(DEFAULT_ISIN.toString()))
            .andExpect(jsonPath("$.nameFund").value(DEFAULT_NAME_FUND.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.actualShares").value(DEFAULT_ACTUAL_SHARES))
            .andExpect(jsonPath("$.initialDate").value(DEFAULT_INITIAL_DATE.toString()))
            .andExpect(jsonPath("$.newShares").value(DEFAULT_NEW_SHARES))
            .andExpect(jsonPath("$.actualValue").value(DEFAULT_ACTUAL_VALUE.doubleValue()))
            .andExpect(jsonPath("$.initialValue").value(DEFAULT_INITIAL_VALUE.doubleValue()))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE.doubleValue()))
            .andExpect(jsonPath("$.initialShares").value(DEFAULT_INITIAL_SHARES));
    }

    @Test
    @Transactional
    public void getNonExistingFunds() throws Exception {
        // Get the funds
        restFundsMockMvc.perform(get("/api/funds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFunds() throws Exception {
        // Initialize the database
        fundsRepository.saveAndFlush(funds);
        int databaseSizeBeforeUpdate = fundsRepository.findAll().size();

        // Update the funds
        Funds updatedFunds = new Funds();
        updatedFunds.setId(funds.getId());
        updatedFunds.setIsin(UPDATED_ISIN);
        updatedFunds.setNameFund(UPDATED_NAME_FUND);
        updatedFunds.setCompanyName(UPDATED_COMPANY_NAME);
        updatedFunds.setActualShares(UPDATED_ACTUAL_SHARES);
        updatedFunds.setInitialDate(UPDATED_INITIAL_DATE);
        updatedFunds.setNewShares(UPDATED_NEW_SHARES);
        updatedFunds.setActualValue(UPDATED_ACTUAL_VALUE);
        updatedFunds.setInitialValue(UPDATED_INITIAL_VALUE);
        updatedFunds.setNewValue(UPDATED_NEW_VALUE);
        updatedFunds.setInitialShares(UPDATED_INITIAL_SHARES);

        restFundsMockMvc.perform(put("/api/funds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFunds)))
                .andExpect(status().isOk());

        // Validate the Funds in the database
        List<Funds> funds = fundsRepository.findAll();
        assertThat(funds).hasSize(databaseSizeBeforeUpdate);
        Funds testFunds = funds.get(funds.size() - 1);
        assertThat(testFunds.getIsin()).isEqualTo(UPDATED_ISIN);
        assertThat(testFunds.getNameFund()).isEqualTo(UPDATED_NAME_FUND);
        assertThat(testFunds.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testFunds.getActualShares()).isEqualTo(UPDATED_ACTUAL_SHARES);
        assertThat(testFunds.getInitialDate()).isEqualTo(UPDATED_INITIAL_DATE);
        assertThat(testFunds.getNewShares()).isEqualTo(UPDATED_NEW_SHARES);
        assertThat(testFunds.getActualValue()).isEqualTo(UPDATED_ACTUAL_VALUE);
        assertThat(testFunds.getInitialValue()).isEqualTo(UPDATED_INITIAL_VALUE);
        assertThat(testFunds.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);
        assertThat(testFunds.getInitialShares()).isEqualTo(UPDATED_INITIAL_SHARES);
    }

    @Test
    @Transactional
    public void deleteFunds() throws Exception {
        // Initialize the database
        fundsRepository.saveAndFlush(funds);
        int databaseSizeBeforeDelete = fundsRepository.findAll().size();

        // Get the funds
        restFundsMockMvc.perform(delete("/api/funds/{id}", funds.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Funds> funds = fundsRepository.findAll();
        assertThat(funds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
