package cms.repository.search;

import cms.domain.BPC;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BPC entity.
 */
public interface BPCSearchRepository extends ElasticsearchRepository<BPC, Long> {
}
