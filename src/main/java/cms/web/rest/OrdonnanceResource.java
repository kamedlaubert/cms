package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Ordonnance;
import cms.repository.OrdonnanceRepository;
import cms.repository.search.OrdonnanceSearchRepository;
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
 * REST controller for managing Ordonnance.
 */
@RestController
@RequestMapping("/api")
public class OrdonnanceResource {

    private final Logger log = LoggerFactory.getLogger(OrdonnanceResource.class);

    @Inject
    private OrdonnanceRepository ordonnanceRepository;

    @Inject
    private OrdonnanceSearchRepository ordonnanceSearchRepository;

    /**
     * POST  /ordonnances -> Create a new ordonnance.
     */
    @RequestMapping(value = "/ordonnances",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ordonnance> create(@RequestBody Ordonnance ordonnance) throws URISyntaxException {
        log.debug("REST request to save Ordonnance : {}", ordonnance);
        if (ordonnance.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new ordonnance cannot already have an ID").body(null);
        }
        Ordonnance result = ordonnanceRepository.save(ordonnance);
        ordonnanceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ordonnances/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("ordonnance", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /ordonnances -> Updates an existing ordonnance.
     */
    @RequestMapping(value = "/ordonnances",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ordonnance> update(@RequestBody Ordonnance ordonnance) throws URISyntaxException {
        log.debug("REST request to update Ordonnance : {}", ordonnance);
        if (ordonnance.getId() == null) {
            return create(ordonnance);
        }
        Ordonnance result = ordonnanceRepository.save(ordonnance);
        ordonnanceSearchRepository.save(ordonnance);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("ordonnance", ordonnance.getId().toString()))
                .body(result);
    }

    /**
     * GET  /ordonnances -> get all the ordonnances.
     */
    @RequestMapping(value = "/ordonnances",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ordonnance> getAll() {
        log.debug("REST request to get all Ordonnances");
        return ordonnanceRepository.findAll();
    }

    /**
     * GET  /ordonnances/:id -> get the "id" ordonnance.
     */
    @RequestMapping(value = "/ordonnances/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ordonnance> get(@PathVariable Long id) {
        log.debug("REST request to get Ordonnance : {}", id);
        return Optional.ofNullable(ordonnanceRepository.findOne(id))
            .map(ordonnance -> new ResponseEntity<>(
                ordonnance,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ordonnances/:id -> delete the "id" ordonnance.
     */
    @RequestMapping(value = "/ordonnances/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Ordonnance : {}", id);
        ordonnanceRepository.delete(id);
        ordonnanceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ordonnance", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ordonnances/:query -> search for the ordonnance corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/ordonnances/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ordonnance> search(@PathVariable String query) {
        return StreamSupport
            .stream(ordonnanceSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
