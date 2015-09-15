package cms.repository.search;

import cms.domain.Symptome;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Symptome entity.
 */
public interface SymptomeSearchRepository extends ElasticsearchRepository<Symptome, Long> {
}
