package cms.web.rest;

import cms.Application;
import cms.domain.Examens;
import cms.repository.ExamensRepository;
import cms.repository.search.ExamensSearchRepository;

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
 * Test class for the ExamensResource REST controller.
 *
 * @see ExamensResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ExamensResourceTest {

    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";
    private static final String DEFAULT_LIBELLE = "SAMPLE_TEXT";
    private static final String UPDATED_LIBELLE = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE = new LocalDate();

    @Inject
    private ExamensRepository examensRepository;

    @Inject
    private ExamensSearchRepository examensSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restExamensMockMvc;

    private Examens examens;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExamensResource examensResource = new ExamensResource();
        ReflectionTestUtils.setField(examensResource, "examensRepository", examensRepository);
        ReflectionTestUtils.setField(examensResource, "examensSearchRepository", examensSearchRepository);
        this.restExamensMockMvc = MockMvcBuilders.standaloneSetup(examensResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        examens = new Examens();
        examens.setType(DEFAULT_TYPE);
        examens.setLibelle(DEFAULT_LIBELLE);
        examens.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createExamens() throws Exception {
        int databaseSizeBeforeCreate = examensRepository.findAll().size();

        // Create the Examens

        restExamensMockMvc.perform(post("/api/examenss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(examens)))
                .andExpect(status().isCreated());

        // Validate the Examens in the database
        List<Examens> examenss = examensRepository.findAll();
        assertThat(examenss).hasSize(databaseSizeBeforeCreate + 1);
        Examens testExamens = examenss.get(examenss.size() - 1);
        assertThat(testExamens.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testExamens.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testExamens.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void getAllExamenss() throws Exception {
        // Initialize the database
        examensRepository.saveAndFlush(examens);

        // Get all the examenss
        restExamensMockMvc.perform(get("/api/examenss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(examens.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getExamens() throws Exception {
        // Initialize the database
        examensRepository.saveAndFlush(examens);

        // Get the examens
        restExamensMockMvc.perform(get("/api/examenss/{id}", examens.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(examens.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExamens() throws Exception {
        // Get the examens
        restExamensMockMvc.perform(get("/api/examenss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExamens() throws Exception {
        // Initialize the database
        examensRepository.saveAndFlush(examens);

		int databaseSizeBeforeUpdate = examensRepository.findAll().size();

        // Update the examens
        examens.setType(UPDATED_TYPE);
        examens.setLibelle(UPDATED_LIBELLE);
        examens.setDate(UPDATED_DATE);
        

        restExamensMockMvc.perform(put("/api/examenss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(examens)))
                .andExpect(status().isOk());

        // Validate the Examens in the database
        List<Examens> examenss = examensRepository.findAll();
        assertThat(examenss).hasSize(databaseSizeBeforeUpdate);
        Examens testExamens = examenss.get(examenss.size() - 1);
        assertThat(testExamens.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExamens.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testExamens.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteExamens() throws Exception {
        // Initialize the database
        examensRepository.saveAndFlush(examens);

		int databaseSizeBeforeDelete = examensRepository.findAll().size();

        // Get the examens
        restExamensMockMvc.perform(delete("/api/examenss/{id}", examens.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Examens> examenss = examensRepository.findAll();
        assertThat(examenss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
