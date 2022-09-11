package Injector;

public class FindUserOrEmailInjector implements FindConditionInjector{

    @Override
    public String getSQLQuery() {
        return "SELECT * FROM users where username=? or email=?;";
    }
}
