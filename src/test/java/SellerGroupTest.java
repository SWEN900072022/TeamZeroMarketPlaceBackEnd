import Entity.SellerGroup;
import Enums.UserRoles;
import MockClasses.MockSellerGroupRepository;
import Model.SellerGroupModel;
import UnitofWork.IUnitofWork;
import Util.JWTUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SellerGroupTest {
    private MockSellerGroupRepository sgRepo;
    private SellerGroupModel sgModel;
    private String jwt;
    private SellerGroup sg;

    public SellerGroupTest() {
        sgRepo = new MockSellerGroupRepository();
        sgModel = new SellerGroupModel(sgRepo);
        jwt = JWTUtil.generateToken("1", new HashMap<>());
        sg = new SellerGroup();
    }

    @Test
    public void userCreateSellerGroup() {
        this.sgRepo.isEmpty = true;
        Map<String, String> claim = new HashMap<>();
        claim.put("role", UserRoles.CUSTOMER.toString());

        jwt = JWTUtil.generateToken("1", claim);
        boolean hasCreated = sgModel.createSellerGroup(sg, jwt);
        assertFalse(hasCreated);
    }

    @Test
    public void sellerCreateSellerGroup() {
        this.sgRepo.isEmpty = true;
        Map<String, String> claim = new HashMap<>();
        claim.put("role", UserRoles.SELLER.toString());

        jwt = JWTUtil.generateToken("1", claim);
        boolean hasCreated = sgModel.createSellerGroup(sg, jwt);
        assertFalse(hasCreated);
    }

    @Test
    public void adminCreateSellerGroup() {
        this.sgRepo.isEmpty = true;
        Map<String, String> claim = new HashMap<>();
        claim.put("role", UserRoles.ADMIN.toString());

        jwt = JWTUtil.generateToken("1", claim);
        boolean hasCreated = sgModel.createSellerGroup(sg, jwt);
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
        boolean hasCreated = sgModel.createSellerGroup(sg, jwt);
        assertFalse(hasCreated);
    }
}
