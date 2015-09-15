package cms.repository.search;

import cms.domain.Personnel_Externe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Personnel_Externe entity.
 */
public interface Personnel_ExterneSearchRepository extends ElasticsearchRepository<Personnel_Externe, Long> {
}
