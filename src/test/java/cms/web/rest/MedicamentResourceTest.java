package cms.web.rest;

import cms.Application;
import cms.domain.Medicament;
import cms.repository.MedicamentRepository;
import cms.repository.search.MedicamentSearchRepository;

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
 * Test class for the MedicamentResource REST controller.
 *
 * @see MedicamentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MedicamentResourceTest {

    private static final String DEFAULT_NOM = "SAMPLE_TEXT";
    private static final String UPDATED_NOM = "UPDATED_TEXT";
    private static final String DEFAULT_POSOLOGIE = "SAMPLE_TEXT";
    private static final String UPDATED_POSOLOGIE = "UPDATED_TEXT";
    private static final String DEFAULT_IND_PARTICULIER = "SAMPLE_TEXT";
    private static final String UPDATED_IND_PARTICULIER = "UPDATED_TEXT";

    @Inject
    private MedicamentRepository medicamentRepository;

    @Inject
    private MedicamentSearchRepository medicamentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restMedicamentMockMvc;

    private Medicament medicament;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MedicamentResource medicamentResource = new MedicamentResource();
        ReflectionTestUtils.setField(medicamentResource, "medicamentRepository", medicamentRepository);
        ReflectionTestUtils.setField(medicamentResource, "medicamentSearchRepository", medicamentSearchRepository);
        this.restMedicamentMockMvc = MockMvcBuilders.standaloneSetup(medicamentResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        medicament = new Medicament();
        medicament.setNom(DEFAULT_NOM);
        medicament.setPosologie(DEFAULT_POSOLOGIE);
        medicament.setIndParticulier(DEFAULT_IND_PARTICULIER);
    }

    @Test
    @Transactional
    public void createMedicament() throws Exception {
        int databaseSizeBeforeCreate = medicamentRepository.findAll().size();

        // Create the Medicament

        restMedicamentMockMvc.perform(post("/api/medicaments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medicament)))
                .andExpect(status().isCreated());

        // Validate the Medicament in the database
        List<Medicament> medicaments = medicamentRepository.findAll();
        assertThat(medicaments).hasSize(databaseSizeBeforeCreate + 1);
        Medicament testMedicament = medicaments.get(medicaments.size() - 1);
        assertThat(testMedicament.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testMedicament.getPosologie()).isEqualTo(DEFAULT_POSOLOGIE);
        assertThat(testMedicament.getIndParticulier()).isEqualTo(DEFAULT_IND_PARTICULIER);
    }

    @Test
    @Transactional
    public void getAllMedicaments() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicaments
        restMedicamentMockMvc.perform(get("/api/medicaments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(medicament.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].posologie").value(hasItem(DEFAULT_POSOLOGIE.toString())))
                .andExpect(jsonPath("$.[*].indParticulier").value(hasItem(DEFAULT_IND_PARTICULIER.toString())));
    }

    @Test
    @Transactional
    public void getMedicament() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get the medicament
        restMedicamentMockMvc.perform(get("/api/medicaments/{id}", medicament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(medicament.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.posologie").value(DEFAULT_POSOLOGIE.toString()))
            .andExpect(jsonPath("$.indParticulier").value(DEFAULT_IND_PARTICULIER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedicament() throws Exception {
        // Get the medicament
        restMedicamentMockMvc.perform(get("/api/medicaments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicament() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

		int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();

        // Update the medicament
        medicament.setNom(UPDATED_NOM);
        medicament.setPosologie(UPDATED_POSOLOGIE);
        medicament.setIndParticulier(UPDATED_IND_PARTICULIER);
        

        restMedicamentMockMvc.perform(put("/api/medicaments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(medicament)))
                .andExpect(status().isOk());

        // Validate the Medicament in the database
        List<Medicament> medicaments = medicamentRepository.findAll();
        assertThat(medicaments).hasSize(databaseSizeBeforeUpdate);
        Medicament testMedicament = medicaments.get(medicaments.size() - 1);
        assertThat(testMedicament.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMedicament.getPosologie()).isEqualTo(UPDATED_POSOLOGIE);
        assertThat(testMedicament.getIndParticulier()).isEqualTo(UPDATED_IND_PARTICULIER);
    }

    @Test
    @Transactional
    public void deleteMedicament() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

		int databaseSizeBeforeDelete = medicamentRepository.findAll().size();

        // Get the medicament
        restMedicamentMockMvc.perform(delete("/api/medicaments/{id}", medicament.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Medicament> medicaments = medicamentRepository.findAll();
        assertThat(medicaments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
