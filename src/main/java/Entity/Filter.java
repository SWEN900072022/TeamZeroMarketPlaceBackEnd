package Entity;

public class Filter {
    private String filterKey;
    private Object filterVal;

    public Filter(String filterKey, String filterVal) {
        this.filterKey = filterKey;
        this.filterVal = filterVal;
    }

    public String getFilterKey() {
        return filterKey;
    }

    public void setFilterKey(String filterKey) {
        this.filterKey = filterKey;
    }

    public Object getFilterVal() {
        return filterVal;
    }

    public void setFilterVal(Object filterVal) {
        this.filterVal = filterVal;
    }
}
