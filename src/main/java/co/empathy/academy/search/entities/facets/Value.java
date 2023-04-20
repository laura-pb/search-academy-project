package co.empathy.academy.search.entities.facets;

public class Value {

    private String id;
    private String value;
    private long count;
    private String filter;

    public Value(String id, String value, long count, String filter) {
        this.id = id;
        this.value = value;
        this.count = count;
        this.filter = filter;
    }

    public Value() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
