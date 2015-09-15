package cms.repository.search;

import cms.domain.Medecin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Medecin entity.
 */
public interface MedecinSearchRepository extends ElasticsearchRepository<Medecin, Long> {
}
