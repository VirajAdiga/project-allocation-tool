package com.project.searchservice.repositories;

import com.project.searchservice.entities.OpeningSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OpeningSearchRepository extends ElasticsearchRepository<OpeningSearch, Long> {

    @Query("{" +
            "\"bool\": {" +
                "\"should\": [{ " +
                    "\"match\": {" +
                        "\"title\": {" +
                            "\"query\": \"?0\", " +
                            "\"fuzziness\": \"AUTO\"" +
                            "}" +
                        "}" +
                    "}, " +
                    "{\"terms\": " +
                        "{\"skills\": [\"?0\"]}" +
                        "}]" +
                    "}" +
                "}")
    Page<OpeningSearch> findByTitleOrSkillsWithFuzzy(String textToMatch, Pageable pageable);
}
