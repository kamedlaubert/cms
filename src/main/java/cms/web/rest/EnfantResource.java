package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Enfant;
import cms.repository.EnfantRepository;
import cms.repository.search.EnfantSearchRepository;
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
 * REST controller for managing Enfant.
 */
@RestController
@RequestMapping("/api")
public class EnfantResource {

    private final Logger log = LoggerFactory.getLogger(EnfantResource.class);

    @Inject
    private EnfantRepository enfantRepository;

    @Inject
    private EnfantSearchRepository enfantSearchRepository;

    /**
     * POST  /enfants -> Create a new enfant.
     */
    @RequestMapping(value = "/enfants",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enfant> create(@RequestBody Enfant enfant) throws URISyntaxException {
        log.debug("REST request to save Enfant : {}", enfant);
        if (enfant.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new enfant cannot already have an ID").body(null);
        }
        Enfant result = enfantRepository.save(enfant);
        enfantSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/enfants/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("enfant", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /enfants -> Updates an existing enfant.
     */
    @RequestMapping(value = "/enfants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enfant> update(@RequestBody Enfant enfant) throws URISyntaxException {
        log.debug("REST request to update Enfant : {}", enfant);
        if (enfant.getId() == null) {
            return create(enfant);
        }
        Enfant result = enfantRepository.save(enfant);
        enfantSearchRepository.save(enfant);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("enfant", enfant.getId().toString()))
                .body(result);
    }

    /**
     * GET  /enfants -> get all the enfants.
     */
    @RequestMapping(value = "/enfants",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Enfant> getAll() {
        log.debug("REST request to get all Enfants");
        return enfantRepository.findAll();
    }

    /**
     * GET  /enfants/:id -> get the "id" enfant.
     */
    @RequestMapping(value = "/enfants/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enfant> get(@PathVariable Long id) {
        log.debug("REST request to get Enfant : {}", id);
        return Optional.ofNullable(enfantRepository.findOne(id))
            .map(enfant -> new ResponseEntity<>(
                enfant,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /enfants/:id -> delete the "id" enfant.
     */
    @RequestMapping(value = "/enfants/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Enfant : {}", id);
        enfantRepository.delete(id);
        enfantSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("enfant", id.toString())).build();
    }

    /**
     * SEARCH  /_search/enfants/:query -> search for the enfant corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/enfants/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Enfant> search(@PathVariable String query) {
        return StreamSupport
            .stream(enfantSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
