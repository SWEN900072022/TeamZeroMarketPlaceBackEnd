package Domain;

import Enums.UserRoles;

import java.util.List;

public class Customer {
    private UserRoles userRoles = UserRoles.CUSTOMER;
    private List<Order> orderList;

}
