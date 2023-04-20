package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryServiceImpl implements QueryService {

    @Override
    public Query multiMatchQuery(String value, String[] fields) {
        Query multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(value)
                .fields(Arrays.stream(fields).toList()))._toQuery();

        return multiMatchQuery;
    }

    @Override
    public Query termsQuery(String[] values, String field, float boost) {
        TermsQueryField termsQueryField = TermsQueryField.of(t -> t
                .value(Arrays.stream(values).toList().stream().map(FieldValue::of).collect(Collectors.toList())));

        Query termsQuery = TermsQuery.of(t -> t
                .terms(termsQueryField)
                .field(field)
                .boost(boost))._toQuery();

        return termsQuery;
    }

    @Override
    public Query termsQuery(String[] values, String field) {
        return termsQuery(values, field, 1.0f);
    }

    @Override
    public Query matchQuery(String value, String field, float boost) {
        Query matchQuery = MatchQuery.of(m -> m
                .query(value)
                .field(field)
                .boost(boost))._toQuery();

        return matchQuery;
    }

    @Override
    public Query matchQuery(String value, String field) {
        return matchQuery(value, field, 1.0f);
    }

    @Override
    public Query shouldQuery(List<Query> queries) {
        Query shouldQuery = BoolQuery.of(b -> b.should(queries))._toQuery();
        return shouldQuery;
    }

    @Override
    public Query mustQuery(List<Query> queries) {
        Query boolQuery = BoolQuery.of(b -> b.must(queries))._toQuery();
        return boolQuery;
    }

    @Override
    public Query rangeQuery(double minValue, double maxValue, String field) {
        Query rangeQuery = RangeQuery.of(r -> r
                .field(field)
                .gte(JsonData.of(minValue))
                .lte(JsonData.of(maxValue)))._toQuery();
        return rangeQuery;
    }

    @Override
    public Query gteQuery(double minValue, String field, float boost) {
        Query gteQuery = RangeQuery.of(r -> r
                .field(field)
                .gte(JsonData.of(minValue))
                .boost(boost))._toQuery();
        return gteQuery;
    }

    @Override
    public Query gteQuery(double minValue, String field) {
        return gteQuery(minValue, field, 1.0f);
    }

    @Override
    public Query matchAllQuery() {
        Query matchAllQuery = MatchAllQuery.of(m -> m)._toQuery();
        return matchAllQuery;
    }

    @Override
    public Aggregation getAggregation(int size, String field) {
        Aggregation agg = TermsAggregation.of(t -> t
                .field(field)
                .size(size))._toAggregation();
        return agg;
    }

    @Override
    public SortOptions getSortOptions(String order, String field) {
        SortOptions sortOptions = SortOptions.of(s -> s
                .field(FieldSort.of(f -> f
                        .field(field)
                        .order(order.equals(ASC) ? SortOrder.Asc : SortOrder.Desc))));
        return sortOptions;
    }

    private final static String ASC = "asc";

}
