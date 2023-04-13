package co.empathy.academy.search.services;

import java.io.File;
import java.io.IOException;

/**
 * This service is in charge of creating indexes and indexing data
 */
public interface IndexService {
    /**
     * Creates an IMDB movies index if it doesn't exist. If it exists, it deletes it and creates a new empty one.
     * Once the new index is created, it parses and indexes IMDb data from basics, akas, ratings, crew and principals files.
     */
    void indexIMDbFiles(String indexName, File basics, File akas, File ratings, File crew, File principals) throws IOException;
}
