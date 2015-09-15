package cms.web.rest;

import cms.Application;
import cms.domain.Personnel;
import cms.repository.PersonnelRepository;
import cms.repository.search.PersonnelSearchRepository;

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
 * Test class for the PersonnelResource REST controller.
 *
 * @see PersonnelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PersonnelResourceTest {

    private static final String DEFAULT_MATRICULE = "SAMPLE_TEXT";
    private static final String UPDATED_MATRICULE = "UPDATED_TEXT";
    private static final String DEFAULT_NOM = "SAMPLE_TEXT";
    private static final String UPDATED_NOM = "UPDATED_TEXT";
    private static final String DEFAULT_PRENOM = "SAMPLE_TEXT";
    private static final String UPDATED_PRENOM = "UPDATED_TEXT";
    private static final String DEFAULT_DIRECTION = "SAMPLE_TEXT";
    private static final String UPDATED_DIRECTION = "UPDATED_TEXT";
    private static final String DEFAULT_AGE = "SAMPLE_TEXT";
    private static final String UPDATED_AGE = "UPDATED_TEXT";
    private static final String DEFAULT_SEXE = "SAMPLE_TEXT";
    private static final String UPDATED_SEXE = "UPDATED_TEXT";
    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";
    private static final String DEFAULT_FAMILLE = "SAMPLE_TEXT";
    private static final String UPDATED_FAMILLE = "UPDATED_TEXT";
    private static final String DEFAULT_GROUPE_SANGUIN = "SAMPLE_TEXT";
    private static final String UPDATED_GROUPE_SANGUIN = "UPDATED_TEXT";
    private static final String DEFAULT_INFOS_COMPL = "SAMPLE_TEXT";
    private static final String UPDATED_INFOS_COMPL = "UPDATED_TEXT";
    private static final String DEFAULT_ALLERGIE = "SAMPLE_TEXT";
    private static final String UPDATED_ALLERGIE = "UPDATED_TEXT";
    private static final String DEFAULT_STATUT = "SAMPLE_TEXT";
    private static final String UPDATED_STATUT = "UPDATED_TEXT";

    @Inject
    private PersonnelRepository personnelRepository;

    @Inject
    private PersonnelSearchRepository personnelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPersonnelMockMvc;

    private Personnel personnel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonnelResource personnelResource = new PersonnelResource();
        ReflectionTestUtils.setField(personnelResource, "personnelRepository", personnelRepository);
        ReflectionTestUtils.setField(personnelResource, "personnelSearchRepository", personnelSearchRepository);
        this.restPersonnelMockMvc = MockMvcBuilders.standaloneSetup(personnelResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        personnel = new Personnel();
        personnel.setMatricule(DEFAULT_MATRICULE);
        personnel.setNom(DEFAULT_NOM);
        personnel.setPrenom(DEFAULT_PRENOM);
        personnel.setDirection(DEFAULT_DIRECTION);
        personnel.setAge(DEFAULT_AGE);
        personnel.setSexe(DEFAULT_SEXE);
        personnel.setType(DEFAULT_TYPE);
        personnel.setFamille(DEFAULT_FAMILLE);
        personnel.setGroupeSanguin(DEFAULT_GROUPE_SANGUIN);
        personnel.setInfosCompl(DEFAULT_INFOS_COMPL);
        personnel.setAllergie(DEFAULT_ALLERGIE);
        personnel.setStatut(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    public void createPersonnel() throws Exception {
        int databaseSizeBeforeCreate = personnelRepository.findAll().size();

        // Create the Personnel

        restPersonnelMockMvc.perform(post("/api/personnels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personnel)))
                .andExpect(status().isCreated());

        // Validate the Personnel in the database
        List<Personnel> personnels = personnelRepository.findAll();
        assertThat(personnels).hasSize(databaseSizeBeforeCreate + 1);
        Personnel testPersonnel = personnels.get(personnels.size() - 1);
        assertThat(testPersonnel.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testPersonnel.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPersonnel.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testPersonnel.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testPersonnel.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPersonnel.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testPersonnel.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPersonnel.getFamille()).isEqualTo(DEFAULT_FAMILLE);
        assertThat(testPersonnel.getGroupeSanguin()).isEqualTo(DEFAULT_GROUPE_SANGUIN);
        assertThat(testPersonnel.getInfosCompl()).isEqualTo(DEFAULT_INFOS_COMPL);
        assertThat(testPersonnel.getAllergie()).isEqualTo(DEFAULT_ALLERGIE);
        assertThat(testPersonnel.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    public void getAllPersonnels() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        // Get all the personnels
        restPersonnelMockMvc.perform(get("/api/personnels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(personnel.getId().intValue())))
                .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.toString())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.toString())))
                .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE.toString())))
                .andExpect(jsonPath("$.[*].groupeSanguin").value(hasItem(DEFAULT_GROUPE_SANGUIN.toString())))
                .andExpect(jsonPath("$.[*].infosCompl").value(hasItem(DEFAULT_INFOS_COMPL.toString())))
                .andExpect(jsonPath("$.[*].allergie").value(hasItem(DEFAULT_ALLERGIE.toString())))
                .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    public void getPersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        // Get the personnel
        restPersonnelMockMvc.perform(get("/api/personnels/{id}", personnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(personnel.getId().intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.toString()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE.toString()))
            .andExpect(jsonPath("$.groupeSanguin").value(DEFAULT_GROUPE_SANGUIN.toString()))
            .andExpect(jsonPath("$.infosCompl").value(DEFAULT_INFOS_COMPL.toString()))
            .andExpect(jsonPath("$.allergie").value(DEFAULT_ALLERGIE.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPersonnel() throws Exception {
        // Get the personnel
        restPersonnelMockMvc.perform(get("/api/personnels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

		int databaseSizeBeforeUpdate = personnelRepository.findAll().size();

        // Update the personnel
        personnel.setMatricule(UPDATED_MATRICULE);
        personnel.setNom(UPDATED_NOM);
        personnel.setPrenom(UPDATED_PRENOM);
        personnel.setDirection(UPDATED_DIRECTION);
        personnel.setAge(UPDATED_AGE);
        personnel.setSexe(UPDATED_SEXE);
        personnel.setType(UPDATED_TYPE);
        personnel.setFamille(UPDATED_FAMILLE);
        personnel.setGroupeSanguin(UPDATED_GROUPE_SANGUIN);
        personnel.setInfosCompl(UPDATED_INFOS_COMPL);
        personnel.setAllergie(UPDATED_ALLERGIE);
        personnel.setStatut(UPDATED_STATUT);
        

        restPersonnelMockMvc.perform(put("/api/personnels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personnel)))
                .andExpect(status().isOk());

        // Validate the Personnel in the database
        List<Personnel> personnels = personnelRepository.findAll();
        assertThat(personnels).hasSize(databaseSizeBeforeUpdate);
        Personnel testPersonnel = personnels.get(personnels.size() - 1);
        assertThat(testPersonnel.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testPersonnel.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonnel.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonnel.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testPersonnel.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPersonnel.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testPersonnel.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPersonnel.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testPersonnel.getGroupeSanguin()).isEqualTo(UPDATED_GROUPE_SANGUIN);
        assertThat(testPersonnel.getInfosCompl()).isEqualTo(UPDATED_INFOS_COMPL);
        assertThat(testPersonnel.getAllergie()).isEqualTo(UPDATED_ALLERGIE);
        assertThat(testPersonnel.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    public void deletePersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

		int databaseSizeBeforeDelete = personnelRepository.findAll().size();

        // Get the personnel
        restPersonnelMockMvc.perform(delete("/api/personnels/{id}", personnel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Personnel> personnels = personnelRepository.findAll();
        assertThat(personnels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
