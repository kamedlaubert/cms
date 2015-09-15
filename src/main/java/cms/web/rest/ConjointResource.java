package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Conjoint;
import cms.repository.ConjointRepository;
import cms.repository.search.ConjointSearchRepository;
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
 * REST controller for managing Conjoint.
 */
@RestController
@RequestMapping("/api")
public class ConjointResource {

    private final Logger log = LoggerFactory.getLogger(ConjointResource.class);

    @Inject
    private ConjointRepository conjointRepository;

    @Inject
    private ConjointSearchRepository conjointSearchRepository;

    /**
     * POST  /conjoints -> Create a new conjoint.
     */
    @RequestMapping(value = "/conjoints",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Conjoint> create(@RequestBody Conjoint conjoint) throws URISyntaxException {
        log.debug("REST request to save Conjoint : {}", conjoint);
        if (conjoint.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new conjoint cannot already have an ID").body(null);
        }
        Conjoint result = conjointRepository.save(conjoint);
        conjointSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/conjoints/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("conjoint", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /conjoints -> Updates an existing conjoint.
     */
    @RequestMapping(value = "/conjoints",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Conjoint> update(@RequestBody Conjoint conjoint) throws URISyntaxException {
        log.debug("REST request to update Conjoint : {}", conjoint);
        if (conjoint.getId() == null) {
            return create(conjoint);
        }
        Conjoint result = conjointRepository.save(conjoint);
        conjointSearchRepository.save(conjoint);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("conjoint", conjoint.getId().toString()))
                .body(result);
    }

    /**
     * GET  /conjoints -> get all the conjoints.
     */
    @RequestMapping(value = "/conjoints",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Conjoint> getAll() {
        log.debug("REST request to get all Conjoints");
        return conjointRepository.findAll();
    }

    /**
     * GET  /conjoints/:id -> get the "id" conjoint.
     */
    @RequestMapping(value = "/conjoints/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Conjoint> get(@PathVariable Long id) {
        log.debug("REST request to get Conjoint : {}", id);
        return Optional.ofNullable(conjointRepository.findOne(id))
            .map(conjoint -> new ResponseEntity<>(
                conjoint,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /conjoints/:id -> delete the "id" conjoint.
     */
    @RequestMapping(value = "/conjoints/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Conjoint : {}", id);
        conjointRepository.delete(id);
        conjointSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("conjoint", id.toString())).build();
    }

    /**
     * SEARCH  /_search/conjoints/:query -> search for the conjoint corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/conjoints/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Conjoint> search(@PathVariable String query) {
        return StreamSupport
            .stream(conjointSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
