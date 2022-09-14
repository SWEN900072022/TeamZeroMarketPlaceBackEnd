package Injector;

public class FindTitleInjector implements FindConditionInjector{
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM listings WHERE title=?;";
    }
}
