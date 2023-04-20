package co.empathy.academy.search.entities.facets;

import java.util.List;

public class ValueFacet implements Facet {
    public static final String VALUE_FACET = "value";
    private String facet;
    private String type;
    private List<Value> values;

    public ValueFacet() {
    }

    public ValueFacet(String facet, String type, List<Value> values) {
        this.facet = facet;
        this.type = type;
        this.values = values;
    }

    public ValueFacet(String facet, List<Value> values) {
        this(facet, VALUE_FACET, values);
    }

    @Override
    public String getFacet() {
        return facet;
    }

    @Override
    public void setFacet(String facet) {
        this.facet = facet;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public List<Value> getValues() {
        return values;
    }

    @Override
    public void setValues(List<Value> values) {
        this.values = values;
    }
}
