package Injector;

public class FindOrderFromUserInjector implements FindConditionInjector{
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM orderitems oi " +
                "JOIN orders o on oi.orderid = o.orderid " +
                "WHERE userid=?";
    }
}
