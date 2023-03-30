package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.services.imdb.IMDbParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    private final static int MOVIE_BATCH_SIZE = 30;

    @Autowired
    private ElasticService elasticService;

    @Override
    public void indexIMDbFiles(File basics, File akas, File ratings) throws IOException {
        IMDbParser parser = new IMDbParser(basics, akas, ratings);

        List<Movie> moviesBatch;
        do {
            moviesBatch = parser.parseData(MOVIE_BATCH_SIZE);
            elasticService.indexIMDbDocs(moviesBatch);
            moviesBatch.clear();
        } while (moviesBatch.size() == MOVIE_BATCH_SIZE);

    }
}