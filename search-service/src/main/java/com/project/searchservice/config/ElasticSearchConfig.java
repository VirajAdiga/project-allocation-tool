package com.project.searchservice.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.Objects;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.project.searchservice")
public class ElasticSearchConfig {

    @Autowired
    private Environment environment;

    private String getHost(){
        return environment.getProperty("ELASTIC_SEARCH_HOST");
    }

    private Integer getPort(){
        return Integer.valueOf(Objects.requireNonNull(environment.getProperty("ELASTIC_SEARCH_PORT")));
    }

    private String getElasticSearchUrl(){
        return String.format("%s:%s", getHost(), getPort());
    }

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(getElasticSearchUrl())
                .withSocketTimeout(5000) // Set the socket timeout
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
