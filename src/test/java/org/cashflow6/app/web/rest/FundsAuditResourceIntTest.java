package org.cashflow6.app.web.rest;

import org.cashflow6.app.Cashflow6App;
import org.cashflow6.app.domain.FundsAudit;
import org.cashflow6.app.repository.FundsAuditRepository;

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
 * Test class for the FundsAuditResource REST controller.
 *
 * @see FundsAuditResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Cashflow6App.class)
@WebAppConfiguration
@IntegrationTest
public class FundsAuditResourceIntTest {


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
    private FundsAuditRepository fundsAuditRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFundsAuditMockMvc;

    private FundsAudit fundsAudit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FundsAuditResource fundsAuditResource = new FundsAuditResource();
        ReflectionTestUtils.setField(fundsAuditResource, "fundsAuditRepository", fundsAuditRepository);
        this.restFundsAuditMockMvc = MockMvcBuilders.standaloneSetup(fundsAuditResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fundsAudit = new FundsAudit();
        fundsAudit.setOpDate(DEFAULT_OP_DATE);
        fundsAudit.setAntValue(DEFAULT_ANT_VALUE);
        fundsAudit.setNewValue(DEFAULT_NEW_VALUE);
        fundsAudit.setAntShares(DEFAULT_ANT_SHARES);
        fundsAudit.setNewShares(DEFAULT_NEW_SHARES);
    }

    @Test
    @Transactional
    public void createFundsAudit() throws Exception {
        int databaseSizeBeforeCreate = fundsAuditRepository.findAll().size();

        // Create the FundsAudit

        restFundsAuditMockMvc.perform(post("/api/funds-audits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fundsAudit)))
                .andExpect(status().isCreated());

        // Validate the FundsAudit in the database
        List<FundsAudit> fundsAudits = fundsAuditRepository.findAll();
        assertThat(fundsAudits).hasSize(databaseSizeBeforeCreate + 1);
        FundsAudit testFundsAudit = fundsAudits.get(fundsAudits.size() - 1);
        assertThat(testFundsAudit.getOpDate()).isEqualTo(DEFAULT_OP_DATE);
        assertThat(testFundsAudit.getAntValue()).isEqualTo(DEFAULT_ANT_VALUE);
        assertThat(testFundsAudit.getNewValue()).isEqualTo(DEFAULT_NEW_VALUE);
        assertThat(testFundsAudit.getAntShares()).isEqualTo(DEFAULT_ANT_SHARES);
        assertThat(testFundsAudit.getNewShares()).isEqualTo(DEFAULT_NEW_SHARES);
    }

    @Test
    @Transactional
    public void getAllFundsAudits() throws Exception {
        // Initialize the database
        fundsAuditRepository.saveAndFlush(fundsAudit);

        // Get all the fundsAudits
        restFundsAuditMockMvc.perform(get("/api/funds-audits?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fundsAudit.getId().intValue())))
                .andExpect(jsonPath("$.[*].opDate").value(hasItem(DEFAULT_OP_DATE.toString())))
                .andExpect(jsonPath("$.[*].antValue").value(hasItem(DEFAULT_ANT_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].antShares").value(hasItem(DEFAULT_ANT_SHARES)))
                .andExpect(jsonPath("$.[*].newShares").value(hasItem(DEFAULT_NEW_SHARES)));
    }

    @Test
    @Transactional
    public void getFundsAudit() throws Exception {
        // Initialize the database
        fundsAuditRepository.saveAndFlush(fundsAudit);

        // Get the fundsAudit
        restFundsAuditMockMvc.perform(get("/api/funds-audits/{id}", fundsAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fundsAudit.getId().intValue()))
            .andExpect(jsonPath("$.opDate").value(DEFAULT_OP_DATE.toString()))
            .andExpect(jsonPath("$.antValue").value(DEFAULT_ANT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE.doubleValue()))
            .andExpect(jsonPath("$.antShares").value(DEFAULT_ANT_SHARES))
            .andExpect(jsonPath("$.newShares").value(DEFAULT_NEW_SHARES));
    }

    @Test
    @Transactional
    public void getNonExistingFundsAudit() throws Exception {
        // Get the fundsAudit
        restFundsAuditMockMvc.perform(get("/api/funds-audits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFundsAudit() throws Exception {
        // Initialize the database
        fundsAuditRepository.saveAndFlush(fundsAudit);
        int databaseSizeBeforeUpdate = fundsAuditRepository.findAll().size();

        // Update the fundsAudit
        FundsAudit updatedFundsAudit = new FundsAudit();
        updatedFundsAudit.setId(fundsAudit.getId());
        updatedFundsAudit.setOpDate(UPDATED_OP_DATE);
        updatedFundsAudit.setAntValue(UPDATED_ANT_VALUE);
        updatedFundsAudit.setNewValue(UPDATED_NEW_VALUE);
        updatedFundsAudit.setAntShares(UPDATED_ANT_SHARES);
        updatedFundsAudit.setNewShares(UPDATED_NEW_SHARES);

        restFundsAuditMockMvc.perform(put("/api/funds-audits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFundsAudit)))
                .andExpect(status().isOk());

        // Validate the FundsAudit in the database
        List<FundsAudit> fundsAudits = fundsAuditRepository.findAll();
        assertThat(fundsAudits).hasSize(databaseSizeBeforeUpdate);
        FundsAudit testFundsAudit = fundsAudits.get(fundsAudits.size() - 1);
        assertThat(testFundsAudit.getOpDate()).isEqualTo(UPDATED_OP_DATE);
        assertThat(testFundsAudit.getAntValue()).isEqualTo(UPDATED_ANT_VALUE);
        assertThat(testFundsAudit.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);
        assertThat(testFundsAudit.getAntShares()).isEqualTo(UPDATED_ANT_SHARES);
        assertThat(testFundsAudit.getNewShares()).isEqualTo(UPDATED_NEW_SHARES);
    }

    @Test
    @Transactional
    public void deleteFundsAudit() throws Exception {
        // Initialize the database
        fundsAuditRepository.saveAndFlush(fundsAudit);
        int databaseSizeBeforeDelete = fundsAuditRepository.findAll().size();

        // Get the fundsAudit
        restFundsAuditMockMvc.perform(delete("/api/funds-audits/{id}", fundsAudit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FundsAudit> fundsAudits = fundsAuditRepository.findAll();
        assertThat(fundsAudits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
