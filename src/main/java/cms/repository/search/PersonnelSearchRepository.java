package cms.repository.search;

import cms.domain.Personnel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Personnel entity.
 */
public interface PersonnelSearchRepository extends ElasticsearchRepository<Personnel, Long> {
}
