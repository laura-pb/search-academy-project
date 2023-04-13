package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.Movie;
import co.empathy.academy.search.services.imdb.IMDbParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    private final static int MOVIE_BATCH_SIZE = 30000;
    private final static String IMDB_SETTINGS_FILE = "analyzer.json";
    private final static String IMDB_MAPPING_FILE = "mapping.json";

    @Autowired
    private ElasticService elasticService;

    @Override
    public void indexIMDbFiles(String indexName, File basics, File akas, File ratings, File crew, File principals) throws IOException {
        IMDbParser parser = new IMDbParser(basics, akas, ratings, crew, principals);

        List<Movie> moviesBatch = new ArrayList<>();
        elasticService.createIndex(indexName, IMDB_SETTINGS_FILE, IMDB_MAPPING_FILE);

        do {
            // To avoid Java memory problems and to achieve a better performance, indexing is made in batches of
            // MOVIE_BATCH_SIZE movies. Once a batch is completed, its data is locally removed and a new batch is computed.
            moviesBatch.clear();
            moviesBatch = parser.parseData(MOVIE_BATCH_SIZE);
            elasticService.indexIMDbDocs(moviesBatch, indexName);
        } while (moviesBatch.size() == MOVIE_BATCH_SIZE);
    }
}
