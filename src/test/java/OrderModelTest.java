import Entity.Order;
import Entity.OrderItem;
import MockClasses.MockRepository;
import Model.OrderModel;
import Enums.UserRoles;
import Util.JWTUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderModelTest {
    private MockRepository repo;
    private OrderModel orderModel;
    private String jwt;
    private Order order;
    private List<OrderItem> orderItemList;

    public OrderModelTest() {
        this.repo = new MockRepository();
        this.orderModel = new OrderModel(repo);

        // Generate a valid jwt for testing
        jwt = JWTUtil.generateToken("1", new HashMap<>());

        order = new Order();
    }

    @Test
    /**
     * Create order test
     */
    public void invalidJWTTokenDuringOrderCreation() {
        boolean isSuccessful = orderModel.createOrderItem(new ArrayList<>(),new Order(), "");
        assertFalse(isSuccessful);
    }

    @Test
    public void cancelOrderInvalidJWT() {
        boolean isSuccessful = orderModel.cancelOrders(new ArrayList<>(), "");
        assertFalse(isSuccessful);
    }

    @Test
    public void cancelOrderTokenWIthInvalidRoles() {
        String jwt = JWTUtil.generateToken("1", new HashMap<>());
        // This jwt should have no roles assigned to it

        boolean isSuccessful = orderModel.cancelOrders(new ArrayList<>(), jwt);
        assertFalse(isSuccessful);
    }

    @Test
    public void NullOrders() {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("role", UserRoles.SELLER.toString());
        String jwt = JWTUtil.generateToken("1", claimMap);

        List<Order> orderList = new ArrayList<>();
        orderList.add(null);

        boolean isSuccessful = orderModel.cancelOrders(orderList, jwt);
        assertTrue(isSuccessful); // Order Model should just skip the null order
    }

    @Test
    public void cancelOrderCustomerCancelOwnOrder() {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("role", UserRoles.CUSTOMER.toString());
        String jwt = JWTUtil.generateToken("1", claimMap);

        List<Order> orderList = new ArrayList<>();
        Order order = new Order();
        order.setOrderId(1);
        order.setUserId(1);
        orderList.add(order);

        boolean isSuccessful = orderModel.cancelOrders(orderList, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void cancelOrderCustomerCancelOthersOrder() {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("role", UserRoles.CUSTOMER.toString());
        String jwt = JWTUtil.generateToken("1", claimMap);

        List<Order> orderList = new ArrayList<>();
        Order order = new Order();
        order.setOrderId(1);
        order.setUserId(1);
        orderList.add(order);

        boolean isSuccessful = orderModel.cancelOrders(orderList, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void cancelOrderSellerCancelOwnOrder() {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("role", UserRoles.SELLER.toString());
        claimMap.put("groupId", "1");
        String jwt = JWTUtil.generateToken("1", claimMap);

        List<Order> orderList = new ArrayList<>();
        Order order = new Order();
        order.setOrderId(1);
        order.setUserId(1);
        orderList.add(order);

        boolean isSuccessful = orderModel.cancelOrders(orderList, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void cancelOrderSellerCancelOtherOrder() {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("role", UserRoles.SELLER.toString());
        claimMap.put("groupId", "1");
        String jwt = JWTUtil.generateToken("1", claimMap);

        List<Order> orderList = new ArrayList<>();
        Order order = new Order();
        order.setOrderId(2);
        order.setUserId(2);
        orderList.add(order);

        boolean isSuccessful = orderModel.cancelOrders(orderList, jwt);
        assertFalse(isSuccessful);
    }

    @Test
    public void listingDoesNotExist() {
        orderItemList = new ArrayList<>();
        OrderItem oi = new OrderItem();
        oi.setQuantity(20);
        orderItemList.add(oi);
        boolean isSuccessful = orderModel.createOrderItem(orderItemList, new Order(), jwt);
        assertFalse(isSuccessful);
    }

    @Test
    public void inSufficientToSatisfyOrder() {
        orderItemList = new ArrayList<>();
        OrderItem oi = new OrderItem();
        oi.setQuantity(20);
        orderItemList.add(oi);

        boolean isSuccessful = orderModel.createOrderItem(orderItemList, order, jwt);
        assertFalse(isSuccessful);
    }

    @Test
    public void correctDetailsForOneOrderItem() {
        orderItemList = new ArrayList<>();
        OrderItem oi = new OrderItem();
        oi.setQuantity(1);
        oi.setListingId(1);
        orderItemList.add(oi);

        boolean isSuccessful = orderModel.createOrderItem(orderItemList, order, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void cancelOrderAdminCancelOrder() {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("role", UserRoles.ADMIN.toString());
        String jwt = JWTUtil.generateToken("1", claimMap);

        List<Order> orderList = new ArrayList<>();
        Order order = new Order();
        order.setOrderId(1);
        order.setUserId(1);
        orderList.add(order);

        boolean isSuccessful = orderModel.cancelOrders(orderList, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void correctDetailsForANumberOfOrderItems() {
        orderItemList = new ArrayList<>();
        OrderItem oi = new OrderItem();
        oi.setListingId(1);
        oi.setQuantity(1);
        OrderItem oi2 = new OrderItem();
        oi2.setQuantity(2);
        oi2.setListingId(2);
        orderItemList.add(oi);
        orderItemList.add(oi2);

        boolean isSuccessful = orderModel.createOrderItem(orderItemList, order, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void cancelOrderUnknownRoleOrder() {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("role", "Something");
        String jwt = JWTUtil.generateToken("1", claimMap);

        List<Order> orderList = new ArrayList<>();
        Order order = new Order();
        order.setOrderId(1);
        order.setUserId(1);

        boolean isSuccessful = orderModel.cancelOrders(orderList, jwt);
        assertFalse(isSuccessful);
    }
}
