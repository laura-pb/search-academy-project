package co.empathy.academy.search.services.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.empathy.academy.search.entities.Movie;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.List;

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
    public void createIndex(String indexName) throws IOException {
        CreateIndexResponse createResponse = client.indices().create(
                new CreateIndexRequest.Builder()
                        .index(indexName)
                        .build()
        );
    }

    @Override
    public void bulkIndexMovies(List<Movie> movies, String indexName) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (Movie movie : movies) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(movie.getMovieId())
                            .document(movie)
                    )
            );
        }
        BulkResponse bulkResponse = client.bulk(br.build());
    }
}
