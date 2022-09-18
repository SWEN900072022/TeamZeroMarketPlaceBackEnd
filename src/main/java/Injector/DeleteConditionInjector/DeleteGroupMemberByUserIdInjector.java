package Injector.DeleteConditionInjector;

import Injector.IInjector;

public class DeleteGroupMemberByUserIdInjector implements IInjector {
    @Override
    public String getSQLQuery() {
        return "DELETE FROM groupmembership WHERE userid=?;";
    }
}
