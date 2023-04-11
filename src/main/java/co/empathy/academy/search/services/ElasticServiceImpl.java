package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.services.elastic.ElasticRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ElasticServiceImpl implements ElasticService{

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
        elasticRequest.createIndex(indexName, settingsFile, mappingsFile);
    }
}
