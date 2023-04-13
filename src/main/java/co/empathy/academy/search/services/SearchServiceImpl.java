package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private QueryService queryService;
    @Autowired
    private ElasticService elasticService;

    @Override
    public AcademySearchResponse<Movie> getMoviesByTitle(String indexName, String title) throws IOException {
        // Fields in the movies index that contain movie titles
        String[] fields = {"primaryTitle", "originalTitle", "akas.title"};
        Query query = queryService.multiMatchQuery(title, fields);
        AcademySearchResponse<Movie> movies = elasticService.executeQuery(indexName, query, 100, null);
        return movies;
    }
}
