package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;

import java.io.IOException;
import java.util.Optional;

public interface SearchService {
    //AcademySearchResponse getMoviesWithFilters(String indexName, Optional<String> genres, Optional<Integer> maxNumber);

    AcademySearchResponse<Movie> getMoviesByTitle(String indexName, String title) throws IOException;
}
