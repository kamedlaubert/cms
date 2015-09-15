package cms.web.rest;

import cms.Application;
import cms.domain.Hospitalisation;
import cms.repository.HospitalisationRepository;
import cms.repository.search.HospitalisationSearchRepository;

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
 * Test class for the HospitalisationResource REST controller.
 *
 * @see HospitalisationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HospitalisationResourceTest {

    private static final String DEFAULT_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_CODE = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_DATE_ADMISSION = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE_ADMISSION = new LocalDate();

    private static final LocalDate DEFAULT_DATE_SORTI = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE_SORTI = new LocalDate();
    private static final String DEFAULT_MOTIF = "SAMPLE_TEXT";
    private static final String UPDATED_MOTIF = "UPDATED_TEXT";
    private static final String DEFAULT_BILAN = "SAMPLE_TEXT";
    private static final String UPDATED_BILAN = "UPDATED_TEXT";

    @Inject
    private HospitalisationRepository hospitalisationRepository;

    @Inject
    private HospitalisationSearchRepository hospitalisationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restHospitalisationMockMvc;

    private Hospitalisation hospitalisation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HospitalisationResource hospitalisationResource = new HospitalisationResource();
        ReflectionTestUtils.setField(hospitalisationResource, "hospitalisationRepository", hospitalisationRepository);
        ReflectionTestUtils.setField(hospitalisationResource, "hospitalisationSearchRepository", hospitalisationSearchRepository);
        this.restHospitalisationMockMvc = MockMvcBuilders.standaloneSetup(hospitalisationResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hospitalisation = new Hospitalisation();
        hospitalisation.setCode(DEFAULT_CODE);
        hospitalisation.setDateAdmission(DEFAULT_DATE_ADMISSION);
        hospitalisation.setDateSorti(DEFAULT_DATE_SORTI);
        hospitalisation.setMotif(DEFAULT_MOTIF);
        hospitalisation.setBilan(DEFAULT_BILAN);
    }

    @Test
    @Transactional
    public void createHospitalisation() throws Exception {
        int databaseSizeBeforeCreate = hospitalisationRepository.findAll().size();

        // Create the Hospitalisation

        restHospitalisationMockMvc.perform(post("/api/hospitalisations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hospitalisation)))
                .andExpect(status().isCreated());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisations = hospitalisationRepository.findAll();
        assertThat(hospitalisations).hasSize(databaseSizeBeforeCreate + 1);
        Hospitalisation testHospitalisation = hospitalisations.get(hospitalisations.size() - 1);
        assertThat(testHospitalisation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testHospitalisation.getDateAdmission()).isEqualTo(DEFAULT_DATE_ADMISSION);
        assertThat(testHospitalisation.getDateSorti()).isEqualTo(DEFAULT_DATE_SORTI);
        assertThat(testHospitalisation.getMotif()).isEqualTo(DEFAULT_MOTIF);
        assertThat(testHospitalisation.getBilan()).isEqualTo(DEFAULT_BILAN);
    }

    @Test
    @Transactional
    public void getAllHospitalisations() throws Exception {
        // Initialize the database
        hospitalisationRepository.saveAndFlush(hospitalisation);

        // Get all the hospitalisations
        restHospitalisationMockMvc.perform(get("/api/hospitalisations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hospitalisation.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].dateAdmission").value(hasItem(DEFAULT_DATE_ADMISSION.toString())))
                .andExpect(jsonPath("$.[*].dateSorti").value(hasItem(DEFAULT_DATE_SORTI.toString())))
                .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF.toString())))
                .andExpect(jsonPath("$.[*].bilan").value(hasItem(DEFAULT_BILAN.toString())));
    }

    @Test
    @Transactional
    public void getHospitalisation() throws Exception {
        // Initialize the database
        hospitalisationRepository.saveAndFlush(hospitalisation);

        // Get the hospitalisation
        restHospitalisationMockMvc.perform(get("/api/hospitalisations/{id}", hospitalisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hospitalisation.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.dateAdmission").value(DEFAULT_DATE_ADMISSION.toString()))
            .andExpect(jsonPath("$.dateSorti").value(DEFAULT_DATE_SORTI.toString()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF.toString()))
            .andExpect(jsonPath("$.bilan").value(DEFAULT_BILAN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHospitalisation() throws Exception {
        // Get the hospitalisation
        restHospitalisationMockMvc.perform(get("/api/hospitalisations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHospitalisation() throws Exception {
        // Initialize the database
        hospitalisationRepository.saveAndFlush(hospitalisation);

		int databaseSizeBeforeUpdate = hospitalisationRepository.findAll().size();

        // Update the hospitalisation
        hospitalisation.setCode(UPDATED_CODE);
        hospitalisation.setDateAdmission(UPDATED_DATE_ADMISSION);
        hospitalisation.setDateSorti(UPDATED_DATE_SORTI);
        hospitalisation.setMotif(UPDATED_MOTIF);
        hospitalisation.setBilan(UPDATED_BILAN);
        

        restHospitalisationMockMvc.perform(put("/api/hospitalisations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hospitalisation)))
                .andExpect(status().isOk());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisations = hospitalisationRepository.findAll();
        assertThat(hospitalisations).hasSize(databaseSizeBeforeUpdate);
        Hospitalisation testHospitalisation = hospitalisations.get(hospitalisations.size() - 1);
        assertThat(testHospitalisation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testHospitalisation.getDateAdmission()).isEqualTo(UPDATED_DATE_ADMISSION);
        assertThat(testHospitalisation.getDateSorti()).isEqualTo(UPDATED_DATE_SORTI);
        assertThat(testHospitalisation.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testHospitalisation.getBilan()).isEqualTo(UPDATED_BILAN);
    }

    @Test
    @Transactional
    public void deleteHospitalisation() throws Exception {
        // Initialize the database
        hospitalisationRepository.saveAndFlush(hospitalisation);

		int databaseSizeBeforeDelete = hospitalisationRepository.findAll().size();

        // Get the hospitalisation
        restHospitalisationMockMvc.perform(delete("/api/hospitalisations/{id}", hospitalisation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Hospitalisation> hospitalisations = hospitalisationRepository.findAll();
        assertThat(hospitalisations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
