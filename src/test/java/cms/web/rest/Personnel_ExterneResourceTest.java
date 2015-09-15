package cms.web.rest;

import cms.Application;
import cms.domain.Personnel_Externe;
import cms.repository.Personnel_ExterneRepository;
import cms.repository.search.Personnel_ExterneSearchRepository;

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
 * Test class for the Personnel_ExterneResource REST controller.
 *
 * @see Personnel_ExterneResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Personnel_ExterneResourceTest {


    @Inject
    private Personnel_ExterneRepository personnel_ExterneRepository;

    @Inject
    private Personnel_ExterneSearchRepository personnel_ExterneSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPersonnel_ExterneMockMvc;

    private Personnel_Externe personnel_Externe;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Personnel_ExterneResource personnel_ExterneResource = new Personnel_ExterneResource();
        ReflectionTestUtils.setField(personnel_ExterneResource, "personnel_ExterneRepository", personnel_ExterneRepository);
        ReflectionTestUtils.setField(personnel_ExterneResource, "personnel_ExterneSearchRepository", personnel_ExterneSearchRepository);
        this.restPersonnel_ExterneMockMvc = MockMvcBuilders.standaloneSetup(personnel_ExterneResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        personnel_Externe = new Personnel_Externe();
    }

    @Test
    @Transactional
    public void createPersonnel_Externe() throws Exception {
        int databaseSizeBeforeCreate = personnel_ExterneRepository.findAll().size();

        // Create the Personnel_Externe

        restPersonnel_ExterneMockMvc.perform(post("/api/personnel_Externes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personnel_Externe)))
                .andExpect(status().isCreated());

        // Validate the Personnel_Externe in the database
        List<Personnel_Externe> personnel_Externes = personnel_ExterneRepository.findAll();
        assertThat(personnel_Externes).hasSize(databaseSizeBeforeCreate + 1);
        Personnel_Externe testPersonnel_Externe = personnel_Externes.get(personnel_Externes.size() - 1);
    }

    @Test
    @Transactional
    public void getAllPersonnel_Externes() throws Exception {
        // Initialize the database
        personnel_ExterneRepository.saveAndFlush(personnel_Externe);

        // Get all the personnel_Externes
        restPersonnel_ExterneMockMvc.perform(get("/api/personnel_Externes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(personnel_Externe.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPersonnel_Externe() throws Exception {
        // Initialize the database
        personnel_ExterneRepository.saveAndFlush(personnel_Externe);

        // Get the personnel_Externe
        restPersonnel_ExterneMockMvc.perform(get("/api/personnel_Externes/{id}", personnel_Externe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(personnel_Externe.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPersonnel_Externe() throws Exception {
        // Get the personnel_Externe
        restPersonnel_ExterneMockMvc.perform(get("/api/personnel_Externes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonnel_Externe() throws Exception {
        // Initialize the database
        personnel_ExterneRepository.saveAndFlush(personnel_Externe);

		int databaseSizeBeforeUpdate = personnel_ExterneRepository.findAll().size();

        // Update the personnel_Externe
        

        restPersonnel_ExterneMockMvc.perform(put("/api/personnel_Externes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personnel_Externe)))
                .andExpect(status().isOk());

        // Validate the Personnel_Externe in the database
        List<Personnel_Externe> personnel_Externes = personnel_ExterneRepository.findAll();
        assertThat(personnel_Externes).hasSize(databaseSizeBeforeUpdate);
        Personnel_Externe testPersonnel_Externe = personnel_Externes.get(personnel_Externes.size() - 1);
    }

    @Test
    @Transactional
    public void deletePersonnel_Externe() throws Exception {
        // Initialize the database
        personnel_ExterneRepository.saveAndFlush(personnel_Externe);

		int databaseSizeBeforeDelete = personnel_ExterneRepository.findAll().size();

        // Get the personnel_Externe
        restPersonnel_ExterneMockMvc.perform(delete("/api/personnel_Externes/{id}", personnel_Externe.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Personnel_Externe> personnel_Externes = personnel_ExterneRepository.findAll();
        assertThat(personnel_Externes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
