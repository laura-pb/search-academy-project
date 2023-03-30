package co.empathy.academy.search.services;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface IndexService {
    /**
     * Indexes IMDb data from basics, akas and ratings files.
     */
    void indexIMDbFiles(File basics, File akas, File ratings) throws IOException;
}
