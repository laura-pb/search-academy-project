package co.empathy.academy.search.services.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.entities.facets.Facet;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;


import co.elastic.clients.elasticsearch.core.search.Hit;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElasticRequestImpl implements ElasticRequest{
    private ElasticsearchClient client;
    @PostConstruct
    private void connect() {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/connecting.html
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200), new HttpHost("elasticsearch", 9200)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        client = new ElasticsearchClient(transport);
    }

    @Override
    public void createIndex(String indexName, String settingsFile, String mappingFile) throws IOException {
        CreateIndexResponse createResponse = client.indices().create(
                new CreateIndexRequest.Builder()
                        .index(indexName)
                        .build()
        );
        putSettings(indexName, settingsFile);
        putMapping(indexName, mappingFile);
    }

    @Override
    public void deleteIndex(String indexName) throws IOException {
        client.indices().delete(DeleteIndexRequest.of(d -> d.index(indexName)));
    }

    @Override
    public boolean checkIndexExistence(String indexName) throws IOException {
        return client.indices().exists(ExistsRequest.of(e -> e.index(indexName))).value();
    }

    @Override
    public void bulkIndexMovies(List<Movie> movies, String indexName) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (Movie movie : movies) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(movie.getTconst())
                            .document(movie)
                    )
            );
        }

        client.bulk(br.build());
    }

    @Override
    public void putMapping(String indexName, String mappingFile) throws IOException {
        InputStream mapping = getClass().getClassLoader().getResourceAsStream(mappingFile);
        client.indices().putMapping(p -> p.index(indexName).withJson(mapping));
    }

    @Override
    public void putSettings(String indexName, String settingsFile) throws IOException {
        client.indices().close(c -> c.index(indexName));

        InputStream settings = getClass().getClassLoader().getResourceAsStream(settingsFile);
        client.indices().putSettings(p -> p.index(indexName).withJson(settings));

        client.indices().open(o -> o.index(indexName));
    }

    @Override
    public AcademySearchResponse executeQuery(String indexName, Query query, Integer maxNumber) throws IOException {
        SearchResponse<Movie> moviesResponse =
                client.search(SearchRequest.of(i -> i
                        .index(indexName)
                        .query(query)
                        .size(maxNumber)), Movie.class);

        List<Movie> hits = moviesResponse.hits().hits().stream().map(Hit::source).toList();
        List<Facet> facets = new ArrayList<>();

        return new AcademySearchResponse(hits, facets);
    }

    @Override
    public AcademySearchResponse executeQuery(String indexName, Query query, Integer maxNumber, SortOptions sortOptions) throws IOException {
        SearchResponse<Movie> moviesResponse =
                client.search(SearchRequest.of(i -> i
                                .index(indexName)
                                .query(query)
                                .sort(sortOptions)
                                .size(maxNumber)), Movie.class);

        List<Movie> hits = moviesResponse.hits().hits().stream().map(Hit::source).toList();
        List<Facet> facets = new ArrayList<>();

        return new AcademySearchResponse(hits, facets);
    }

    @Override
    public SearchResponse executeQuery(String indexName, Query query, Integer maxNumber, Map<String, Aggregation> aggregations) throws IOException {
        SearchResponse<Movie> moviesResponse =
            client.search(SearchRequest.of(i -> i
                                .index(indexName)
                                .query(query)
                                .size(maxNumber)
                                .aggregations(aggregations)), Movie.class);

        return moviesResponse;
    }
}
