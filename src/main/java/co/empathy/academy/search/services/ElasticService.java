package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;

import java.io.IOException;
import java.util.List;

public interface ElasticService {
    void indexIMDbDocs(List<Movie> movies, String indexName) throws IOException;

    void createIndex(String indexName, String settingsFile, String mappingsFile) throws IOException;

    AcademySearchResponse executeQuery(String indexName, Query query, Integer maxNumber, List<SortOptions> sortOptions) throws IOException;
}
