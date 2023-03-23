package co.empathy.academy.search.services;

import co.empathy.academy.search.services.imdb.IMDbParser;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public class IndexServiceImpl implements IndexService{
    @Override
    public void indexIMDbFiles(Optional<File> basics, Optional<File> akas, Optional<File> ratings) {
        new IMDbParser().parseData(basics, akas, ratings);
    }
}
