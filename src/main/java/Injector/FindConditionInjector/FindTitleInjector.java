package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindTitleInjector implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM listings WHERE title=?;";
    }
}
