package co.empathy.academy.search.services;

import java.io.IOException;

public interface ElasticService {
    String getClusterName() throws IOException;

    String search(String query);
}
