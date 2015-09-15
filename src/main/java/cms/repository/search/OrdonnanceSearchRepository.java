package cms.repository.search;

import cms.domain.Ordonnance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Ordonnance entity.
 */
public interface OrdonnanceSearchRepository extends ElasticsearchRepository<Ordonnance, Long> {
}
