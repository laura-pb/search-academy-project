package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.Movie;

import java.io.IOException;
import java.util.List;

public interface ElasticService {
    void indexIMDbDocs(List<Movie> movies) throws IOException;
}
