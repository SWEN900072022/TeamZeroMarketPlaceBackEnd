package Injector.FindConditionInjector;

public class FindEmailAndPasswordInjector extends GeneralFindConditionInjector{
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM users where email=? and password=?;";
    }
}
