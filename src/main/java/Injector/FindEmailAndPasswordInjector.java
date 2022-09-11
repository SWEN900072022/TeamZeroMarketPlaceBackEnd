package Injector;

public class FindEmailAndPasswordInjector implements FindConditionInjector{
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM users where email=? and password=?;";
    }
}
