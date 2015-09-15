package cms.web.rest;

import cms.Application;
import cms.domain.InstitutionSanitaire;
import cms.repository.InstitutionSanitaireRepository;
import cms.repository.search.InstitutionSanitaireSearchRepository;

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
 * Test class for the InstitutionSanitaireResource REST controller.
 *
 * @see InstitutionSanitaireResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InstitutionSanitaireResourceTest {

    private static final String DEFAULT_RAISON_SOCIAL = "SAMPLE_TEXT";
    private static final String UPDATED_RAISON_SOCIAL = "UPDATED_TEXT";
    private static final String DEFAULT_NOM = "SAMPLE_TEXT";
    private static final String UPDATED_NOM = "UPDATED_TEXT";
    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";

    @Inject
    private InstitutionSanitaireRepository institutionSanitaireRepository;

    @Inject
    private InstitutionSanitaireSearchRepository institutionSanitaireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restInstitutionSanitaireMockMvc;

    private InstitutionSanitaire institutionSanitaire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstitutionSanitaireResource institutionSanitaireResource = new InstitutionSanitaireResource();
        ReflectionTestUtils.setField(institutionSanitaireResource, "institutionSanitaireRepository", institutionSanitaireRepository);
        ReflectionTestUtils.setField(institutionSanitaireResource, "institutionSanitaireSearchRepository", institutionSanitaireSearchRepository);
        this.restInstitutionSanitaireMockMvc = MockMvcBuilders.standaloneSetup(institutionSanitaireResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        institutionSanitaire = new InstitutionSanitaire();
        institutionSanitaire.setRaisonSocial(DEFAULT_RAISON_SOCIAL);
        institutionSanitaire.setNom(DEFAULT_NOM);
        institutionSanitaire.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createInstitutionSanitaire() throws Exception {
        int databaseSizeBeforeCreate = institutionSanitaireRepository.findAll().size();

        // Create the InstitutionSanitaire

        restInstitutionSanitaireMockMvc.perform(post("/api/institutionSanitaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(institutionSanitaire)))
                .andExpect(status().isCreated());

        // Validate the InstitutionSanitaire in the database
        List<InstitutionSanitaire> institutionSanitaires = institutionSanitaireRepository.findAll();
        assertThat(institutionSanitaires).hasSize(databaseSizeBeforeCreate + 1);
        InstitutionSanitaire testInstitutionSanitaire = institutionSanitaires.get(institutionSanitaires.size() - 1);
        assertThat(testInstitutionSanitaire.getRaisonSocial()).isEqualTo(DEFAULT_RAISON_SOCIAL);
        assertThat(testInstitutionSanitaire.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testInstitutionSanitaire.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllInstitutionSanitaires() throws Exception {
        // Initialize the database
        institutionSanitaireRepository.saveAndFlush(institutionSanitaire);

        // Get all the institutionSanitaires
        restInstitutionSanitaireMockMvc.perform(get("/api/institutionSanitaires"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(institutionSanitaire.getId().intValue())))
                .andExpect(jsonPath("$.[*].raisonSocial").value(hasItem(DEFAULT_RAISON_SOCIAL.toString())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getInstitutionSanitaire() throws Exception {
        // Initialize the database
        institutionSanitaireRepository.saveAndFlush(institutionSanitaire);

        // Get the institutionSanitaire
        restInstitutionSanitaireMockMvc.perform(get("/api/institutionSanitaires/{id}", institutionSanitaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(institutionSanitaire.getId().intValue()))
            .andExpect(jsonPath("$.raisonSocial").value(DEFAULT_RAISON_SOCIAL.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstitutionSanitaire() throws Exception {
        // Get the institutionSanitaire
        restInstitutionSanitaireMockMvc.perform(get("/api/institutionSanitaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstitutionSanitaire() throws Exception {
        // Initialize the database
        institutionSanitaireRepository.saveAndFlush(institutionSanitaire);

		int databaseSizeBeforeUpdate = institutionSanitaireRepository.findAll().size();

        // Update the institutionSanitaire
        institutionSanitaire.setRaisonSocial(UPDATED_RAISON_SOCIAL);
        institutionSanitaire.setNom(UPDATED_NOM);
        institutionSanitaire.setType(UPDATED_TYPE);
        

        restInstitutionSanitaireMockMvc.perform(put("/api/institutionSanitaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(institutionSanitaire)))
                .andExpect(status().isOk());

        // Validate the InstitutionSanitaire in the database
        List<InstitutionSanitaire> institutionSanitaires = institutionSanitaireRepository.findAll();
        assertThat(institutionSanitaires).hasSize(databaseSizeBeforeUpdate);
        InstitutionSanitaire testInstitutionSanitaire = institutionSanitaires.get(institutionSanitaires.size() - 1);
        assertThat(testInstitutionSanitaire.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
        assertThat(testInstitutionSanitaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testInstitutionSanitaire.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteInstitutionSanitaire() throws Exception {
        // Initialize the database
        institutionSanitaireRepository.saveAndFlush(institutionSanitaire);

		int databaseSizeBeforeDelete = institutionSanitaireRepository.findAll().size();

        // Get the institutionSanitaire
        restInstitutionSanitaireMockMvc.perform(delete("/api/institutionSanitaires/{id}", institutionSanitaire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<InstitutionSanitaire> institutionSanitaires = institutionSanitaireRepository.findAll();
        assertThat(institutionSanitaires).hasSize(databaseSizeBeforeDelete - 1);
    }
}
