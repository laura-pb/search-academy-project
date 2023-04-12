package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class QueryServiceImpl implements QueryService {

    @Override
    public Query multiMatchQuery(String value, String[] fields) {
        Query multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(value)
                .fields(Arrays.stream(fields).toList()))._toQuery();

        return multiMatchQuery;
    }
}
