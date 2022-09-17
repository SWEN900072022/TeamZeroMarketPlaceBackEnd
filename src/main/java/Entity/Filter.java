package Entity;

public class Filter {
    private String filterKey;
    private String filterVal;

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

    public String getFilterVal() {
        return filterVal;
    }

    public void setFilterVal(String filterVal) {
        this.filterVal = filterVal;
    }
}
