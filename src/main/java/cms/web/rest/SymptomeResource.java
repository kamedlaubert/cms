package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Symptome;
import cms.repository.SymptomeRepository;
import cms.repository.search.SymptomeSearchRepository;
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
 * REST controller for managing Symptome.
 */
@RestController
@RequestMapping("/api")
public class SymptomeResource {

    private final Logger log = LoggerFactory.getLogger(SymptomeResource.class);

    @Inject
    private SymptomeRepository symptomeRepository;

    @Inject
    private SymptomeSearchRepository symptomeSearchRepository;

    /**
     * POST  /symptomes -> Create a new symptome.
     */
    @RequestMapping(value = "/symptomes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Symptome> create(@RequestBody Symptome symptome) throws URISyntaxException {
        log.debug("REST request to save Symptome : {}", symptome);
        if (symptome.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new symptome cannot already have an ID").body(null);
        }
        Symptome result = symptomeRepository.save(symptome);
        symptomeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/symptomes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("symptome", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /symptomes -> Updates an existing symptome.
     */
    @RequestMapping(value = "/symptomes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Symptome> update(@RequestBody Symptome symptome) throws URISyntaxException {
        log.debug("REST request to update Symptome : {}", symptome);
        if (symptome.getId() == null) {
            return create(symptome);
        }
        Symptome result = symptomeRepository.save(symptome);
        symptomeSearchRepository.save(symptome);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("symptome", symptome.getId().toString()))
                .body(result);
    }

    /**
     * GET  /symptomes -> get all the symptomes.
     */
    @RequestMapping(value = "/symptomes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Symptome> getAll() {
        log.debug("REST request to get all Symptomes");
        return symptomeRepository.findAll();
    }

    /**
     * GET  /symptomes/:id -> get the "id" symptome.
     */
    @RequestMapping(value = "/symptomes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Symptome> get(@PathVariable Long id) {
        log.debug("REST request to get Symptome : {}", id);
        return Optional.ofNullable(symptomeRepository.findOne(id))
            .map(symptome -> new ResponseEntity<>(
                symptome,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /symptomes/:id -> delete the "id" symptome.
     */
    @RequestMapping(value = "/symptomes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Symptome : {}", id);
        symptomeRepository.delete(id);
        symptomeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("symptome", id.toString())).build();
    }

    /**
     * SEARCH  /_search/symptomes/:query -> search for the symptome corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/symptomes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Symptome> search(@PathVariable String query) {
        return StreamSupport
            .stream(symptomeSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
