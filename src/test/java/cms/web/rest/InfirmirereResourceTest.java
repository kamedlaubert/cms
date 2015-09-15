package cms.web.rest;

import cms.Application;
import cms.domain.Infirmirere;
import cms.repository.InfirmirereRepository;
import cms.repository.search.InfirmirereSearchRepository;

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
 * Test class for the InfirmirereResource REST controller.
 *
 * @see InfirmirereResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InfirmirereResourceTest {


    @Inject
    private InfirmirereRepository infirmirereRepository;

    @Inject
    private InfirmirereSearchRepository infirmirereSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restInfirmirereMockMvc;

    private Infirmirere infirmirere;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InfirmirereResource infirmirereResource = new InfirmirereResource();
        ReflectionTestUtils.setField(infirmirereResource, "infirmirereRepository", infirmirereRepository);
        ReflectionTestUtils.setField(infirmirereResource, "infirmirereSearchRepository", infirmirereSearchRepository);
        this.restInfirmirereMockMvc = MockMvcBuilders.standaloneSetup(infirmirereResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        infirmirere = new Infirmirere();
    }

    @Test
    @Transactional
    public void createInfirmirere() throws Exception {
        int databaseSizeBeforeCreate = infirmirereRepository.findAll().size();

        // Create the Infirmirere

        restInfirmirereMockMvc.perform(post("/api/infirmireres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(infirmirere)))
                .andExpect(status().isCreated());

        // Validate the Infirmirere in the database
        List<Infirmirere> infirmireres = infirmirereRepository.findAll();
        assertThat(infirmireres).hasSize(databaseSizeBeforeCreate + 1);
        Infirmirere testInfirmirere = infirmireres.get(infirmireres.size() - 1);
    }

    @Test
    @Transactional
    public void getAllInfirmireres() throws Exception {
        // Initialize the database
        infirmirereRepository.saveAndFlush(infirmirere);

        // Get all the infirmireres
        restInfirmirereMockMvc.perform(get("/api/infirmireres"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(infirmirere.getId().intValue())));
    }

    @Test
    @Transactional
    public void getInfirmirere() throws Exception {
        // Initialize the database
        infirmirereRepository.saveAndFlush(infirmirere);

        // Get the infirmirere
        restInfirmirereMockMvc.perform(get("/api/infirmireres/{id}", infirmirere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(infirmirere.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInfirmirere() throws Exception {
        // Get the infirmirere
        restInfirmirereMockMvc.perform(get("/api/infirmireres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInfirmirere() throws Exception {
        // Initialize the database
        infirmirereRepository.saveAndFlush(infirmirere);

		int databaseSizeBeforeUpdate = infirmirereRepository.findAll().size();

        // Update the infirmirere
        

        restInfirmirereMockMvc.perform(put("/api/infirmireres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(infirmirere)))
                .andExpect(status().isOk());

        // Validate the Infirmirere in the database
        List<Infirmirere> infirmireres = infirmirereRepository.findAll();
        assertThat(infirmireres).hasSize(databaseSizeBeforeUpdate);
        Infirmirere testInfirmirere = infirmireres.get(infirmireres.size() - 1);
    }

    @Test
    @Transactional
    public void deleteInfirmirere() throws Exception {
        // Initialize the database
        infirmirereRepository.saveAndFlush(infirmirere);

		int databaseSizeBeforeDelete = infirmirereRepository.findAll().size();

        // Get the infirmirere
        restInfirmirereMockMvc.perform(delete("/api/infirmireres/{id}", infirmirere.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Infirmirere> infirmireres = infirmirereRepository.findAll();
        assertThat(infirmireres).hasSize(databaseSizeBeforeDelete - 1);
    }
}
