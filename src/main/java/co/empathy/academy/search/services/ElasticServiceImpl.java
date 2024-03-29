package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.services.elastic.ElasticRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElasticServiceImpl implements ElasticService {

    private final ElasticRequest elasticRequest;

    public ElasticServiceImpl(ElasticRequest elasticRequest) {
        this.elasticRequest = elasticRequest;
    }

    @Override
    public void indexIMDbDocs(List<Movie> movies, String indexName) throws IOException {
        elasticRequest.bulkIndexMovies(movies, indexName);
    }

    @Override
    public void createIndex(String indexName, String settingsFile, String mappingsFile) throws IOException {
        boolean indexExists = elasticRequest.checkIndexExistence(indexName);

        if (indexExists) {
            elasticRequest.deleteIndex(indexName);
        }

        elasticRequest.createIndex(indexName, settingsFile, mappingsFile);
    }

    @Override
    public AcademySearchResponse executeQuery(String indexName, Query query, Integer maxNumber) throws IOException {
        return elasticRequest.executeQuery(indexName, query, maxNumber);
    }

    @Override
    public AcademySearchResponse executeQuery(String indexName, Query query, Integer maxNumber, SortOptions sortOptions) throws IOException {
        return elasticRequest.executeQuery(indexName, query, maxNumber, sortOptions);
    }

    @Override
    public SearchResponse executeQuery(String indexName, Query query, Integer maxNumber, Map<String, Aggregation> aggregations) throws IOException {
        return elasticRequest.executeQuery(indexName, query, maxNumber, aggregations);
    }
}
