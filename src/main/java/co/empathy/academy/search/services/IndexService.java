package co.empathy.academy.search.services;

import java.io.File;
import java.io.IOException;

public interface IndexService {
    /**
     * Indexes IMDb data from basics, akas and ratings files.
     */
    void indexIMDbFiles(String indexName, File basics, File akas, File ratings, File crew, File principals) throws IOException;
}
