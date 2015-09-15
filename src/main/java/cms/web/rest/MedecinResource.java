package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.Medecin;
import cms.repository.MedecinRepository;
import cms.repository.search.MedecinSearchRepository;
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
 * REST controller for managing Medecin.
 */
@RestController
@RequestMapping("/api")
public class MedecinResource {

    private final Logger log = LoggerFactory.getLogger(MedecinResource.class);

    @Inject
    private MedecinRepository medecinRepository;

    @Inject
    private MedecinSearchRepository medecinSearchRepository;

    /**
     * POST  /medecins -> Create a new medecin.
     */
    @RequestMapping(value = "/medecins",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medecin> create(@RequestBody Medecin medecin) throws URISyntaxException {
        log.debug("REST request to save Medecin : {}", medecin);
        if (medecin.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new medecin cannot already have an ID").body(null);
        }
        Medecin result = medecinRepository.save(medecin);
        medecinSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medecins/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("medecin", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /medecins -> Updates an existing medecin.
     */
    @RequestMapping(value = "/medecins",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medecin> update(@RequestBody Medecin medecin) throws URISyntaxException {
        log.debug("REST request to update Medecin : {}", medecin);
        if (medecin.getId() == null) {
            return create(medecin);
        }
        Medecin result = medecinRepository.save(medecin);
        medecinSearchRepository.save(medecin);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("medecin", medecin.getId().toString()))
                .body(result);
    }

    /**
     * GET  /medecins -> get all the medecins.
     */
    @RequestMapping(value = "/medecins",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Medecin> getAll() {
        log.debug("REST request to get all Medecins");
        return medecinRepository.findAll();
    }

    /**
     * GET  /medecins/:id -> get the "id" medecin.
     */
    @RequestMapping(value = "/medecins/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medecin> get(@PathVariable Long id) {
        log.debug("REST request to get Medecin : {}", id);
        return Optional.ofNullable(medecinRepository.findOne(id))
            .map(medecin -> new ResponseEntity<>(
                medecin,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /medecins/:id -> delete the "id" medecin.
     */
    @RequestMapping(value = "/medecins/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Medecin : {}", id);
        medecinRepository.delete(id);
        medecinSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("medecin", id.toString())).build();
    }

    /**
     * SEARCH  /_search/medecins/:query -> search for the medecin corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/medecins/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Medecin> search(@PathVariable String query) {
        return StreamSupport
            .stream(medecinSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
