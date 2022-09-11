package Injector;

public class FindIdInjector implements FindConditionInjector {

    @Override
    public String getSQLQuery() {
        return "SELECT * FROM ? where id=?;";
    }
}
