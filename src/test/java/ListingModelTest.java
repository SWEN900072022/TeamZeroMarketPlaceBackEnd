import Entity.Filter;
import Entity.Listing;
import Enums.UserRoles;
import MockClasses.MockRepository;
import Model.ListingModel;
import Util.JWTUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ListingModelTest {
    private MockRepository repo;
    private ListingModel listingModel;
    private String jwt;

    public ListingModelTest() {
        this.repo = new MockRepository();
        listingModel = new ListingModel(repo);
        jwt = JWTUtil.generateToken("1", new HashMap<>());
    }

    @Test
    public void searchByTitle() {
        Filter filter = new Filter("title", "a");
        List<Filter> filterList = new ArrayList<>();
        filterList.add(filter);
        List<Listing> list = listingModel.search(filterList, jwt);
        assertEquals(1, list.size());

        filter = new Filter("title", "b");
        filterList = new ArrayList<>();
        filterList.add(filter);
        list = listingModel.search(filterList, jwt);
        assertEquals(1, list.size());
    }

    @Test
    /**
     * Test for the behaviour when we search for listings based on the seller groups selling them
     */
    public void searchByGroupName() {
        Filter filter = new Filter("groupId", 2);
        List<Filter> filterList = new ArrayList<>();
        filterList.add(filter);
        List<Listing> list = listingModel.search(filterList, jwt);
        assertEquals(1, list.size());

        filter = new Filter("groupId", 1);
        filterList = new ArrayList<>();
        filterList.add(filter);
        list = listingModel.search(filterList, jwt);
        assertEquals(1, list.size());
    }

    @Test
    public void InvalidJWTWhenCreatingListing() {
        boolean isSuccessful = listingModel.createListing(new Listing(), "");
        assertFalse(isSuccessful);
    }

    @Test
    /**
     * Test for the behaviour when an incorrect JWT Token is supplied
     */
    public void searchIncorrectJWTToken() {
        Filter filter = new Filter("groupName", "2");
        List<Filter> filterList = new ArrayList<>();
        filterList.add(filter);
        List<Listing> list = listingModel.search(filterList, "");
        assertEquals(0, list.size());
    }

    @Test
    /**
     * Test the behaviour when a null filter condition list are provided
     */
    public void searchNullFilterCondition() {
        List<Listing> list = listingModel.search(null, jwt);
        assertEquals(2, list.size());
    }

    @Test
    /**
     * Test the behaviour when an empty filter condition list is provided
     */
    public void searchWithNoFilterCondition() {
        List<Listing> list = listingModel.search(new ArrayList<>(), jwt);
        assertEquals(2, list.size());
    }

    @Test
    public void IdealListingCreation() {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("role", UserRoles.SELLER.toString());
        claimMap.put("groupId", "1");
        jwt = JWTUtil.generateToken("1", claimMap);

        Listing listing = new Listing();
        boolean isSuccessful = listingModel.createListing(listing, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void IdealModifyListingBuyer() {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("role", UserRoles.CUSTOMER.toString());
        jwt = JWTUtil.generateToken("1", claimMap);

        boolean isSuccessful = listingModel.modifyListing(1, 2, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void IdealModifyListingSeller(){
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("role", UserRoles.SELLER.toString());
        claimMap.put("groupId", "1");
        jwt = JWTUtil.generateToken("1", claimMap);

        boolean isSuccessful = listingModel.modifyListing(1, 3, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void ModifyListingIncorrectListingId() {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put("role", UserRoles.CUSTOMER.toString());
        jwt = JWTUtil.generateToken("1", claimMap);

        boolean isSuccessful = listingModel.modifyListing(3, 3, jwt);
        assertFalse(isSuccessful);
    }

    @Test
    public void ModifyListingIncorrectJWT() {
        boolean isSuccessful = listingModel.modifyListing(1, 2, "");
        assertFalse(isSuccessful);
    }
}
