package org.cashflow6.app.web.rest;

import org.cashflow6.app.Cashflow6App;
import org.cashflow6.app.domain.Currency;
import org.cashflow6.app.repository.CurrencyRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CurrencyResource REST controller.
 *
 * @see CurrencyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Cashflow6App.class)
@WebAppConfiguration
@IntegrationTest
public class CurrencyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final Double DEFAULT_CONVER = 1D;
    private static final Double UPDATED_CONVER = 2D;

    @Inject
    private CurrencyRepository currencyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCurrencyMockMvc;

    private Currency currency;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CurrencyResource currencyResource = new CurrencyResource();
        ReflectionTestUtils.setField(currencyResource, "currencyRepository", currencyRepository);
        this.restCurrencyMockMvc = MockMvcBuilders.standaloneSetup(currencyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        currency = new Currency();
        currency.setName(DEFAULT_NAME);
        currency.setCode(DEFAULT_CODE);
        currency.setConver(DEFAULT_CONVER);
    }

    @Test
    @Transactional
    public void createCurrency() throws Exception {
        int databaseSizeBeforeCreate = currencyRepository.findAll().size();

        // Create the Currency

        restCurrencyMockMvc.perform(post("/api/currencies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(currency)))
                .andExpect(status().isCreated());

        // Validate the Currency in the database
        List<Currency> currencies = currencyRepository.findAll();
        assertThat(currencies).hasSize(databaseSizeBeforeCreate + 1);
        Currency testCurrency = currencies.get(currencies.size() - 1);
        assertThat(testCurrency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCurrency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCurrency.getConver()).isEqualTo(DEFAULT_CONVER);
    }

    @Test
    @Transactional
    public void getAllCurrencies() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencies
        restCurrencyMockMvc.perform(get("/api/currencies?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].conver").value(hasItem(DEFAULT_CONVER.doubleValue())));
    }

    @Test
    @Transactional
    public void getCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get the currency
        restCurrencyMockMvc.perform(get("/api/currencies/{id}", currency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(currency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.conver").value(DEFAULT_CONVER.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCurrency() throws Exception {
        // Get the currency
        restCurrencyMockMvc.perform(get("/api/currencies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency
        Currency updatedCurrency = new Currency();
        updatedCurrency.setId(currency.getId());
        updatedCurrency.setName(UPDATED_NAME);
        updatedCurrency.setCode(UPDATED_CODE);
        updatedCurrency.setConver(UPDATED_CONVER);

        restCurrencyMockMvc.perform(put("/api/currencies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCurrency)))
                .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencies = currencyRepository.findAll();
        assertThat(currencies).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencies.get(currencies.size() - 1);
        assertThat(testCurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCurrency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCurrency.getConver()).isEqualTo(UPDATED_CONVER);
    }

    @Test
    @Transactional
    public void deleteCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);
        int databaseSizeBeforeDelete = currencyRepository.findAll().size();

        // Get the currency
        restCurrencyMockMvc.perform(delete("/api/currencies/{id}", currency.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Currency> currencies = currencyRepository.findAll();
        assertThat(currencies).hasSize(databaseSizeBeforeDelete - 1);
    }
}
