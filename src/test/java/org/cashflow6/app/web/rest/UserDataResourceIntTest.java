package org.cashflow6.app.web.rest;

import org.cashflow6.app.Cashflow6App;
import org.cashflow6.app.domain.UserData;
import org.cashflow6.app.repository.UserDataRepository;

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
 * Test class for the UserDataResource REST controller.
 *
 * @see UserDataResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Cashflow6App.class)
@WebAppConfiguration
@IntegrationTest
public class UserDataResourceIntTest {

    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_COUNTRY = "AAAAA";
    private static final String UPDATED_COUNTRY = "BBBBB";

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private UserDataRepository userDataRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserDataMockMvc;

    private UserData userData;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserDataResource userDataResource = new UserDataResource();
        ReflectionTestUtils.setField(userDataResource, "userDataRepository", userDataRepository);
        this.restUserDataMockMvc = MockMvcBuilders.standaloneSetup(userDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userData = new UserData();
        userData.setCity(DEFAULT_CITY);
        userData.setCountry(DEFAULT_COUNTRY);
        userData.setBirthday(DEFAULT_BIRTHDAY);
    }

    @Test
    @Transactional
    public void createUserData() throws Exception {
        int databaseSizeBeforeCreate = userDataRepository.findAll().size();

        // Create the UserData

        restUserDataMockMvc.perform(post("/api/user-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userData)))
                .andExpect(status().isCreated());

        // Validate the UserData in the database
        List<UserData> userData = userDataRepository.findAll();
        assertThat(userData).hasSize(databaseSizeBeforeCreate + 1);
        UserData testUserData = userData.get(userData.size() - 1);
        assertThat(testUserData.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserData.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testUserData.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
    }

    @Test
    @Transactional
    public void checkBirthdayIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDataRepository.findAll().size();
        // set the field null
        userData.setBirthday(null);

        // Create the UserData, which fails.

        restUserDataMockMvc.perform(post("/api/user-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userData)))
                .andExpect(status().isBadRequest());

        List<UserData> userData = userDataRepository.findAll();
        assertThat(userData).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get all the userData
        restUserDataMockMvc.perform(get("/api/user-data?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userData.getId().intValue())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())));
    }

    @Test
    @Transactional
    public void getUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get the userData
        restUserDataMockMvc.perform(get("/api/user-data/{id}", userData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userData.getId().intValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserData() throws Exception {
        // Get the userData
        restUserDataMockMvc.perform(get("/api/user-data/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);
        int databaseSizeBeforeUpdate = userDataRepository.findAll().size();

        // Update the userData
        UserData updatedUserData = new UserData();
        updatedUserData.setId(userData.getId());
        updatedUserData.setCity(UPDATED_CITY);
        updatedUserData.setCountry(UPDATED_COUNTRY);
        updatedUserData.setBirthday(UPDATED_BIRTHDAY);

        restUserDataMockMvc.perform(put("/api/user-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserData)))
                .andExpect(status().isOk());

        // Validate the UserData in the database
        List<UserData> userData = userDataRepository.findAll();
        assertThat(userData).hasSize(databaseSizeBeforeUpdate);
        UserData testUserData = userData.get(userData.size() - 1);
        assertThat(testUserData.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserData.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserData.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void deleteUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);
        int databaseSizeBeforeDelete = userDataRepository.findAll().size();

        // Get the userData
        restUserDataMockMvc.perform(delete("/api/user-data/{id}", userData.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserData> userData = userDataRepository.findAll();
        assertThat(userData).hasSize(databaseSizeBeforeDelete - 1);
    }
}
