package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.entities.facets.ValueFacet;

import java.io.IOException;
import java.util.Optional;

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

    /**
     * Returns Movies according to the provided filters
     * @param imdbIndexName
     * @param genres
     * @param types
     * @param minRuntime
     * @param maxRuntime
     * @param minRating
     * @param minYear
     * @param maxYear
     * @param sortCriteria
     * @return
     */
    AcademySearchResponse<Movie> getMoviesByFilters(String imdbIndexName, Optional<String[]> genres, Optional<String[]> types,
                                                    Optional<Integer> minRuntime, Optional<Integer> maxRuntime,
                                                    Optional<Float> minRating, Optional<Integer> minYear,
                                                    Optional<Integer> maxYear, Optional<String> sortCriteria, Optional<String> title) throws IOException;
    AcademySearchResponse<Movie> getAggregation(String indexName, String field) throws IOException;

    ValueFacet getValueFacet(String indexName, String field) throws IOException;
}
