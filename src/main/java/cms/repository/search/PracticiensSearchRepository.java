package cms.repository.search;

import cms.domain.Practiciens;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Practiciens entity.
 */
public interface PracticiensSearchRepository extends ElasticsearchRepository<Practiciens, Long> {
}
