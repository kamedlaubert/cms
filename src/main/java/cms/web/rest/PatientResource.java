package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Patient;
import cms.repository.PatientRepository;
import cms.repository.search.PatientSearchRepository;
import cms.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Patient.
 */
@RestController
@RequestMapping("/api")
public class PatientResource {

    private final Logger log = LoggerFactory.getLogger(PatientResource.class);

    @Inject
    private PatientRepository patientRepository;

    @Inject
    private PatientSearchRepository patientSearchRepository;

    /**
     * POST  /patients -> Create a new patient.
     */
    @RequestMapping(value = "/patients",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Patient> create(@RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patient);
        if (patient.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new patient cannot already have an ID").body(null);
        }
        Patient result = patientRepository.save(patient);
        patientSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/patients/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("patient", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /patients -> Updates an existing patient.
     */
    @RequestMapping(value = "/patients",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Patient> update(@RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to update Patient : {}", patient);
        if (patient.getId() == null) {
            return create(patient);
        }
        Patient result = patientRepository.save(patient);
        patientSearchRepository.save(patient);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("patient", patient.getId().toString()))
                .body(result);
    }

    /**
     * GET  /patients -> get all the patients.
     */
    @RequestMapping(value = "/patients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Patient> getAll() {
        log.debug("REST request to get all Patients");
        return patientRepository.findAll();
    }

    /**
     * GET  /patients/:id -> get the "id" patient.
     */
    @RequestMapping(value = "/patients/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Patient> get(@PathVariable Long id) {
        log.debug("REST request to get Patient : {}", id);
        return Optional.ofNullable(patientRepository.findOne(id))
            .map(patient -> new ResponseEntity<>(
                patient,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /patients/:id -> delete the "id" patient.
     */
    @RequestMapping(value = "/patients/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Patient : {}", id);
        patientRepository.delete(id);
        patientSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("patient", id.toString())).build();
    }

    /**
     * SEARCH  /_search/patients/:query -> search for the patient corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/patients/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Patient> search(@PathVariable String query) {
        return StreamSupport
            .stream(patientSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
