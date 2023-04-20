package co.empathy.academy.search.entities.facets;

import java.util.List;

public interface Facet {
    String getFacet();
    void setFacet(String facet);
    String getType();
    void setType(String type);
    List<Value> getValues();
    void setValues(List<Value> values);
}
