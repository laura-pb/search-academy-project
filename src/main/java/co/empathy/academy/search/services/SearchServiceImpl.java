package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.entities.facets.Facet;
import co.empathy.academy.search.entities.facets.Value;
import co.empathy.academy.search.entities.facets.ValueFacet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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
        String[] fields = {"primaryTitle^3", "primaryTitleNgrams", "originalTitle^3", "originalTitleNgrams", "akas.title^2"};
        Query titleQuery = queryService.multiMatchQuery(title, fields);
        // Movies and tvseries results are boosted
        String[] boostedTypes = {"movie", "tvSeries"};
        Query boostMoviesAndSeries = queryService.termsQuery(boostedTypes, "titleType", 15f);
        // Movies with more than 10k votes are boosted
        int votes = 10000;
        Query boostPopular = queryService.gteQuery(votes, "numVotes", 15f);
        List<Query> queries = new ArrayList<>();
        queries.add(titleQuery);
        queries.add(boostMoviesAndSeries);
        queries.add(boostPopular);

        Query finalQuery = queryService.shouldQuery(queries);
        AcademySearchResponse<Movie> movies = elasticService.executeQuery(indexName, finalQuery, 50);
        return movies;
    }

    @Override
    public AcademySearchResponse<Movie> getMoviesByFilters(String imdbIndexName, Optional<String[]> genres,
                                                           Optional<String[]> types, Optional<Integer> minRuntime,
                                                           Optional<Integer> maxRuntime, Optional<Float> minRating,
                                                           Optional<Integer> minYear, Optional<Integer> maxYear,
                                                           Optional<String> sortCriteria, Optional<String> title) throws IOException {
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

        if (title.isPresent()) {
            String[] fields = {"primaryTitle^3", "primaryTitleNgrams", "originalTitle^3", "originalTitleNgrams", "akas.title^2"};
            Query query = queryService.multiMatchQuery(title.get(), fields);
            filterPresentQueries.add(query);
        }

        // return only documents with +8k votes for more meaningful results
        Query query = queryService.gteQuery(MIN_VOTES, "numVotes");
        filterPresentQueries.add(query);

        Query finalQuery = queryService.mustQuery(filterPresentQueries);

        // Default sorting: desc by rating
        String sortField = RATING;
        String sortOrder = DESC;
        if (sortCriteria.isPresent()) {
            String[] sortValues = sortCriteria.get().split("_");
            sortField = sortValues[FIELD];
            sortOrder = sortValues[ORDER];
        }
        SortOptions sortOptions = queryService.getSortOptions(sortOrder, sortField);

        AcademySearchResponse<Movie> movies = elasticService.executeQuery(imdbIndexName, finalQuery, 500, sortOptions);
        return movies;
    }

    @Override
    public AcademySearchResponse<Movie> getAggregation(String indexName, String field) throws IOException {
        ValueFacet facet = getValueFacet(indexName, field);

        List<Facet> facets = new ArrayList<>();
        facets.add(facet);

        AcademySearchResponse response = new AcademySearchResponse<>(new ArrayList<Movie>(), facets);
        return response;
    }

    @Override
    public ValueFacet getValueFacet(String indexName, String field) throws IOException {
        Query matchAllQuery = queryService.matchAllQuery();

        Aggregation fieldAgg = queryService.getAggregation(100, field);
        Map<String, Aggregation> aggs = new HashMap<>();
        aggs.put(field, fieldAgg);

        SearchResponse<Movie> queryResponse = elasticService.executeQuery(indexName, matchAllQuery, 0, aggs);
        List<Value> values = new ArrayList<>();
        List<StringTermsBucket> buckets = queryResponse.aggregations().get(field).sterms().buckets().array();
        for (StringTermsBucket bucket : buckets) {
            values.add(new Value(bucket.key().stringValue(), bucket.key().stringValue(), bucket.docCount(),
                    field + ":" + bucket.key().stringValue()));
        }

        // Capitalize field name first letter to properly compose facet name
        String fieldFirstUpperCase = field.substring(0, 1).toUpperCase() + field.substring(1);
        ValueFacet facetField = new ValueFacet("facet" + fieldFirstUpperCase, values);

        return facetField;
    }

    @Override
    public AcademySearchResponse<Movie> getMovieByTconst(String indexName, String tconst) throws IOException {
        Query exactMatchQuery = queryService.matchQuery(tconst, TCONST);

        AcademySearchResponse<Movie> movies = elasticService.executeQuery(indexName, exactMatchQuery, 1);
        return movies;
    }

    private final static String GENRES = "genres";
    private final static String TYPES = "titleType";
    private final static String RUNTIME = "runtimeMinutes";
    private final static String RATING = "averageRating";
    private final static String YEAR = "startYear";
    private final static String TCONST = "tconst";

    private final static int FIELD = 0;
    private final static int ORDER = 1;
    private final static String DESC = "desc";

    private final int MIN_VOTES = 8000;
}
