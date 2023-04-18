package co.empathy.academy.search.services.elastic;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.entities.AcademySearchResponse;

import java.io.IOException;
import java.util.List;

public interface ElasticRequest {

    /**
     * Creates a new index in ElasticSearch and configures it with mappings and settings (analyzer, filters...)
     * @param indexName name of the index to be created
     * @param settingsFile file that contains settings configuration
     * @param mappingFile file that contains mapping configuration
     * @throws IOException
     */
    void createIndex(String indexName, String settingsFile, String mappingFile) throws IOException;

    /**
     * Deletes an index in ElasticSearch
     * @param indexName name of the index to be deleted
     * @throws IOException
     */
    void deleteIndex(String indexName) throws IOException;

    /**
     * Returns true if the provided index exists in the ElasticSearch client and false otherwise.
     * @param indexName name of the index
     * @return
     * @throws IOException
     */
    boolean checkIndexExistence(String indexName) throws IOException;

    /**
     * Bulk indexes the provided movie list in the given index
     * @param movies list of movies to be indexed
     * @param indexName name of the index where movies should be indexed
     * @throws IOException
     */
    void bulkIndexMovies(List<Movie> movies, String indexName) throws IOException;

    /**
     * Configures the provided index with the given mappings
     * @param indexName name of the index
     * @param mappingFile file that contains mappings
     * @throws IOException
     */
    void putMapping(String indexName, String mappingFile) throws IOException;

    /**
     * Configures the provided index with the given settings (analyzer, filters...)
     * @param indexName name of the index
     * @param settingsFile file that contains settings
     * @throws IOException
     */
    void putSettings(String indexName, String settingsFile) throws IOException;

    /**
     * Sends the given query to ElasticSearch so it is executed in the specified index. Given that there are more
     * than maxNumber of results, only the first maxNumber are returned. Results are sorted according to sortOptions.
     * Result is returned wrapped in a AcademySearchResponse, that contains hits and facets.
     * @param indexName
     * @param query
     * @param maxNumber
     * @param sortOptions
     * @return
     * @throws IOException
     */
    AcademySearchResponse executeQuery(String indexName, Query query, Integer maxNumber, SortOptions sortOptions) throws IOException;
}
