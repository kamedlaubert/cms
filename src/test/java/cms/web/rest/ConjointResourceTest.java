package cms.web.rest;

import cms.Application;
import cms.domain.Conjoint;
import cms.repository.ConjointRepository;
import cms.repository.search.ConjointSearchRepository;

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
 * Test class for the ConjointResource REST controller.
 *
 * @see ConjointResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ConjointResourceTest {


    @Inject
    private ConjointRepository conjointRepository;

    @Inject
    private ConjointSearchRepository conjointSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restConjointMockMvc;

    private Conjoint conjoint;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConjointResource conjointResource = new ConjointResource();
        ReflectionTestUtils.setField(conjointResource, "conjointRepository", conjointRepository);
        ReflectionTestUtils.setField(conjointResource, "conjointSearchRepository", conjointSearchRepository);
        this.restConjointMockMvc = MockMvcBuilders.standaloneSetup(conjointResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        conjoint = new Conjoint();
    }

    @Test
    @Transactional
    public void createConjoint() throws Exception {
        int databaseSizeBeforeCreate = conjointRepository.findAll().size();

        // Create the Conjoint

        restConjointMockMvc.perform(post("/api/conjoints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(conjoint)))
                .andExpect(status().isCreated());

        // Validate the Conjoint in the database
        List<Conjoint> conjoints = conjointRepository.findAll();
        assertThat(conjoints).hasSize(databaseSizeBeforeCreate + 1);
        Conjoint testConjoint = conjoints.get(conjoints.size() - 1);
    }

    @Test
    @Transactional
    public void getAllConjoints() throws Exception {
        // Initialize the database
        conjointRepository.saveAndFlush(conjoint);

        // Get all the conjoints
        restConjointMockMvc.perform(get("/api/conjoints"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(conjoint.getId().intValue())));
    }

    @Test
    @Transactional
    public void getConjoint() throws Exception {
        // Initialize the database
        conjointRepository.saveAndFlush(conjoint);

        // Get the conjoint
        restConjointMockMvc.perform(get("/api/conjoints/{id}", conjoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(conjoint.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingConjoint() throws Exception {
        // Get the conjoint
        restConjointMockMvc.perform(get("/api/conjoints/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConjoint() throws Exception {
        // Initialize the database
        conjointRepository.saveAndFlush(conjoint);

		int databaseSizeBeforeUpdate = conjointRepository.findAll().size();

        // Update the conjoint
        

        restConjointMockMvc.perform(put("/api/conjoints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(conjoint)))
                .andExpect(status().isOk());

        // Validate the Conjoint in the database
        List<Conjoint> conjoints = conjointRepository.findAll();
        assertThat(conjoints).hasSize(databaseSizeBeforeUpdate);
        Conjoint testConjoint = conjoints.get(conjoints.size() - 1);
    }

    @Test
    @Transactional
    public void deleteConjoint() throws Exception {
        // Initialize the database
        conjointRepository.saveAndFlush(conjoint);

		int databaseSizeBeforeDelete = conjointRepository.findAll().size();

        // Get the conjoint
        restConjointMockMvc.perform(delete("/api/conjoints/{id}", conjoint.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Conjoint> conjoints = conjointRepository.findAll();
        assertThat(conjoints).hasSize(databaseSizeBeforeDelete - 1);
    }
}
