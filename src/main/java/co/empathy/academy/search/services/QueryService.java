package co.empathy.academy.search.services;


import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public interface QueryService {
    public Query multiMatchQuery(String value, String[] fields);
}
