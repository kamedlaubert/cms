package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Personnel;
import cms.repository.PersonnelRepository;
import cms.repository.search.PersonnelSearchRepository;
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
 * REST controller for managing Personnel.
 */
@RestController
@RequestMapping("/api")
public class PersonnelResource {

    private final Logger log = LoggerFactory.getLogger(PersonnelResource.class);

    @Inject
    private PersonnelRepository personnelRepository;

    @Inject
    private PersonnelSearchRepository personnelSearchRepository;

    /**
     * POST  /personnels -> Create a new personnel.
     */
    @RequestMapping(value = "/personnels",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personnel> create(@RequestBody Personnel personnel) throws URISyntaxException {
        log.debug("REST request to save Personnel : {}", personnel);
        if (personnel.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new personnel cannot already have an ID").body(null);
        }
        Personnel result = personnelRepository.save(personnel);
        personnelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/personnels/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("personnel", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /personnels -> Updates an existing personnel.
     */
    @RequestMapping(value = "/personnels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personnel> update(@RequestBody Personnel personnel) throws URISyntaxException {
        log.debug("REST request to update Personnel : {}", personnel);
        if (personnel.getId() == null) {
            return create(personnel);
        }
        Personnel result = personnelRepository.save(personnel);
        personnelSearchRepository.save(personnel);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("personnel", personnel.getId().toString()))
                .body(result);
    }

    /**
     * GET  /personnels -> get all the personnels.
     */
    @RequestMapping(value = "/personnels",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Personnel> getAll() {
        log.debug("REST request to get all Personnels");
        return personnelRepository.findAll();
    }

    /**
     * GET  /personnels/:id -> get the "id" personnel.
     */
    @RequestMapping(value = "/personnels/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personnel> get(@PathVariable Long id) {
        log.debug("REST request to get Personnel : {}", id);
        return Optional.ofNullable(personnelRepository.findOne(id))
            .map(personnel -> new ResponseEntity<>(
                personnel,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /personnels/:id -> delete the "id" personnel.
     */
    @RequestMapping(value = "/personnels/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Personnel : {}", id);
        personnelRepository.delete(id);
        personnelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("personnel", id.toString())).build();
    }

    /**
     * SEARCH  /_search/personnels/:query -> search for the personnel corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/personnels/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Personnel> search(@PathVariable String query) {
        return StreamSupport
            .stream(personnelSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
