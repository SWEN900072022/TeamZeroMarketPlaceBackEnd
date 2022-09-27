package Injector.DeleteConditionInjector;

import Injector.ISQLInjector;

public class DeleteGroupMemberByUserIdInjector implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "DELETE FROM groupmembership WHERE userid=?;";
    }
}
