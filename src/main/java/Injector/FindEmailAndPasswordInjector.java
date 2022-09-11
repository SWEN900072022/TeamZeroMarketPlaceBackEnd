package Injector;

public class FindEmailAndPasswordInjector implements FindConditionInjector{
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM user where email=? and password=?;";
    }
}
