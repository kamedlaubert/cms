package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Infirmirere;
import cms.repository.InfirmirereRepository;
import cms.repository.search.InfirmirereSearchRepository;
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
 * REST controller for managing Infirmirere.
 */
@RestController
@RequestMapping("/api")
public class InfirmirereResource {

    private final Logger log = LoggerFactory.getLogger(InfirmirereResource.class);

    @Inject
    private InfirmirereRepository infirmirereRepository;

    @Inject
    private InfirmirereSearchRepository infirmirereSearchRepository;

    /**
     * POST  /infirmireres -> Create a new infirmirere.
     */
    @RequestMapping(value = "/infirmireres",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Infirmirere> create(@RequestBody Infirmirere infirmirere) throws URISyntaxException {
        log.debug("REST request to save Infirmirere : {}", infirmirere);
        if (infirmirere.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new infirmirere cannot already have an ID").body(null);
        }
        Infirmirere result = infirmirereRepository.save(infirmirere);
        infirmirereSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/infirmireres/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("infirmirere", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /infirmireres -> Updates an existing infirmirere.
     */
    @RequestMapping(value = "/infirmireres",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Infirmirere> update(@RequestBody Infirmirere infirmirere) throws URISyntaxException {
        log.debug("REST request to update Infirmirere : {}", infirmirere);
        if (infirmirere.getId() == null) {
            return create(infirmirere);
        }
        Infirmirere result = infirmirereRepository.save(infirmirere);
        infirmirereSearchRepository.save(infirmirere);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("infirmirere", infirmirere.getId().toString()))
                .body(result);
    }

    /**
     * GET  /infirmireres -> get all the infirmireres.
     */
    @RequestMapping(value = "/infirmireres",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Infirmirere> getAll() {
        log.debug("REST request to get all Infirmireres");
        return infirmirereRepository.findAll();
    }

    /**
     * GET  /infirmireres/:id -> get the "id" infirmirere.
     */
    @RequestMapping(value = "/infirmireres/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Infirmirere> get(@PathVariable Long id) {
        log.debug("REST request to get Infirmirere : {}", id);
        return Optional.ofNullable(infirmirereRepository.findOne(id))
            .map(infirmirere -> new ResponseEntity<>(
                infirmirere,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /infirmireres/:id -> delete the "id" infirmirere.
     */
    @RequestMapping(value = "/infirmireres/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Infirmirere : {}", id);
        infirmirereRepository.delete(id);
        infirmirereSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("infirmirere", id.toString())).build();
    }

    /**
     * SEARCH  /_search/infirmireres/:query -> search for the infirmirere corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/infirmireres/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Infirmirere> search(@PathVariable String query) {
        return StreamSupport
            .stream(infirmirereSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
