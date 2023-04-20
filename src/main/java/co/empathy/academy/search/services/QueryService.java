package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.List;

/**
 * This service is in charge of creating different types of queries compatible with ElasticSearch.
 */
public interface QueryService {
    /**
     * Creates a multiMatch query. This query searches for the provided value in every field
     * contained in fields.
     * @param value to be searched
     * @param fields where value is searched for
     * @return multiMatch Query
     */
    Query multiMatchQuery(String value, String[] fields);

    /**
     * Creates a terms query. This query searches for all the provided values in the given field.
     * It boosts the results by the bool value
     * @param values to be searched
     * @param field where values are searched for
     * @param boost boost value
     * @return terms Query
     */
    Query termsQuery(String[] values, String field, float boost);

    /**
     * Creates a terms query. This query searches for all the provided values in the given field.
     * @param values to be searched
     * @param field where values are searched for
     * @return terms Query
     */
    Query termsQuery(String[] values, String field);

    /**
     * Creates a match query searching for the provided value in the given field. It boosts results
     * matching the query by the bool value
     * @param value to be searched
     * @param field where value is searched for
     * @param boost boost value
     * @return match Query
     */
    Query matchQuery(String value, String field, float boost);

    /**
     * Creates a match query searching for the provided value in the given field.
     * @param value to be searched
     * @param field where value is searched for
     * @return match Query
     */
    Query matchQuery(String value, String field);

    /**
     * Returns a bool query composed of all queries provided in the parameter as should queries.
     * @param queries list of queries to join
     * @return bool query containing only should queries
     */
    Query shouldQuery(List<Query> queries);

    /**
     * Returns a bool query composed of all queries provided in the parameter as must queries.
     * @param queries list of queries to join
     * @return bool query containing only must queries
     */
    Query mustQuery(List<Query> queries);

    /**
     *
     * @param minValue
     * @param maxValue
     * @param field
     * @return
     */
    Query rangeQuery(double minValue, double maxValue, String field);

    /**
     * Creates a query that returns documents which field value is greater or equal than minValue provided as parameter.
     * It boosts results by boost value.
     * @param minValue minimum value (included) that field must contain
     * @param field that contains value that is going to be compared with minValue
     * @param boost boost value
     * @return range Query with only lower limit
     */
    Query gteQuery(double minValue, String field, float boost);

    /**
     * Creates a query that returns documents which field value is greater or equal than minValue provided as parameter.
     * @param minValue minimum value (included) that field must contain
     * @param field that contains value that is going to be compared with minValue
     * @return range Query with only lower limit
     */
    Query gteQuery(double minValue, String field);

    /**
     * Creates a query that matches everything in the index. Used for building aggregations.
     * @return matchAll Query
     */
    Query matchAllQuery();

    Aggregation getAggregation(int size, String field);

    SortOptions getSortOptions(String order, String field);
}
