package cms.web.rest;

import cms.Application;
import cms.domain.BPC;
import cms.repository.BPCRepository;
import cms.repository.search.BPCSearchRepository;

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
 * Test class for the BPCResource REST controller.
 *
 * @see BPCResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BPCResourceTest {

    private static final String DEFAULT_NUMERO_BPC = "SAMPLE_TEXT";
    private static final String UPDATED_NUMERO_BPC = "UPDATED_TEXT";
    private static final String DEFAULT_OBJECT_BPC = "SAMPLE_TEXT";
    private static final String UPDATED_OBJECT_BPC = "UPDATED_TEXT";

    @Inject
    private BPCRepository bPCRepository;

    @Inject
    private BPCSearchRepository bPCSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restBPCMockMvc;

    private BPC bPC;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BPCResource bPCResource = new BPCResource();
        ReflectionTestUtils.setField(bPCResource, "bPCRepository", bPCRepository);
        ReflectionTestUtils.setField(bPCResource, "bPCSearchRepository", bPCSearchRepository);
        this.restBPCMockMvc = MockMvcBuilders.standaloneSetup(bPCResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bPC = new BPC();
        bPC.setNumeroBpc(DEFAULT_NUMERO_BPC);
        bPC.setObjectBpc(DEFAULT_OBJECT_BPC);
    }

    @Test
    @Transactional
    public void createBPC() throws Exception {
        int databaseSizeBeforeCreate = bPCRepository.findAll().size();

        // Create the BPC

        restBPCMockMvc.perform(post("/api/bPCs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bPC)))
                .andExpect(status().isCreated());

        // Validate the BPC in the database
        List<BPC> bPCs = bPCRepository.findAll();
        assertThat(bPCs).hasSize(databaseSizeBeforeCreate + 1);
        BPC testBPC = bPCs.get(bPCs.size() - 1);
        assertThat(testBPC.getNumeroBpc()).isEqualTo(DEFAULT_NUMERO_BPC);
        assertThat(testBPC.getObjectBpc()).isEqualTo(DEFAULT_OBJECT_BPC);
    }

    @Test
    @Transactional
    public void getAllBPCs() throws Exception {
        // Initialize the database
        bPCRepository.saveAndFlush(bPC);

        // Get all the bPCs
        restBPCMockMvc.perform(get("/api/bPCs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bPC.getId().intValue())))
                .andExpect(jsonPath("$.[*].numeroBpc").value(hasItem(DEFAULT_NUMERO_BPC.toString())))
                .andExpect(jsonPath("$.[*].objectBpc").value(hasItem(DEFAULT_OBJECT_BPC.toString())));
    }

    @Test
    @Transactional
    public void getBPC() throws Exception {
        // Initialize the database
        bPCRepository.saveAndFlush(bPC);

        // Get the bPC
        restBPCMockMvc.perform(get("/api/bPCs/{id}", bPC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bPC.getId().intValue()))
            .andExpect(jsonPath("$.numeroBpc").value(DEFAULT_NUMERO_BPC.toString()))
            .andExpect(jsonPath("$.objectBpc").value(DEFAULT_OBJECT_BPC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBPC() throws Exception {
        // Get the bPC
        restBPCMockMvc.perform(get("/api/bPCs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBPC() throws Exception {
        // Initialize the database
        bPCRepository.saveAndFlush(bPC);

		int databaseSizeBeforeUpdate = bPCRepository.findAll().size();

        // Update the bPC
        bPC.setNumeroBpc(UPDATED_NUMERO_BPC);
        bPC.setObjectBpc(UPDATED_OBJECT_BPC);
        

        restBPCMockMvc.perform(put("/api/bPCs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bPC)))
                .andExpect(status().isOk());

        // Validate the BPC in the database
        List<BPC> bPCs = bPCRepository.findAll();
        assertThat(bPCs).hasSize(databaseSizeBeforeUpdate);
        BPC testBPC = bPCs.get(bPCs.size() - 1);
        assertThat(testBPC.getNumeroBpc()).isEqualTo(UPDATED_NUMERO_BPC);
        assertThat(testBPC.getObjectBpc()).isEqualTo(UPDATED_OBJECT_BPC);
    }

    @Test
    @Transactional
    public void deleteBPC() throws Exception {
        // Initialize the database
        bPCRepository.saveAndFlush(bPC);

		int databaseSizeBeforeDelete = bPCRepository.findAll().size();

        // Get the bPC
        restBPCMockMvc.perform(delete("/api/bPCs/{id}", bPC.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BPC> bPCs = bPCRepository.findAll();
        assertThat(bPCs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
