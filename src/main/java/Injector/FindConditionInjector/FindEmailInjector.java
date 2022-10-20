package Injector.FindConditionInjector;

public class FindEmailInjector extends GeneralFindConditionInjector{
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM users where email=?;";
    }
}