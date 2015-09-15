package cms.repository.search;

import cms.domain.Infirmirere;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Infirmirere entity.
 */
public interface InfirmirereSearchRepository extends ElasticsearchRepository<Infirmirere, Long> {
}
