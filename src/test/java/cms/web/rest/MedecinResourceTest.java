package cms.web.rest;

import cms.Application;
import cms.domain.Medecin;
import cms.repository.MedecinRepository;
import cms.repository.search.MedecinSearchRepository;

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
 * Test class for the MedecinResource REST controller.
 *
 * @see MedecinResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MedecinResourceTest {


    @Inject
    private MedecinRepository medecinRepository;

    @Inject
    private MedecinSearchRepository medecinSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restMedecinMockMvc;

    private Medecin medecin;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MedecinResource medecinResource = new MedecinResource();
        ReflectionTestUtils.setField(medecinResource, "medecinRepository", medecinRepository);
        ReflectionTestUtils.setField(medecinResource, "medecinSearchRepository", medecinSearchRepository);
        this.restMedecinMockMvc = MockMvcBuilders.standaloneSetup(medecinResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        medecin = new Medecin();
    }

    @Test
    @Transactional
    public void createMedecin() throws Exception {
        int databaseSizeBeforeCreate = medecinRepository.findAll().size();

        // Create the Medecin

        restMedecinMockMvc.perform(post("/api/medecins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medecin)))
                .andExpect(status().isCreated());

        // Validate the Medecin in the database
        List<Medecin> medecins = medecinRepository.findAll();
        assertThat(medecins).hasSize(databaseSizeBeforeCreate + 1);
        Medecin testMedecin = medecins.get(medecins.size() - 1);
    }

    @Test
    @Transactional
    public void getAllMedecins() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecins
        restMedecinMockMvc.perform(get("/api/medecins"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(medecin.getId().intValue())));
    }

    @Test
    @Transactional
    public void getMedecin() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get the medecin
        restMedecinMockMvc.perform(get("/api/medecins/{id}", medecin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(medecin.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMedecin() throws Exception {
        // Get the medecin
        restMedecinMockMvc.perform(get("/api/medecins/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedecin() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

		int databaseSizeBeforeUpdate = medecinRepository.findAll().size();

        // Update the medecin
        

        restMedecinMockMvc.perform(put("/api/medecins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medecin)))
                .andExpect(status().isOk());

        // Validate the Medecin in the database
        List<Medecin> medecins = medecinRepository.findAll();
        assertThat(medecins).hasSize(databaseSizeBeforeUpdate);
        Medecin testMedecin = medecins.get(medecins.size() - 1);
    }

    @Test
    @Transactional
    public void deleteMedecin() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

		int databaseSizeBeforeDelete = medecinRepository.findAll().size();

        // Get the medecin
        restMedecinMockMvc.perform(delete("/api/medecins/{id}", medecin.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Medecin> medecins = medecinRepository.findAll();
        assertThat(medecins).hasSize(databaseSizeBeforeDelete - 1);
    }
}
