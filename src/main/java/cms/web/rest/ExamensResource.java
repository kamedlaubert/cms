package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Examens;
import cms.repository.ExamensRepository;
import cms.repository.search.ExamensSearchRepository;
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
 * REST controller for managing Examens.
 */
@RestController
@RequestMapping("/api")
public class ExamensResource {

    private final Logger log = LoggerFactory.getLogger(ExamensResource.class);

    @Inject
    private ExamensRepository examensRepository;

    @Inject
    private ExamensSearchRepository examensSearchRepository;

    /**
     * POST  /examenss -> Create a new examens.
     */
    @RequestMapping(value = "/examenss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Examens> create(@RequestBody Examens examens) throws URISyntaxException {
        log.debug("REST request to save Examens : {}", examens);
        if (examens.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new examens cannot already have an ID").body(null);
        }
        Examens result = examensRepository.save(examens);
        examensSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/examenss/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("examens", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /examenss -> Updates an existing examens.
     */
    @RequestMapping(value = "/examenss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Examens> update(@RequestBody Examens examens) throws URISyntaxException {
        log.debug("REST request to update Examens : {}", examens);
        if (examens.getId() == null) {
            return create(examens);
        }
        Examens result = examensRepository.save(examens);
        examensSearchRepository.save(examens);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("examens", examens.getId().toString()))
                .body(result);
    }

    /**
     * GET  /examenss -> get all the examenss.
     */
    @RequestMapping(value = "/examenss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Examens> getAll() {
        log.debug("REST request to get all Examenss");
        return examensRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /examenss/:id -> get the "id" examens.
     */
    @RequestMapping(value = "/examenss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Examens> get(@PathVariable Long id) {
        log.debug("REST request to get Examens : {}", id);
        return Optional.ofNullable(examensRepository.findOneWithEagerRelationships(id))
            .map(examens -> new ResponseEntity<>(
                examens,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /examenss/:id -> delete the "id" examens.
     */
    @RequestMapping(value = "/examenss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Examens : {}", id);
        examensRepository.delete(id);
        examensSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("examens", id.toString())).build();
    }

    /**
     * SEARCH  /_search/examenss/:query -> search for the examens corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/examenss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Examens> search(@PathVariable String query) {
        return StreamSupport
            .stream(examensSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
