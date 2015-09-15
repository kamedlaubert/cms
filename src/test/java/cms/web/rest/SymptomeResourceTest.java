package cms.web.rest;

import cms.Application;
import cms.domain.Symptome;
import cms.repository.SymptomeRepository;
import cms.repository.search.SymptomeSearchRepository;

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
 * Test class for the SymptomeResource REST controller.
 *
 * @see SymptomeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SymptomeResourceTest {

    private static final String DEFAULT_LIBELLE = "SAMPLE_TEXT";
    private static final String UPDATED_LIBELLE = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private SymptomeRepository symptomeRepository;

    @Inject
    private SymptomeSearchRepository symptomeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restSymptomeMockMvc;

    private Symptome symptome;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SymptomeResource symptomeResource = new SymptomeResource();
        ReflectionTestUtils.setField(symptomeResource, "symptomeRepository", symptomeRepository);
        ReflectionTestUtils.setField(symptomeResource, "symptomeSearchRepository", symptomeSearchRepository);
        this.restSymptomeMockMvc = MockMvcBuilders.standaloneSetup(symptomeResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        symptome = new Symptome();
        symptome.setLibelle(DEFAULT_LIBELLE);
        symptome.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSymptome() throws Exception {
        int databaseSizeBeforeCreate = symptomeRepository.findAll().size();

        // Create the Symptome

        restSymptomeMockMvc.perform(post("/api/symptomes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(symptome)))
                .andExpect(status().isCreated());

        // Validate the Symptome in the database
        List<Symptome> symptomes = symptomeRepository.findAll();
        assertThat(symptomes).hasSize(databaseSizeBeforeCreate + 1);
        Symptome testSymptome = symptomes.get(symptomes.size() - 1);
        assertThat(testSymptome.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSymptome.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSymptomes() throws Exception {
        // Initialize the database
        symptomeRepository.saveAndFlush(symptome);

        // Get all the symptomes
        restSymptomeMockMvc.perform(get("/api/symptomes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(symptome.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSymptome() throws Exception {
        // Initialize the database
        symptomeRepository.saveAndFlush(symptome);

        // Get the symptome
        restSymptomeMockMvc.perform(get("/api/symptomes/{id}", symptome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(symptome.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSymptome() throws Exception {
        // Get the symptome
        restSymptomeMockMvc.perform(get("/api/symptomes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSymptome() throws Exception {
        // Initialize the database
        symptomeRepository.saveAndFlush(symptome);

		int databaseSizeBeforeUpdate = symptomeRepository.findAll().size();

        // Update the symptome
        symptome.setLibelle(UPDATED_LIBELLE);
        symptome.setDescription(UPDATED_DESCRIPTION);
        

        restSymptomeMockMvc.perform(put("/api/symptomes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(symptome)))
                .andExpect(status().isOk());

        // Validate the Symptome in the database
        List<Symptome> symptomes = symptomeRepository.findAll();
        assertThat(symptomes).hasSize(databaseSizeBeforeUpdate);
        Symptome testSymptome = symptomes.get(symptomes.size() - 1);
        assertThat(testSymptome.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSymptome.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteSymptome() throws Exception {
        // Initialize the database
        symptomeRepository.saveAndFlush(symptome);

		int databaseSizeBeforeDelete = symptomeRepository.findAll().size();

        // Get the symptome
        restSymptomeMockMvc.perform(delete("/api/symptomes/{id}", symptome.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Symptome> symptomes = symptomeRepository.findAll();
        assertThat(symptomes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
