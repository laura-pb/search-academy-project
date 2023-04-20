package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstraction layer between the ElasticSearch client and the different services that use it.
 */
public interface ElasticService {
    /**
     * Sends a list of movies to the ElasticSearch client to index them.
     * @param movies to be indexed
     * @param indexName index where movies are going to be indexed
     * @throws IOException
     */
    void indexIMDbDocs(List<Movie> movies, String indexName) throws IOException;

    /**
     * Sends the index name and the mappings and settings files to the Elastic client to create a new
     * index and configure it. If an index with the provided name already exists, it deletes it (removing
     * all its data) and creates it again.
     * it again,
     * @param indexName
     * @param settingsFile
     * @param mappingsFile
     * @throws IOException
     */
    void createIndex(String indexName, String settingsFile, String mappingsFile) throws IOException;

    /**
     * Sends the provided query to the Elastic client to execute it in the specified index. If the query returns more
     * than maxNumber of results, it just returns the first maxNumber. Results are sorted according to sortOptions.
     * @param indexName name of the index where the query will be executed
     * @param query to execute
     * @param maxNumber maximum number of hits to be returned
     * @return an AcademySearchResponse that is composed both of hits and facets
     * @throws IOException
     */
    AcademySearchResponse executeQuery(String indexName, Query query, Integer maxNumber) throws IOException;

    /**
     * Sends the provided query to the Elastic client to execute it in the specified index. If the query returns more
     * than maxNumber of results, it just returns the first maxNumber. Results are sorted according to sortOptions.
     * @param indexName name of the index where the query will be executed
     * @param query to execute
     * @param maxNumber maximum number of hits to be returned
     * @param sortOptions way in which results obtained are going to be sorted
     * @return an AcademySearchResponse that is composed both of hits and facets
     * @throws IOException
     */
    AcademySearchResponse executeQuery(String indexName, Query query, Integer maxNumber, SortOptions sortOptions) throws IOException;

    /**
     * Sends the provided query to the Elastic client to execute it in the specified index. If the query returns more
     * than maxNumber of results, it just returns the first maxNumber. Results are sorted according to sortOptions.
     * @param indexName name of the index where the query will be executed
     * @param query to execute
     * @param maxNumber maximum number of hits to be returned
     * @param aggregations way in which results obtained are going to be sorted
     * @return an AcademySearchResponse that is composed both of hits and facets
     * @throws IOException
     */
    SearchResponse executeQuery(String indexName, Query query, Integer maxNumber, Map<String, Aggregation> aggregations) throws IOException;
}
