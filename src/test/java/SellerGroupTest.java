import Entity.SellerGroup;
import Enums.UserRoles;
import MockClasses.MockRepository;
import Model.SellerGroupModel;
import UnitofWork.IUnitofWork;
import Util.JWTUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SellerGroupTest {
    private MockRepository repo;
    private SellerGroupModel sgModel;
    private String jwt;
    private SellerGroup sg;

    public SellerGroupTest() {
        this.repo = new MockRepository();
        sgModel = new SellerGroupModel(repo);
        jwt = JWTUtil.generateToken("1", new HashMap<>());
        sg = new SellerGroup();
    }

    @Test
    public void userCreateSellerGroup() {
        Map<String, String> claim = new HashMap<>();
        claim.put("role", UserRoles.CUSTOMER.toString());

        jwt = JWTUtil.generateToken("1", claim);
        boolean hasCreated = sgModel.createSellerGroup(sg, jwt);
        assertFalse(hasCreated);
    }

    @Test
    public void sellerCreateSellerGroup() {
        Map<String, String> claim = new HashMap<>();
        claim.put("role", UserRoles.SELLER.toString());

        jwt = JWTUtil.generateToken("1", claim);
        boolean hasCreated = sgModel.createSellerGroup(sg, jwt);
        assertFalse(hasCreated);
    }

    @Test
    public void adminCreateSellerGroup() {
        Map<String, String> claim = new HashMap<>();
        claim.put("role", UserRoles.ADMIN.toString());

        jwt = JWTUtil.generateToken("1", claim);

        SellerGroup sg1 = new SellerGroup();
        sg1.setGroupId(3);
        sg1.setGroupName("c");

        boolean hasCreated = sgModel.createSellerGroup(sg1, jwt);
        assertTrue(hasCreated);
    }

    @Test
    public void invalidSellerGroupToken() {
        boolean hasCreated = sgModel.createSellerGroup(new SellerGroup(), "");
        assertFalse(hasCreated);
    }

    @Test
    public void existingNameFound() {
        Map<String, String> claim = new HashMap<>();
        claim.put("role", UserRoles.ADMIN.toString());

        jwt = JWTUtil.generateToken("1", claim);
        SellerGroup sg1 = new SellerGroup();

        sg1.setGroupName("a");
        sg1.setGroupId(1);

        boolean hasCreated = sgModel.createSellerGroup(sg1, jwt);
        assertFalse(hasCreated);
    }
}
