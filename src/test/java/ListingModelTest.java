import Entity.Filter;
import Entity.Listing;
import MockClasses.MockListingRepository;
import Model.ListingModel;
import Util.JWTUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListingModelTest {
    private MockListingRepository listingRepository;
    private ListingModel listingModel;
    private String jwt;

    public ListingModelTest() {
        listingRepository = new MockListingRepository();
        listingModel = new ListingModel(listingRepository);
        jwt = JWTUtil.generateToken("1", new HashMap<>());
    }

    @Test
    /**
     * Test for the behaviour when we search for listings based on titles
     */
    public void searchByTitle() {
        Filter filter = new Filter("title", "a");
        List<Filter> filterList = new ArrayList<>();
        filterList.add(filter);
        List<Listing> list = listingModel.search(filterList, jwt);
        assertEquals(2, list.size());

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
        Filter filter = new Filter("groupName", "2");
        List<Filter> filterList = new ArrayList<>();
        filterList.add(filter);
        List<Listing> list = listingModel.search(filterList, jwt);
        assertEquals(2, list.size());

        filter = new Filter("groupName", "1");
        filterList = new ArrayList<>();
        filterList.add(filter);
        list = listingModel.search(filterList, jwt);
        assertEquals(1, list.size());
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
        assertEquals(3, list.size());
    }

    @Test
    /**
     * Test the behaviour when an empty filter condition list is provided
     */
    public void searchWithNoFilterCondition() {
        List<Listing> list = listingModel.search(new ArrayList<>(), jwt);
        assertEquals(3, list.size());
    }
}
