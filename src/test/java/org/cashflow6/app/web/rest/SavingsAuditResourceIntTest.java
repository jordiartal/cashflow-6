package org.cashflow6.app.web.rest;

import org.cashflow6.app.Cashflow6App;
import org.cashflow6.app.domain.SavingsAudit;
import org.cashflow6.app.repository.SavingsAuditRepository;

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
 * Test class for the SavingsAuditResource REST controller.
 *
 * @see SavingsAuditResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Cashflow6App.class)
@WebAppConfiguration
@IntegrationTest
public class SavingsAuditResourceIntTest {


    private static final LocalDate DEFAULT_OP_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OP_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_ANT_VALUE = 1D;
    private static final Double UPDATED_ANT_VALUE = 2D;

    private static final Double DEFAULT_NEW_VALUE = 1D;
    private static final Double UPDATED_NEW_VALUE = 2D;

    @Inject
    private SavingsAuditRepository savingsAuditRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSavingsAuditMockMvc;

    private SavingsAudit savingsAudit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SavingsAuditResource savingsAuditResource = new SavingsAuditResource();
        ReflectionTestUtils.setField(savingsAuditResource, "savingsAuditRepository", savingsAuditRepository);
        this.restSavingsAuditMockMvc = MockMvcBuilders.standaloneSetup(savingsAuditResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        savingsAudit = new SavingsAudit();
        savingsAudit.setOpDate(DEFAULT_OP_DATE);
        savingsAudit.setAntValue(DEFAULT_ANT_VALUE);
        savingsAudit.setNewValue(DEFAULT_NEW_VALUE);
    }

    @Test
    @Transactional
    public void createSavingsAudit() throws Exception {
        int databaseSizeBeforeCreate = savingsAuditRepository.findAll().size();

        // Create the SavingsAudit

        restSavingsAuditMockMvc.perform(post("/api/savings-audits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(savingsAudit)))
                .andExpect(status().isCreated());

        // Validate the SavingsAudit in the database
        List<SavingsAudit> savingsAudits = savingsAuditRepository.findAll();
        assertThat(savingsAudits).hasSize(databaseSizeBeforeCreate + 1);
        SavingsAudit testSavingsAudit = savingsAudits.get(savingsAudits.size() - 1);
        assertThat(testSavingsAudit.getOpDate()).isEqualTo(DEFAULT_OP_DATE);
        assertThat(testSavingsAudit.getAntValue()).isEqualTo(DEFAULT_ANT_VALUE);
        assertThat(testSavingsAudit.getNewValue()).isEqualTo(DEFAULT_NEW_VALUE);
    }

    @Test
    @Transactional
    public void getAllSavingsAudits() throws Exception {
        // Initialize the database
        savingsAuditRepository.saveAndFlush(savingsAudit);

        // Get all the savingsAudits
        restSavingsAuditMockMvc.perform(get("/api/savings-audits?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(savingsAudit.getId().intValue())))
                .andExpect(jsonPath("$.[*].opDate").value(hasItem(DEFAULT_OP_DATE.toString())))
                .andExpect(jsonPath("$.[*].antValue").value(hasItem(DEFAULT_ANT_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    public void getSavingsAudit() throws Exception {
        // Initialize the database
        savingsAuditRepository.saveAndFlush(savingsAudit);

        // Get the savingsAudit
        restSavingsAuditMockMvc.perform(get("/api/savings-audits/{id}", savingsAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(savingsAudit.getId().intValue()))
            .andExpect(jsonPath("$.opDate").value(DEFAULT_OP_DATE.toString()))
            .andExpect(jsonPath("$.antValue").value(DEFAULT_ANT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSavingsAudit() throws Exception {
        // Get the savingsAudit
        restSavingsAuditMockMvc.perform(get("/api/savings-audits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSavingsAudit() throws Exception {
        // Initialize the database
        savingsAuditRepository.saveAndFlush(savingsAudit);
        int databaseSizeBeforeUpdate = savingsAuditRepository.findAll().size();

        // Update the savingsAudit
        SavingsAudit updatedSavingsAudit = new SavingsAudit();
        updatedSavingsAudit.setId(savingsAudit.getId());
        updatedSavingsAudit.setOpDate(UPDATED_OP_DATE);
        updatedSavingsAudit.setAntValue(UPDATED_ANT_VALUE);
        updatedSavingsAudit.setNewValue(UPDATED_NEW_VALUE);

        restSavingsAuditMockMvc.perform(put("/api/savings-audits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSavingsAudit)))
                .andExpect(status().isOk());

        // Validate the SavingsAudit in the database
        List<SavingsAudit> savingsAudits = savingsAuditRepository.findAll();
        assertThat(savingsAudits).hasSize(databaseSizeBeforeUpdate);
        SavingsAudit testSavingsAudit = savingsAudits.get(savingsAudits.size() - 1);
        assertThat(testSavingsAudit.getOpDate()).isEqualTo(UPDATED_OP_DATE);
        assertThat(testSavingsAudit.getAntValue()).isEqualTo(UPDATED_ANT_VALUE);
        assertThat(testSavingsAudit.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    public void deleteSavingsAudit() throws Exception {
        // Initialize the database
        savingsAuditRepository.saveAndFlush(savingsAudit);
        int databaseSizeBeforeDelete = savingsAuditRepository.findAll().size();

        // Get the savingsAudit
        restSavingsAuditMockMvc.perform(delete("/api/savings-audits/{id}", savingsAudit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SavingsAudit> savingsAudits = savingsAuditRepository.findAll();
        assertThat(savingsAudits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
