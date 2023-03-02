package co.empathy.academy.search.services;

import co.empathy.academy.search.services.elastic.ElasticRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticServiceImpl implements ElasticService{

    private final ElasticRequest elasticRequest;

    public ElasticServiceImpl(ElasticRequest elasticRequest) {
        this.elasticRequest = elasticRequest;
    }

    @Override
    public String getClusterName() throws IOException {
        return elasticRequest.getClusterName();
    }

    @Override
    public String search(String query) {
        if (!query.isBlank()) {
            return elasticRequest.executeQuery(query);
        }
        return "";
    }
}
