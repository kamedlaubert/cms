package cms.repository.search;

import cms.domain.Examens;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Examens entity.
 */
public interface ExamensSearchRepository extends ElasticsearchRepository<Examens, Long> {
}
