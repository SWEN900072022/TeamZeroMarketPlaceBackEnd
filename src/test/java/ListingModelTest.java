import Entity.Listing;
import MockClasses.MockListingRepository;
import Model.ListingModel;
import Util.JWTUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListingModelTest {
    private MockListingRepository mockListingRepository;
    private ListingModel listingModel;
    private String jwt;

    public ListingModelTest() {
        this.mockListingRepository = new MockListingRepository();
        listingModel = new ListingModel(mockListingRepository);
        jwt = JWTUtil.generateToken("1", new HashMap<>());
    }

    @Test
    public void InvalidJWTWhenCreatingListing() {
        boolean isSuccessful = listingModel.createListing(new Listing(), "");
        assertFalse(isSuccessful);
    }

    @Test
    public void IdealListingCreation() {
        Listing listing = new Listing();
        boolean isSuccessful = listingModel.createListing(listing, jwt);
        assertTrue(isSuccessful);
    }
}
