package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        // Exact match for titles greatly boosted over partial matches
        String[] fields = {"primaryTitle^8", "primaryTitleNgrams", "originalTitle^8", "originalTitleNgrams", "akas.title^4"};
        Query titleQuery = queryService.multiMatchQuery(title, fields);
        // Movies and tvseries results are boosted
        String[] boostedTypes = {"movie", "tvSeries"};
        Query boostMoviesAndSeries = queryService.termsQuery(boostedTypes, "titleType", 2f);
        // Movies with more than 10k votes are boosted
        int votes = 10000;
        Query boostPopular = queryService.gteQuery(votes, "numVotes", 2f);
        List<Query> queries = new ArrayList<>();
        queries.add(titleQuery);
        queries.add(boostMoviesAndSeries);
        queries.add(boostPopular);

        Query finalQuery = queryService.shouldQuery(queries);
        AcademySearchResponse<Movie> movies = elasticService.executeQuery(indexName, finalQuery, 50, null);
        return movies;
    }

    @Override
    public AcademySearchResponse<Movie> getMoviesByFilters(String imdbIndexName, Optional<String[]> genres,
                                                           Optional<String[]> types, Optional<Integer> minRuntime,
                                                           Optional<Integer> maxRuntime, Optional<Float> minRating,
                                                           Optional<Integer> minYear, Optional<Integer> maxYear,
                                                           Optional<String> sortCriteria) throws IOException {
        List<Query> filterPresentQueries = new ArrayList<>();

        if (genres.isPresent()) {
            Query query = queryService.termsQuery(genres.get(), GENRES);
            filterPresentQueries.add(query);
        }

        if (types.isPresent()) {
            Query query = queryService.termsQuery(types.get(), TYPES);
            filterPresentQueries.add(query);
        }

        if (minRuntime.isPresent() || maxRuntime.isPresent()) {
            Query query = queryService.rangeQuery(minRuntime.isPresent() ? minRuntime.get() : 0, maxRuntime.isPresent() ?
                    maxRuntime.get() : Integer.MAX_VALUE, RUNTIME);
            filterPresentQueries.add(query);
        }

        if (minRating.isPresent()) {
            Query query = queryService.gteQuery(minRating.get(), RATING);
            filterPresentQueries.add(query);
        }

        if (minYear.isPresent() || maxYear.isPresent()) {
            Query query = queryService.rangeQuery(minYear.isPresent() ? minYear.get() : 0, maxYear.isPresent() ?
                    maxYear.get() : 5000, YEAR);
            filterPresentQueries.add(query);
        }

        // return only movies with +8k votes for more meaningful results
        Query query = queryService.gteQuery(MIN_VOTES, "numVotes");
        filterPresentQueries.add(query);

        Query finalQuery = queryService.mustQuery(filterPresentQueries);

        // Default sorting: desc by rating
        String sortField = RATING;
        String sortOrder = DESC;
        if (sortCriteria.isPresent()) {
            String[] sortValues = sortCriteria.get().split(";");
            sortField = sortValues[FIELD];
            sortOrder = sortValues[ORDER];
        }
        SortOptions sortOptions = queryService.getSortOptions(sortOrder, sortField);

        AcademySearchResponse<Movie> movies = elasticService.executeQuery(imdbIndexName, finalQuery, 500, sortOptions);
        return movies;
    }

    private final static String GENRES = "genres";
    private final static String TYPES = "titleType";
    private final static String RUNTIME = "runtimeMinutes";
    private final static String RATING = "averageRating";
    private final static String YEAR = "startYear";

    private final static int FIELD = 0;
    private final static int ORDER = 1;
    private final static String DESC = "desc";

    private final int MIN_VOTES = 8000;
}
