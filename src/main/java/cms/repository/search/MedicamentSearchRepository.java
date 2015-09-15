package cms.repository.search;

import cms.domain.Medicament;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Medicament entity.
 */
public interface MedicamentSearchRepository extends ElasticsearchRepository<Medicament, Long> {
}
