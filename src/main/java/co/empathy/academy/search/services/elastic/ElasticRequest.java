package co.empathy.academy.search.services.elastic;

import co.empathy.academy.search.entities.Movie;

import java.io.IOException;
import java.util.List;

public interface ElasticRequest {

    void createIndex(String indexName, String settingsFile, String mappingFile) throws IOException;

    void bulkIndexMovies(List<Movie> movies, String indexName) throws IOException;

    void putMapping(String indexName, String mappingFile) throws IOException;

    void putSettings(String indexName, String settingsFile) throws IOException;
}
