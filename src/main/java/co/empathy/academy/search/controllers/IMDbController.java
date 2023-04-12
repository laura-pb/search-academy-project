package co.empathy.academy.search.controllers;

import co.empathy.academy.search.entities.AcademySearchResponse;
import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.services.IndexService;
import co.empathy.academy.search.services.SearchService;
import co.empathy.academy.search.util.FileConversion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/movies")
public class IMDbController {
    private final static String IMDB_INDEX_NAME = "movies";

    @Autowired
    private IndexService indexService;

    @Autowired
    private SearchService searchService;

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

    //TODO THIS IS A FIRST BASIC DRAFT JUST TO TEST CONNECTION WITH FRONTEND
    @Operation(summary = "Get movies that match the provided title. It DOESN'T match exactly")
    @Parameter(name = "title", description = "Title of the searched movie")
    @GetMapping("/{title}")
    public ResponseEntity<AcademySearchResponse<Movie>> queryMoviesByTitle(@PathVariable("title") String title) throws IOException {
        AcademySearchResponse<Movie> searchResponse = searchService.getMoviesByTitle(IMDB_INDEX_NAME, title);
        return ResponseEntity.ok(searchResponse);
    }
}
