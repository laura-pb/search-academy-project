package co.empathy.academy.search.controllers;

import co.empathy.academy.search.entities.User;
import co.empathy.academy.search.entities.error.ErrorData;
import co.empathy.academy.search.services.IndexService;
import co.empathy.academy.search.util.FileConversion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/files")
public class IndexController {
    @Autowired
    private IndexService indexService;

    @Operation(summary = "Asynchronously indexes basics, akas and ratings IMDb datasets from its files")
    @Parameter(name = "basics", description = "IMDb basics file with basic film information")
    @Parameter(name = "akas", description = "IMDb akas file with movie title translations")
    @Parameter(name = "ratings", description = "IMDb ratings file with movie rating and votes information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Files uploaded.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Unexpected problem reading the file", content = @Content)
    })
    @PostMapping(value = "", consumes = {"multipart/form-data"})
    public ResponseEntity indexIMDbFiles(@RequestParam MultipartFile basics,
                               @RequestParam MultipartFile akas,
                               @RequestParam MultipartFile ratings) throws IOException {
        File basicsFile = FileConversion.convertMultipartToTempFile(basics);
        File akasFile = FileConversion.convertMultipartToTempFile(akas);
        File ratingsFile = FileConversion.convertMultipartToTempFile(ratings);

        //BackgroundJob.enqueue(() -> indexService.indexIMDbFiles(basicsFile, akasFile, ratingsFile));
        indexService.indexIMDbFiles(basicsFile, akasFile, ratingsFile);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
