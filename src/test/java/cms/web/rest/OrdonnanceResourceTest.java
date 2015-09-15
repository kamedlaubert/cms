package cms.web.rest;

import cms.Application;
import cms.domain.Ordonnance;
import cms.repository.OrdonnanceRepository;
import cms.repository.search.OrdonnanceSearchRepository;

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
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the OrdonnanceResource REST controller.
 *
 * @see OrdonnanceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OrdonnanceResourceTest {


    private static final LocalDate DEFAULT_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE = new LocalDate();
    private static final String DEFAULT_LIGNE = "SAMPLE_TEXT";
    private static final String UPDATED_LIGNE = "UPDATED_TEXT";

    @Inject
    private OrdonnanceRepository ordonnanceRepository;

    @Inject
    private OrdonnanceSearchRepository ordonnanceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restOrdonnanceMockMvc;

    private Ordonnance ordonnance;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrdonnanceResource ordonnanceResource = new OrdonnanceResource();
        ReflectionTestUtils.setField(ordonnanceResource, "ordonnanceRepository", ordonnanceRepository);
        ReflectionTestUtils.setField(ordonnanceResource, "ordonnanceSearchRepository", ordonnanceSearchRepository);
        this.restOrdonnanceMockMvc = MockMvcBuilders.standaloneSetup(ordonnanceResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ordonnance = new Ordonnance();
        ordonnance.setDate(DEFAULT_DATE);
        ordonnance.setLigne(DEFAULT_LIGNE);
    }

    @Test
    @Transactional
    public void createOrdonnance() throws Exception {
        int databaseSizeBeforeCreate = ordonnanceRepository.findAll().size();

        // Create the Ordonnance

        restOrdonnanceMockMvc.perform(post("/api/ordonnances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ordonnance)))
                .andExpect(status().isCreated());

        // Validate the Ordonnance in the database
        List<Ordonnance> ordonnances = ordonnanceRepository.findAll();
        assertThat(ordonnances).hasSize(databaseSizeBeforeCreate + 1);
        Ordonnance testOrdonnance = ordonnances.get(ordonnances.size() - 1);
        assertThat(testOrdonnance.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOrdonnance.getLigne()).isEqualTo(DEFAULT_LIGNE);
    }

    @Test
    @Transactional
    public void getAllOrdonnances() throws Exception {
        // Initialize the database
        ordonnanceRepository.saveAndFlush(ordonnance);

        // Get all the ordonnances
        restOrdonnanceMockMvc.perform(get("/api/ordonnances"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ordonnance.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].ligne").value(hasItem(DEFAULT_LIGNE.toString())));
    }

    @Test
    @Transactional
    public void getOrdonnance() throws Exception {
        // Initialize the database
        ordonnanceRepository.saveAndFlush(ordonnance);

        // Get the ordonnance
        restOrdonnanceMockMvc.perform(get("/api/ordonnances/{id}", ordonnance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ordonnance.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.ligne").value(DEFAULT_LIGNE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrdonnance() throws Exception {
        // Get the ordonnance
        restOrdonnanceMockMvc.perform(get("/api/ordonnances/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrdonnance() throws Exception {
        // Initialize the database
        ordonnanceRepository.saveAndFlush(ordonnance);

		int databaseSizeBeforeUpdate = ordonnanceRepository.findAll().size();

        // Update the ordonnance
        ordonnance.setDate(UPDATED_DATE);
        ordonnance.setLigne(UPDATED_LIGNE);
        

        restOrdonnanceMockMvc.perform(put("/api/ordonnances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ordonnance)))
                .andExpect(status().isOk());

        // Validate the Ordonnance in the database
        List<Ordonnance> ordonnances = ordonnanceRepository.findAll();
        assertThat(ordonnances).hasSize(databaseSizeBeforeUpdate);
        Ordonnance testOrdonnance = ordonnances.get(ordonnances.size() - 1);
        assertThat(testOrdonnance.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOrdonnance.getLigne()).isEqualTo(UPDATED_LIGNE);
    }

    @Test
    @Transactional
    public void deleteOrdonnance() throws Exception {
        // Initialize the database
        ordonnanceRepository.saveAndFlush(ordonnance);

		int databaseSizeBeforeDelete = ordonnanceRepository.findAll().size();

        // Get the ordonnance
        restOrdonnanceMockMvc.perform(delete("/api/ordonnances/{id}", ordonnance.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ordonnance> ordonnances = ordonnanceRepository.findAll();
        assertThat(ordonnances).hasSize(databaseSizeBeforeDelete - 1);
    }
}
