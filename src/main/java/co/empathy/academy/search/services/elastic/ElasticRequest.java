package co.empathy.academy.search.services.elastic;

import java.io.IOException;

public interface ElasticRequest {
    String getClusterName() throws IOException;

    String executeQuery(String query);
}
