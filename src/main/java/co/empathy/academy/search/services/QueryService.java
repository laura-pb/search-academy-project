package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

/**
 * This service is in charge of creating different types of queries compatible with ElasticSearch.
 */
public interface QueryService {
    /**
     * Creates a multiMatch query. This query searches for the provided value in every field
     * contained in fields.
     * @param value to be searched
     * @param fields where value is searched for
     * @return
     */
    Query multiMatchQuery(String value, String[] fields);
}
