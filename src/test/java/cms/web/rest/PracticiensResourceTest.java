package cms.web.rest;

import cms.Application;
import cms.domain.Practiciens;
import cms.repository.PracticiensRepository;
import cms.repository.search.PracticiensSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
 * Test class for the PracticiensResource REST controller.
 *
 * @see PracticiensResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PracticiensResourceTest {

    private static final String DEFAULT_MATRICULE = "SAMPLE_TEXT";
    private static final String UPDATED_MATRICULE = "UPDATED_TEXT";
    private static final String DEFAULT_TEL = "SAMPLE_TEXT";
    private static final String UPDATED_TEL = "UPDATED_TEXT";
    private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL = "UPDATED_TEXT";
    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";

    @Inject
    private PracticiensRepository practiciensRepository;

    @Inject
    private PracticiensSearchRepository practiciensSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPracticiensMockMvc;

    private Practiciens practiciens;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PracticiensResource practiciensResource = new PracticiensResource();
        ReflectionTestUtils.setField(practiciensResource, "practiciensRepository", practiciensRepository);
        ReflectionTestUtils.setField(practiciensResource, "practiciensSearchRepository", practiciensSearchRepository);
        this.restPracticiensMockMvc = MockMvcBuilders.standaloneSetup(practiciensResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        practiciens = new Practiciens();
        practiciens.setMatricule(DEFAULT_MATRICULE);
        practiciens.setTel(DEFAULT_TEL);
        practiciens.setEmail(DEFAULT_EMAIL);
        practiciens.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createPracticiens() throws Exception {
        int databaseSizeBeforeCreate = practiciensRepository.findAll().size();

        // Create the Practiciens

        restPracticiensMockMvc.perform(post("/api/practicienss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(practiciens)))
                .andExpect(status().isCreated());

        // Validate the Practiciens in the database
        List<Practiciens> practicienss = practiciensRepository.findAll();
        assertThat(practicienss).hasSize(databaseSizeBeforeCreate + 1);
        Practiciens testPracticiens = practicienss.get(practicienss.size() - 1);
        assertThat(testPracticiens.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testPracticiens.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testPracticiens.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPracticiens.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllPracticienss() throws Exception {
        // Initialize the database
        practiciensRepository.saveAndFlush(practiciens);

        // Get all the practicienss
        restPracticiensMockMvc.perform(get("/api/practicienss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(practiciens.getId().intValue())))
                .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.toString())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getPracticiens() throws Exception {
        // Initialize the database
        practiciensRepository.saveAndFlush(practiciens);

        // Get the practiciens
        restPracticiensMockMvc.perform(get("/api/practicienss/{id}", practiciens.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(practiciens.getId().intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPracticiens() throws Exception {
        // Get the practiciens
        restPracticiensMockMvc.perform(get("/api/practicienss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePracticiens() throws Exception {
        // Initialize the database
        practiciensRepository.saveAndFlush(practiciens);

		int databaseSizeBeforeUpdate = practiciensRepository.findAll().size();

        // Update the practiciens
        practiciens.setMatricule(UPDATED_MATRICULE);
        practiciens.setTel(UPDATED_TEL);
        practiciens.setEmail(UPDATED_EMAIL);
        practiciens.setType(UPDATED_TYPE);
        

        restPracticiensMockMvc.perform(put("/api/practicienss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(practiciens)))
                .andExpect(status().isOk());

        // Validate the Practiciens in the database
        List<Practiciens> practicienss = practiciensRepository.findAll();
        assertThat(practicienss).hasSize(databaseSizeBeforeUpdate);
        Practiciens testPracticiens = practicienss.get(practicienss.size() - 1);
        assertThat(testPracticiens.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testPracticiens.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testPracticiens.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPracticiens.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deletePracticiens() throws Exception {
        // Initialize the database
        practiciensRepository.saveAndFlush(practiciens);

		int databaseSizeBeforeDelete = practiciensRepository.findAll().size();

        // Get the practiciens
        restPracticiensMockMvc.perform(delete("/api/practicienss/{id}", practiciens.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Practiciens> practicienss = practiciensRepository.findAll();
        assertThat(practicienss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
