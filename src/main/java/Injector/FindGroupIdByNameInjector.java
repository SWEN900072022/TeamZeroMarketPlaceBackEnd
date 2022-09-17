package Injector;

public class FindGroupIdByNameInjector implements FindConditionInjector{
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM sellergroups WHERE groupname=?;";
    }
}
