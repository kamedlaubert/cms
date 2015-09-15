package cms.repository.search;

import cms.domain.Hospitalisation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Hospitalisation entity.
 */
public interface HospitalisationSearchRepository extends ElasticsearchRepository<Hospitalisation, Long> {
}
