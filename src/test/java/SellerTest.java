import Domain.Customer;
import Domain.Seller;
import Domain.User;
import Enums.UserRoles;
import MockClasses.MockRepository;
import org.junit.jupiter.api.Test;

public class SellerTest {
    private MockRepository repo;
    private Seller seller;

    public SellerTest() {
        this.repo = new MockRepository();
        this.seller = (Seller) User.create(
                "b",
                "b",
                "b",
                2,
                UserRoles.SELLER.toString()
        );
        seller.setRepo(repo);
    }

    @Test
    public void createListing() {

    }
}
