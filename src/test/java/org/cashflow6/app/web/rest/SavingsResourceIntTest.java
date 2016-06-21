package org.cashflow6.app.web.rest;

import org.cashflow6.app.Cashflow6App;
import org.cashflow6.app.domain.Savings;
import org.cashflow6.app.repository.SavingsRepository;

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
 * Test class for the SavingsResource REST controller.
 *
 * @see SavingsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Cashflow6App.class)
@WebAppConfiguration
@IntegrationTest
public class SavingsResourceIntTest {

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBB";

    private static final Double DEFAULT_ACTUAL_VALUE = 1D;
    private static final Double UPDATED_ACTUAL_VALUE = 2D;

    private static final Double DEFAULT_INITIAL_VALUE = 1D;
    private static final Double UPDATED_INITIAL_VALUE = 2D;

    private static final LocalDate DEFAULT_INITIAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INITIAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_NEW_VALUE = 1D;
    private static final Double UPDATED_NEW_VALUE = 2D;

    @Inject
    private SavingsRepository savingsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSavingsMockMvc;

    private Savings savings;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SavingsResource savingsResource = new SavingsResource();
        ReflectionTestUtils.setField(savingsResource, "savingsRepository", savingsRepository);
        this.restSavingsMockMvc = MockMvcBuilders.standaloneSetup(savingsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        savings = new Savings();
        savings.setAccountName(DEFAULT_ACCOUNT_NAME);
        savings.setActualValue(DEFAULT_ACTUAL_VALUE);
        savings.setInitialValue(DEFAULT_INITIAL_VALUE);
        savings.setInitialDate(DEFAULT_INITIAL_DATE);
        savings.setNewValue(DEFAULT_NEW_VALUE);
    }

    @Test
    @Transactional
    public void createSavings() throws Exception {
        int databaseSizeBeforeCreate = savingsRepository.findAll().size();

        // Create the Savings

        restSavingsMockMvc.perform(post("/api/savings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(savings)))
                .andExpect(status().isCreated());

        // Validate the Savings in the database
        List<Savings> savings = savingsRepository.findAll();
        assertThat(savings).hasSize(databaseSizeBeforeCreate + 1);
        Savings testSavings = savings.get(savings.size() - 1);
        assertThat(testSavings.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testSavings.getActualValue()).isEqualTo(DEFAULT_ACTUAL_VALUE);
        assertThat(testSavings.getInitialValue()).isEqualTo(DEFAULT_INITIAL_VALUE);
        assertThat(testSavings.getInitialDate()).isEqualTo(DEFAULT_INITIAL_DATE);
        assertThat(testSavings.getNewValue()).isEqualTo(DEFAULT_NEW_VALUE);
    }

    @Test
    @Transactional
    public void getAllSavings() throws Exception {
        // Initialize the database
        savingsRepository.saveAndFlush(savings);

        // Get all the savings
        restSavingsMockMvc.perform(get("/api/savings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(savings.getId().intValue())))
                .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME.toString())))
                .andExpect(jsonPath("$.[*].actualValue").value(hasItem(DEFAULT_ACTUAL_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].initialValue").value(hasItem(DEFAULT_INITIAL_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].initialDate").value(hasItem(DEFAULT_INITIAL_DATE.toString())))
                .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    public void getSavings() throws Exception {
        // Initialize the database
        savingsRepository.saveAndFlush(savings);

        // Get the savings
        restSavingsMockMvc.perform(get("/api/savings/{id}", savings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(savings.getId().intValue()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME.toString()))
            .andExpect(jsonPath("$.actualValue").value(DEFAULT_ACTUAL_VALUE.doubleValue()))
            .andExpect(jsonPath("$.initialValue").value(DEFAULT_INITIAL_VALUE.doubleValue()))
            .andExpect(jsonPath("$.initialDate").value(DEFAULT_INITIAL_DATE.toString()))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSavings() throws Exception {
        // Get the savings
        restSavingsMockMvc.perform(get("/api/savings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSavings() throws Exception {
        // Initialize the database
        savingsRepository.saveAndFlush(savings);
        int databaseSizeBeforeUpdate = savingsRepository.findAll().size();

        // Update the savings
        Savings updatedSavings = new Savings();
        updatedSavings.setId(savings.getId());
        updatedSavings.setAccountName(UPDATED_ACCOUNT_NAME);
        updatedSavings.setActualValue(UPDATED_ACTUAL_VALUE);
        updatedSavings.setInitialValue(UPDATED_INITIAL_VALUE);
        updatedSavings.setInitialDate(UPDATED_INITIAL_DATE);
        updatedSavings.setNewValue(UPDATED_NEW_VALUE);

        restSavingsMockMvc.perform(put("/api/savings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSavings)))
                .andExpect(status().isOk());

        // Validate the Savings in the database
        List<Savings> savings = savingsRepository.findAll();
        assertThat(savings).hasSize(databaseSizeBeforeUpdate);
        Savings testSavings = savings.get(savings.size() - 1);
        assertThat(testSavings.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testSavings.getActualValue()).isEqualTo(UPDATED_ACTUAL_VALUE);
        assertThat(testSavings.getInitialValue()).isEqualTo(UPDATED_INITIAL_VALUE);
        assertThat(testSavings.getInitialDate()).isEqualTo(UPDATED_INITIAL_DATE);
        assertThat(testSavings.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    public void deleteSavings() throws Exception {
        // Initialize the database
        savingsRepository.saveAndFlush(savings);
        int databaseSizeBeforeDelete = savingsRepository.findAll().size();

        // Get the savings
        restSavingsMockMvc.perform(delete("/api/savings/{id}", savings.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Savings> savings = savingsRepository.findAll();
        assertThat(savings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
