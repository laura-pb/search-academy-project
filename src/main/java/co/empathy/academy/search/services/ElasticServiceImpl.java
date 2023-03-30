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
    public void indexIMDbDocs(List<Movie> movies) throws IOException {
        String indexName = "movies";
        elasticRequest.createIndex(indexName);
        elasticRequest.bulkIndexMovies(movies, indexName);

    }
}
