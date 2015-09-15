package cms.web.rest;

import cms.Application;
import cms.domain.Enfant;
import cms.repository.EnfantRepository;
import cms.repository.search.EnfantSearchRepository;

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
 * Test class for the EnfantResource REST controller.
 *
 * @see EnfantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EnfantResourceTest {


    @Inject
    private EnfantRepository enfantRepository;

    @Inject
    private EnfantSearchRepository enfantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restEnfantMockMvc;

    private Enfant enfant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnfantResource enfantResource = new EnfantResource();
        ReflectionTestUtils.setField(enfantResource, "enfantRepository", enfantRepository);
        ReflectionTestUtils.setField(enfantResource, "enfantSearchRepository", enfantSearchRepository);
        this.restEnfantMockMvc = MockMvcBuilders.standaloneSetup(enfantResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        enfant = new Enfant();
    }

    @Test
    @Transactional
    public void createEnfant() throws Exception {
        int databaseSizeBeforeCreate = enfantRepository.findAll().size();

        // Create the Enfant

        restEnfantMockMvc.perform(post("/api/enfants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enfant)))
                .andExpect(status().isCreated());

        // Validate the Enfant in the database
        List<Enfant> enfants = enfantRepository.findAll();
        assertThat(enfants).hasSize(databaseSizeBeforeCreate + 1);
        Enfant testEnfant = enfants.get(enfants.size() - 1);
    }

    @Test
    @Transactional
    public void getAllEnfants() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfants
        restEnfantMockMvc.perform(get("/api/enfants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(enfant.getId().intValue())));
    }

    @Test
    @Transactional
    public void getEnfant() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get the enfant
        restEnfantMockMvc.perform(get("/api/enfants/{id}", enfant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(enfant.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEnfant() throws Exception {
        // Get the enfant
        restEnfantMockMvc.perform(get("/api/enfants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnfant() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

		int databaseSizeBeforeUpdate = enfantRepository.findAll().size();

        // Update the enfant
        

        restEnfantMockMvc.perform(put("/api/enfants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enfant)))
                .andExpect(status().isOk());

        // Validate the Enfant in the database
        List<Enfant> enfants = enfantRepository.findAll();
        assertThat(enfants).hasSize(databaseSizeBeforeUpdate);
        Enfant testEnfant = enfants.get(enfants.size() - 1);
    }

    @Test
    @Transactional
    public void deleteEnfant() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

		int databaseSizeBeforeDelete = enfantRepository.findAll().size();

        // Get the enfant
        restEnfantMockMvc.perform(delete("/api/enfants/{id}", enfant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Enfant> enfants = enfantRepository.findAll();
        assertThat(enfants).hasSize(databaseSizeBeforeDelete - 1);
    }
}
