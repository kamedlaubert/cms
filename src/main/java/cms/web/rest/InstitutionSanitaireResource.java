package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.InstitutionSanitaire;
import cms.repository.InstitutionSanitaireRepository;
import cms.repository.search.InstitutionSanitaireSearchRepository;
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
 * REST controller for managing InstitutionSanitaire.
 */
@RestController
@RequestMapping("/api")
public class InstitutionSanitaireResource {

    private final Logger log = LoggerFactory.getLogger(InstitutionSanitaireResource.class);

    @Inject
    private InstitutionSanitaireRepository institutionSanitaireRepository;

    @Inject
    private InstitutionSanitaireSearchRepository institutionSanitaireSearchRepository;

    /**
     * POST  /institutionSanitaires -> Create a new institutionSanitaire.
     */
    @RequestMapping(value = "/institutionSanitaires",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstitutionSanitaire> create(@RequestBody InstitutionSanitaire institutionSanitaire) throws URISyntaxException {
        log.debug("REST request to save InstitutionSanitaire : {}", institutionSanitaire);
        if (institutionSanitaire.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new institutionSanitaire cannot already have an ID").body(null);
        }
        InstitutionSanitaire result = institutionSanitaireRepository.save(institutionSanitaire);
        institutionSanitaireSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/institutionSanitaires/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("institutionSanitaire", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /institutionSanitaires -> Updates an existing institutionSanitaire.
     */
    @RequestMapping(value = "/institutionSanitaires",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstitutionSanitaire> update(@RequestBody InstitutionSanitaire institutionSanitaire) throws URISyntaxException {
        log.debug("REST request to update InstitutionSanitaire : {}", institutionSanitaire);
        if (institutionSanitaire.getId() == null) {
            return create(institutionSanitaire);
        }
        InstitutionSanitaire result = institutionSanitaireRepository.save(institutionSanitaire);
        institutionSanitaireSearchRepository.save(institutionSanitaire);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("institutionSanitaire", institutionSanitaire.getId().toString()))
                .body(result);
    }

    /**
     * GET  /institutionSanitaires -> get all the institutionSanitaires.
     */
    @RequestMapping(value = "/institutionSanitaires",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstitutionSanitaire> getAll() {
        log.debug("REST request to get all InstitutionSanitaires");
        return institutionSanitaireRepository.findAll();
    }

    /**
     * GET  /institutionSanitaires/:id -> get the "id" institutionSanitaire.
     */
    @RequestMapping(value = "/institutionSanitaires/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstitutionSanitaire> get(@PathVariable Long id) {
        log.debug("REST request to get InstitutionSanitaire : {}", id);
        return Optional.ofNullable(institutionSanitaireRepository.findOne(id))
            .map(institutionSanitaire -> new ResponseEntity<>(
                institutionSanitaire,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /institutionSanitaires/:id -> delete the "id" institutionSanitaire.
     */
    @RequestMapping(value = "/institutionSanitaires/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete InstitutionSanitaire : {}", id);
        institutionSanitaireRepository.delete(id);
        institutionSanitaireSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("institutionSanitaire", id.toString())).build();
    }

    /**
     * SEARCH  /_search/institutionSanitaires/:query -> search for the institutionSanitaire corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/institutionSanitaires/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstitutionSanitaire> search(@PathVariable String query) {
        return StreamSupport
            .stream(institutionSanitaireSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
