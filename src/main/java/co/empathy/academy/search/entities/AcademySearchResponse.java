package co.empathy.academy.search.entities;

import co.empathy.academy.search.entities.facets.Facet;

import java.util.List;

public class AcademySearchResponse<E> {
    private List<E> hits;
    private List<Facet> facets;

    public AcademySearchResponse() {}

    public AcademySearchResponse(List<E> hits, List<Facet> facets) {
        this.hits = hits;
        this.facets = facets;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }

    public List<E> getHits() {
        return hits;
    }

    public void setHits(List<E> hits) {
        this.hits = hits;
    }
}
