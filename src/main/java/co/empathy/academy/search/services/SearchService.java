package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;

import java.io.IOException;

/**
 * This service obtains query values and filters from the IMDbController and returns a result. To do so,
 * it orchestrates the query execution by obtaining the corresponding query from QueryService and sending
 * it to ElasticService.
 */
public interface SearchService {
    /**
     * Returns the 100 movies that best match the title given
     * @param indexName movies index name
     * @param title of the movie
     * @return
     * @throws IOException
     */
    AcademySearchResponse<Movie> getMoviesByTitle(String indexName, String title) throws IOException;
}
