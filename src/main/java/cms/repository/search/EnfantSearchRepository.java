package cms.repository.search;

import cms.domain.Enfant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Enfant entity.
 */
public interface EnfantSearchRepository extends ElasticsearchRepository<Enfant, Long> {
}
