package cms.web.rest;

import cms.Application;
import cms.domain.Consultation;
import cms.repository.ConsultationRepository;
import cms.repository.search.ConsultationSearchRepository;

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
 * Test class for the ConsultationResource REST controller.
 *
 * @see ConsultationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ConsultationResourceTest {

    private static final String DEFAULT_MOTIF = "SAMPLE_TEXT";
    private static final String UPDATED_MOTIF = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE = new LocalDate();
    private static final String DEFAULT_DIAGNOSTIQUE_CONSUL = "SAMPLE_TEXT";
    private static final String UPDATED_DIAGNOSTIQUE_CONSUL = "UPDATED_TEXT";

    @Inject
    private ConsultationRepository consultationRepository;

    @Inject
    private ConsultationSearchRepository consultationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restConsultationMockMvc;

    private Consultation consultation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConsultationResource consultationResource = new ConsultationResource();
        ReflectionTestUtils.setField(consultationResource, "consultationRepository", consultationRepository);
        ReflectionTestUtils.setField(consultationResource, "consultationSearchRepository", consultationSearchRepository);
        this.restConsultationMockMvc = MockMvcBuilders.standaloneSetup(consultationResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        consultation = new Consultation();
        consultation.setMotif(DEFAULT_MOTIF);
        consultation.setDate(DEFAULT_DATE);
        consultation.setDiagnostiqueConsul(DEFAULT_DIAGNOSTIQUE_CONSUL);
    }

    @Test
    @Transactional
    public void createConsultation() throws Exception {
        int databaseSizeBeforeCreate = consultationRepository.findAll().size();

        // Create the Consultation

        restConsultationMockMvc.perform(post("/api/consultations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(consultation)))
                .andExpect(status().isCreated());

        // Validate the Consultation in the database
        List<Consultation> consultations = consultationRepository.findAll();
        assertThat(consultations).hasSize(databaseSizeBeforeCreate + 1);
        Consultation testConsultation = consultations.get(consultations.size() - 1);
        assertThat(testConsultation.getMotif()).isEqualTo(DEFAULT_MOTIF);
        assertThat(testConsultation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testConsultation.getDiagnostiqueConsul()).isEqualTo(DEFAULT_DIAGNOSTIQUE_CONSUL);
    }

    @Test
    @Transactional
    public void getAllConsultations() throws Exception {
        // Initialize the database
        consultationRepository.saveAndFlush(consultation);

        // Get all the consultations
        restConsultationMockMvc.perform(get("/api/consultations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(consultation.getId().intValue())))
                .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].diagnostiqueConsul").value(hasItem(DEFAULT_DIAGNOSTIQUE_CONSUL.toString())));
    }

    @Test
    @Transactional
    public void getConsultation() throws Exception {
        // Initialize the database
        consultationRepository.saveAndFlush(consultation);

        // Get the consultation
        restConsultationMockMvc.perform(get("/api/consultations/{id}", consultation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(consultation.getId().intValue()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.diagnostiqueConsul").value(DEFAULT_DIAGNOSTIQUE_CONSUL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConsultation() throws Exception {
        // Get the consultation
        restConsultationMockMvc.perform(get("/api/consultations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConsultation() throws Exception {
        // Initialize the database
        consultationRepository.saveAndFlush(consultation);

		int databaseSizeBeforeUpdate = consultationRepository.findAll().size();

        // Update the consultation
        consultation.setMotif(UPDATED_MOTIF);
        consultation.setDate(UPDATED_DATE);
        consultation.setDiagnostiqueConsul(UPDATED_DIAGNOSTIQUE_CONSUL);
        

        restConsultationMockMvc.perform(put("/api/consultations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(consultation)))
                .andExpect(status().isOk());

        // Validate the Consultation in the database
        List<Consultation> consultations = consultationRepository.findAll();
        assertThat(consultations).hasSize(databaseSizeBeforeUpdate);
        Consultation testConsultation = consultations.get(consultations.size() - 1);
        assertThat(testConsultation.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testConsultation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testConsultation.getDiagnostiqueConsul()).isEqualTo(UPDATED_DIAGNOSTIQUE_CONSUL);
    }

    @Test
    @Transactional
    public void deleteConsultation() throws Exception {
        // Initialize the database
        consultationRepository.saveAndFlush(consultation);

		int databaseSizeBeforeDelete = consultationRepository.findAll().size();

        // Get the consultation
        restConsultationMockMvc.perform(delete("/api/consultations/{id}", consultation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Consultation> consultations = consultationRepository.findAll();
        assertThat(consultations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
