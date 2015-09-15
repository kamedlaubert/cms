package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Medicament;
import cms.repository.MedicamentRepository;
import cms.repository.search.MedicamentSearchRepository;
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
 * REST controller for managing Medicament.
 */
@RestController
@RequestMapping("/api")
public class MedicamentResource {

    private final Logger log = LoggerFactory.getLogger(MedicamentResource.class);

    @Inject
    private MedicamentRepository medicamentRepository;

    @Inject
    private MedicamentSearchRepository medicamentSearchRepository;

    /**
     * POST  /medicaments -> Create a new medicament.
     */
    @RequestMapping(value = "/medicaments",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicament> create(@RequestBody Medicament medicament) throws URISyntaxException {
        log.debug("REST request to save Medicament : {}", medicament);
        if (medicament.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new medicament cannot already have an ID").body(null);
        }
        Medicament result = medicamentRepository.save(medicament);
        medicamentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medicaments/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("medicament", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /medicaments -> Updates an existing medicament.
     */
    @RequestMapping(value = "/medicaments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicament> update(@RequestBody Medicament medicament) throws URISyntaxException {
        log.debug("REST request to update Medicament : {}", medicament);
        if (medicament.getId() == null) {
            return create(medicament);
        }
        Medicament result = medicamentRepository.save(medicament);
        medicamentSearchRepository.save(medicament);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("medicament", medicament.getId().toString()))
                .body(result);
    }

    /**
     * GET  /medicaments -> get all the medicaments.
     */
    @RequestMapping(value = "/medicaments",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Medicament> getAll() {
        log.debug("REST request to get all Medicaments");
        return medicamentRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /medicaments/:id -> get the "id" medicament.
     */
    @RequestMapping(value = "/medicaments/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicament> get(@PathVariable Long id) {
        log.debug("REST request to get Medicament : {}", id);
        return Optional.ofNullable(medicamentRepository.findOneWithEagerRelationships(id))
            .map(medicament -> new ResponseEntity<>(
                medicament,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /medicaments/:id -> delete the "id" medicament.
     */
    @RequestMapping(value = "/medicaments/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Medicament : {}", id);
        medicamentRepository.delete(id);
        medicamentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("medicament", id.toString())).build();
    }

    /**
     * SEARCH  /_search/medicaments/:query -> search for the medicament corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/medicaments/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Medicament> search(@PathVariable String query) {
        return StreamSupport
            .stream(medicamentSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
