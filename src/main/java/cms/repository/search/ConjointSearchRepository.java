package cms.repository.search;

import cms.domain.Conjoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Conjoint entity.
 */
public interface ConjointSearchRepository extends ElasticsearchRepository<Conjoint, Long> {
}
