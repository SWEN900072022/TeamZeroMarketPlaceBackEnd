import Entity.Listing;
import Entity.Order;
import Entity.OrderItem;
import MockClasses.MockListingRepository;
import MockClasses.MockOrderItemRepository;
import MockClasses.MockOrderRepository;
import MockClasses.MockUserRepository;
import Model.OrderModel;
import UnitofWork.IUnitofWork;
import Util.JWTUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderModelTest {
    private MockOrderRepository orderRepo;
    private MockListingRepository listingRepo;
    private MockOrderItemRepository orderItemRepo;
    private MockUserRepository userRepo;
    private OrderModel orderModel;
    private String jwt;
    private Order order;
    private List<OrderItem> orderItemList;

    public OrderModelTest() {
        this.orderRepo = new MockOrderRepository();
        this.listingRepo = new MockListingRepository();
        this.orderItemRepo = new MockOrderItemRepository();
        this.userRepo = new MockUserRepository();
        this.orderModel = new OrderModel(orderRepo, listingRepo, orderItemRepo, userRepo);

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
    public void listingDoesNotExist() {
        listingRepo.isNull = true;
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
        orderItemList.add(oi);

        boolean isSuccessful = orderModel.createOrderItem(orderItemList, order, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void correctDetailsForANumberOfOrderItems() {
        orderItemList = new ArrayList<>();
        OrderItem oi = new OrderItem();
        oi.setQuantity(1);
        OrderItem oi2 = new OrderItem();
        oi.setQuantity(2);
        orderItemList.add(oi);

        boolean isSuccessful = orderModel.createOrderItem(orderItemList, order, jwt);
        assertTrue(isSuccessful);
    }
}
