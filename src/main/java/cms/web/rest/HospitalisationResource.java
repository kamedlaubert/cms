package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Hospitalisation;
import cms.repository.HospitalisationRepository;
import cms.repository.search.HospitalisationSearchRepository;
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
 * REST controller for managing Hospitalisation.
 */
@RestController
@RequestMapping("/api")
public class HospitalisationResource {

    private final Logger log = LoggerFactory.getLogger(HospitalisationResource.class);

    @Inject
    private HospitalisationRepository hospitalisationRepository;

    @Inject
    private HospitalisationSearchRepository hospitalisationSearchRepository;

    /**
     * POST  /hospitalisations -> Create a new hospitalisation.
     */
    @RequestMapping(value = "/hospitalisations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hospitalisation> create(@RequestBody Hospitalisation hospitalisation) throws URISyntaxException {
        log.debug("REST request to save Hospitalisation : {}", hospitalisation);
        if (hospitalisation.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hospitalisation cannot already have an ID").body(null);
        }
        Hospitalisation result = hospitalisationRepository.save(hospitalisation);
        hospitalisationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hospitalisations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("hospitalisation", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /hospitalisations -> Updates an existing hospitalisation.
     */
    @RequestMapping(value = "/hospitalisations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hospitalisation> update(@RequestBody Hospitalisation hospitalisation) throws URISyntaxException {
        log.debug("REST request to update Hospitalisation : {}", hospitalisation);
        if (hospitalisation.getId() == null) {
            return create(hospitalisation);
        }
        Hospitalisation result = hospitalisationRepository.save(hospitalisation);
        hospitalisationSearchRepository.save(hospitalisation);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("hospitalisation", hospitalisation.getId().toString()))
                .body(result);
    }

    /**
     * GET  /hospitalisations -> get all the hospitalisations.
     */
    @RequestMapping(value = "/hospitalisations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Hospitalisation> getAll() {
        log.debug("REST request to get all Hospitalisations");
        return hospitalisationRepository.findAll();
    }

    /**
     * GET  /hospitalisations/:id -> get the "id" hospitalisation.
     */
    @RequestMapping(value = "/hospitalisations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hospitalisation> get(@PathVariable Long id) {
        log.debug("REST request to get Hospitalisation : {}", id);
        return Optional.ofNullable(hospitalisationRepository.findOne(id))
            .map(hospitalisation -> new ResponseEntity<>(
                hospitalisation,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hospitalisations/:id -> delete the "id" hospitalisation.
     */
    @RequestMapping(value = "/hospitalisations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Hospitalisation : {}", id);
        hospitalisationRepository.delete(id);
        hospitalisationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hospitalisation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/hospitalisations/:query -> search for the hospitalisation corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/hospitalisations/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Hospitalisation> search(@PathVariable String query) {
        return StreamSupport
            .stream(hospitalisationSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
