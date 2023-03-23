package co.empathy.academy.search.services;

import java.io.File;
import java.util.Optional;

public interface IndexService {
    /**
     * Indexes IMDb data from basics, akas and ratings files.
     */
    void indexIMDbFiles(Optional<File> basics, Optional<File> akas, Optional<File> ratings);
}
