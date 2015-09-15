package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Personnel_Externe;
import cms.repository.Personnel_ExterneRepository;
import cms.repository.search.Personnel_ExterneSearchRepository;
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
 * REST controller for managing Personnel_Externe.
 */
@RestController
@RequestMapping("/api")
public class Personnel_ExterneResource {

    private final Logger log = LoggerFactory.getLogger(Personnel_ExterneResource.class);

    @Inject
    private Personnel_ExterneRepository personnel_ExterneRepository;

    @Inject
    private Personnel_ExterneSearchRepository personnel_ExterneSearchRepository;

    /**
     * POST  /personnel_Externes -> Create a new personnel_Externe.
     */
    @RequestMapping(value = "/personnel_Externes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personnel_Externe> create(@RequestBody Personnel_Externe personnel_Externe) throws URISyntaxException {
        log.debug("REST request to save Personnel_Externe : {}", personnel_Externe);
        if (personnel_Externe.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new personnel_Externe cannot already have an ID").body(null);
        }
        Personnel_Externe result = personnel_ExterneRepository.save(personnel_Externe);
        personnel_ExterneSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/personnel_Externes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("personnel_Externe", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /personnel_Externes -> Updates an existing personnel_Externe.
     */
    @RequestMapping(value = "/personnel_Externes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personnel_Externe> update(@RequestBody Personnel_Externe personnel_Externe) throws URISyntaxException {
        log.debug("REST request to update Personnel_Externe : {}", personnel_Externe);
        if (personnel_Externe.getId() == null) {
            return create(personnel_Externe);
        }
        Personnel_Externe result = personnel_ExterneRepository.save(personnel_Externe);
        personnel_ExterneSearchRepository.save(personnel_Externe);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("personnel_Externe", personnel_Externe.getId().toString()))
                .body(result);
    }

    /**
     * GET  /personnel_Externes -> get all the personnel_Externes.
     */
    @RequestMapping(value = "/personnel_Externes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Personnel_Externe> getAll() {
        log.debug("REST request to get all Personnel_Externes");
        return personnel_ExterneRepository.findAll();
    }

    /**
     * GET  /personnel_Externes/:id -> get the "id" personnel_Externe.
     */
    @RequestMapping(value = "/personnel_Externes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personnel_Externe> get(@PathVariable Long id) {
        log.debug("REST request to get Personnel_Externe : {}", id);
        return Optional.ofNullable(personnel_ExterneRepository.findOne(id))
            .map(personnel_Externe -> new ResponseEntity<>(
                personnel_Externe,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /personnel_Externes/:id -> delete the "id" personnel_Externe.
     */
    @RequestMapping(value = "/personnel_Externes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Personnel_Externe : {}", id);
        personnel_ExterneRepository.delete(id);
        personnel_ExterneSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("personnel_Externe", id.toString())).build();
    }

    /**
     * SEARCH  /_search/personnel_Externes/:query -> search for the personnel_Externe corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/personnel_Externes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Personnel_Externe> search(@PathVariable String query) {
        return StreamSupport
            .stream(personnel_ExterneSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
