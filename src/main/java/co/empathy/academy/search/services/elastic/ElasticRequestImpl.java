package co.empathy.academy.search.services.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class ElasticRequestImpl implements ElasticRequest{
    private ElasticsearchClient client;

    @PostConstruct
    private void connect() {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/connecting.html
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        client = new ElasticsearchClient(transport);
    }

    @Override
    public String getClusterName() throws IOException {
        return client.cluster().health().clusterName();
    }

    @Override
    public String executeQuery(String query) {
        return query;
    }
}
