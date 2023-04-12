package co.empathy.academy.search.services.elastic;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.entities.AcademySearchResponse;

import java.io.IOException;
import java.util.List;

public interface ElasticRequest {

    void createIndex(String indexName, String settingsFile, String mappingFile) throws IOException;

    void deleteIndex(String indexName) throws IOException;

    void bulkIndexMovies(List<Movie> movies, String indexName) throws IOException;

    void putMapping(String indexName, String mappingFile) throws IOException;

    void putSettings(String indexName, String settingsFile) throws IOException;

    AcademySearchResponse executeQuery(String indexName, Query query, Integer maxNumber, List<SortOptions> sortOptions) throws IOException;
}
