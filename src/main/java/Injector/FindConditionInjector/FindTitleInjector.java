package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindTitleInjector implements IInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM listings WHERE title=?;";
    }
}
