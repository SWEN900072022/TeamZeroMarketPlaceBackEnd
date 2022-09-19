package Injector.FindConditionInjector;

public class FindUserOrEmailInjector extends GeneralFindConditionInjector{

    @Override
    public String getSQLQuery() {
        return "SELECT * FROM users where username=? or email=? LIMIT ? OFFSET ?;";
    }
}
