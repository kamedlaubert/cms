package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Practiciens;
import cms.repository.PracticiensRepository;
import cms.repository.search.PracticiensSearchRepository;
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
 * REST controller for managing Practiciens.
 */
@RestController
@RequestMapping("/api")
public class PracticiensResource {

    private final Logger log = LoggerFactory.getLogger(PracticiensResource.class);

    @Inject
    private PracticiensRepository practiciensRepository;

    @Inject
    private PracticiensSearchRepository practiciensSearchRepository;

    /**
     * POST  /practicienss -> Create a new practiciens.
     */
    @RequestMapping(value = "/practicienss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Practiciens> create(@RequestBody Practiciens practiciens) throws URISyntaxException {
        log.debug("REST request to save Practiciens : {}", practiciens);
        if (practiciens.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new practiciens cannot already have an ID").body(null);
        }
        Practiciens result = practiciensRepository.save(practiciens);
        practiciensSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/practicienss/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("practiciens", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /practicienss -> Updates an existing practiciens.
     */
    @RequestMapping(value = "/practicienss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Practiciens> update(@RequestBody Practiciens practiciens) throws URISyntaxException {
        log.debug("REST request to update Practiciens : {}", practiciens);
        if (practiciens.getId() == null) {
            return create(practiciens);
        }
        Practiciens result = practiciensRepository.save(practiciens);
        practiciensSearchRepository.save(practiciens);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("practiciens", practiciens.getId().toString()))
                .body(result);
    }

    /**
     * GET  /practicienss -> get all the practicienss.
     */
    @RequestMapping(value = "/practicienss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Practiciens> getAll() {
        log.debug("REST request to get all Practicienss");
        return practiciensRepository.findAll();
    }

    /**
     * GET  /practicienss/:id -> get the "id" practiciens.
     */
    @RequestMapping(value = "/practicienss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Practiciens> get(@PathVariable Long id) {
        log.debug("REST request to get Practiciens : {}", id);
        return Optional.ofNullable(practiciensRepository.findOne(id))
            .map(practiciens -> new ResponseEntity<>(
                practiciens,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /practicienss/:id -> delete the "id" practiciens.
     */
    @RequestMapping(value = "/practicienss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Practiciens : {}", id);
        practiciensRepository.delete(id);
        practiciensSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("practiciens", id.toString())).build();
    }

    /**
     * SEARCH  /_search/practicienss/:query -> search for the practiciens corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/practicienss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Practiciens> search(@PathVariable String query) {
        return StreamSupport
            .stream(practiciensSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
