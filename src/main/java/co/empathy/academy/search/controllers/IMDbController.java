package co.empathy.academy.search.controllers;

import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.services.FavoriteServiceImpl;
import co.empathy.academy.search.services.IndexService;
import co.empathy.academy.search.services.SearchService;
import co.empathy.academy.search.util.FileConversion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/movies")
public class IMDbController {
    private final static String IMDB_INDEX_NAME = "movies";
    private final static String GENRES = "genres";
    private final static String TYPES = "titleType";

    @Autowired
    private IndexService indexService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private FavoriteServiceImpl favoriteService;

    @Operation(summary = "Asynchronously indexes basics, akas, ratings, crew and principals IMDb datasets from its files." +
            "If the index already exists, it replaces it with the new data.")
    @Parameter(name = "basics", description = "IMDb basics file with basic film information")
    @Parameter(name = "akas", description = "IMDb akas file with movie title translations")
    @Parameter(name = "ratings", description = "IMDb ratings file with movie rating and votes information")
    @Parameter(name = "crew", description = "IMDb crew file with movie director and writer information")
    @Parameter(name = "principals", description = "IMDb principals file with movie cast information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Files uploaded.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request, some file missing", content = @Content),
            @ApiResponse(responseCode = "500", description = "Unexpected problem reading the file", content = @Content)
    })
    @PostMapping(value = "", consumes = {"multipart/form-data"})
    public ResponseEntity indexIMDbFiles(@RequestParam MultipartFile basics,
                                         @RequestParam MultipartFile akas,
                                         @RequestParam MultipartFile ratings,
                                         @RequestParam MultipartFile crew,
                                         @RequestParam MultipartFile principals) throws IOException {
        File basicsFile = FileConversion.convertMultipartToTempFile(basics);
        File akasFile = FileConversion.convertMultipartToTempFile(akas);
        File ratingsFile = FileConversion.convertMultipartToTempFile(ratings);
        File crewFile = FileConversion.convertMultipartToTempFile(crew);
        File principalsFile = FileConversion.convertMultipartToTempFile(principals);

        BackgroundJob.enqueue(() -> indexService.indexIMDbFiles(IMDB_INDEX_NAME, basicsFile, akasFile, ratingsFile, crewFile, principalsFile));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


    @Operation(summary = "Get movies that match the provided title. It DOESN'T match exactly, but boosts exact results")
    @Parameter(name = "title", description = "Title of the searched movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all movies that match the query.", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AcademySearchResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Unexpected problem accessing database", content = @Content)
    })
    @GetMapping(value = "/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AcademySearchResponse<Movie>> queryMoviesByTitle(@PathVariable("title") String title) throws IOException {
        AcademySearchResponse<Movie> searchResponse = searchService.getMoviesByTitle(IMDB_INDEX_NAME, title);
        return ResponseEntity.ok(searchResponse);
    }

    @Operation(summary = "Returns movies that meet the provided filters and sorts them according to the given sorting option")
    @Parameter(name = "genres", description = "List of genres. Movies returned must have at least one genre of the provided ones")
    @Parameter(name = "types", description = "List of media types (i.e. movie, short, tvSeries...). " +
            "Media returned must be of one type of the provided ones")
    @Parameter(name = "minRuntime", description = "Minimum number of minutes that movies must last to be returned")
    @Parameter(name = "maxRuntime", description = "Maximum number of minutes that movies must last to be returned")
    @Parameter(name = "minRating", description = "Minimum average rating that movies must have to be returned")
    @Parameter(name = "minYear", description = "Movies from years prior to minYear won't be returned")
    @Parameter(name = "maxYear", description = "Movies from years coming after maxYear won't be returned")
    @Parameter(name = "sortCriteria", description = "Movies will be sorted by the chosen criteria",
            schema = @Schema(allowableValues = { "startYear_desc", "startYear_asc", "averageRating_desc", "averageRating_asc"}))
    @Parameter(name = "title", description = "Movies' title returned must match parameter title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all movies that match given filters.", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AcademySearchResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Unexpected problem accessing database", content = @Content)
    })
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AcademySearchResponse<Movie>> filterMovies(@RequestParam("genres") Optional<String[]> genres,
                                                                     @RequestParam("types") Optional<String[]> types,
                                                                     @RequestParam("minRuntime") Optional<Integer> minRuntime,
                                                                     @RequestParam("maxRuntime") Optional<Integer> maxRuntime,
                                                                     @RequestParam("minRating") Optional<Float> minRating,
                                                                     @RequestParam("minYear") Optional<Integer> minYear,
                                                                     @RequestParam("maxYear") Optional<Integer> maxYear,
                                                                     @RequestParam("sortCriteria") Optional<String> sortCriteria,
                                                                     @RequestParam("title") Optional<String> title) throws IOException {
        AcademySearchResponse<Movie> searchResponse = searchService.getMoviesByFilters(IMDB_INDEX_NAME, genres, types,
                                                        minRuntime, maxRuntime, minRating, minYear, maxYear, sortCriteria, title);
        return ResponseEntity.ok(searchResponse);
    }

    @Operation(summary = "Get an aggregation of all genres available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aggregation of all genres", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AcademySearchResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Unexpected problem accessing database", content = @Content)
    })
    @GetMapping(value = "/genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AcademySearchResponse<Movie>> getGenres() throws IOException {
        AcademySearchResponse<Movie> searchResponse = searchService.getAggregation(IMDB_INDEX_NAME, GENRES);
        return ResponseEntity.ok(searchResponse);
    }

    @Operation(summary = "Get an aggregation of all types available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aggregation of all types", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AcademySearchResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Unexpected problem accessing database", content = @Content)
    })
    @GetMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AcademySearchResponse<Movie>> getTypes() throws IOException {
        AcademySearchResponse<Movie> searchResponse = searchService.getAggregation(IMDB_INDEX_NAME, TYPES);
        return ResponseEntity.ok(searchResponse);
    }

    @Operation(summary = "Add a new movie to session favorite movies")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movie added", content = @Content),
    })
    @PostMapping(value = "/favorites/{movieId}")
    public ResponseEntity addFavoriteMovie(@PathVariable String movieId, HttpServletRequest request) throws IOException, InterruptedException {
        favoriteService.addFavoriteMovie(movieId, request.getSession());
        return ResponseEntity.status(HttpStatus.CREATED).body("Movie added to favorites");
    }
    
    @Operation(summary = "Obtain daily recommended movie based on session likes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily movie", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AcademySearchResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not enough movies to give a recommendation", content = @Content)
    })
    @GetMapping(value = "/daily", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AcademySearchResponse<Movie>> getDailyMovie(HttpServletRequest request) throws IOException, InterruptedException {
        AcademySearchResponse<Movie> daily = favoriteService.getDaily(request.getSession(), IMDB_INDEX_NAME);
        return ResponseEntity.ok(daily);
    }
}
