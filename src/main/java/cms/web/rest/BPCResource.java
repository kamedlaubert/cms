package cms.web.rest;

import com.codahale.metrics.annotation.Timed;
import cms.domain.BPC;
import cms.repository.BPCRepository;
import cms.repository.search.BPCSearchRepository;
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
 * REST controller for managing BPC.
 */
@RestController
@RequestMapping("/api")
public class BPCResource {

    private final Logger log = LoggerFactory.getLogger(BPCResource.class);

    @Inject
    private BPCRepository bPCRepository;

    @Inject
    private BPCSearchRepository bPCSearchRepository;

    /**
     * POST  /bPCs -> Create a new bPC.
     */
    @RequestMapping(value = "/bPCs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BPC> create(@RequestBody BPC bPC) throws URISyntaxException {
        log.debug("REST request to save BPC : {}", bPC);
        if (bPC.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new bPC cannot already have an ID").body(null);
        }
        BPC result = bPCRepository.save(bPC);
        bPCSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bPCs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("bPC", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /bPCs -> Updates an existing bPC.
     */
    @RequestMapping(value = "/bPCs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BPC> update(@RequestBody BPC bPC) throws URISyntaxException {
        log.debug("REST request to update BPC : {}", bPC);
        if (bPC.getId() == null) {
            return create(bPC);
        }
        BPC result = bPCRepository.save(bPC);
        bPCSearchRepository.save(bPC);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("bPC", bPC.getId().toString()))
                .body(result);
    }

    /**
     * GET  /bPCs -> get all the bPCs.
     */
    @RequestMapping(value = "/bPCs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BPC> getAll(@RequestParam(required = false) String filter) {
        if ("hospitalisation-is-null".equals(filter)) {
            log.debug("REST request to get all BPCs where hospitalisation is null");
            return StreamSupport
                .stream(bPCRepository.findAll().spliterator(), false)
                .filter(bPC -> bPC.getHospitalisation() == null)
                .collect(Collectors.toList());
        }

        log.debug("REST request to get all BPCs");
        return bPCRepository.findAll();
    }

    /**
     * GET  /bPCs/:id -> get the "id" bPC.
     */
    @RequestMapping(value = "/bPCs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BPC> get(@PathVariable Long id) {
        log.debug("REST request to get BPC : {}", id);
        return Optional.ofNullable(bPCRepository.findOne(id))
            .map(bPC -> new ResponseEntity<>(
                bPC,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bPCs/:id -> delete the "id" bPC.
     */
    @RequestMapping(value = "/bPCs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete BPC : {}", id);
        bPCRepository.delete(id);
        bPCSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bPC", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bPCs/:query -> search for the bPC corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/bPCs/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BPC> search(@PathVariable String query) {
        return StreamSupport
            .stream(bPCSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
