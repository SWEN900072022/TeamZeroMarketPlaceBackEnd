import Entity.Listing;
import Injector.FindConditionInjector;
import Model.ListingModel;
import UnitofWork.IUnitofWork;
import Util.JWTUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListingModelTest {
    private ListingModel listingModel;
    private MockListingRepository repo;

    public ListingModelTest() {
        repo = new MockListingRepository();
        listingModel = new ListingModel(repo);
    }

    @Test
    public void successfulFixedPriceListingCreation() {
        // Should have a valid jwt token and no exceptions in the commit stage
        String jwt = JWTUtil.generateToken("a", new HashMap<>()); // valid token
        Listing testListing = new Listing();
        boolean result = listingModel.createListing(testListing, jwt);
        assertTrue(result);
    }

    @Test
    public void incorrectJWTToken() {
        String jwt = "";
        Listing testListing = new Listing();
        boolean result = listingModel.createListing(testListing, jwt);
        assertFalse(result);
    }

    class MockListingRepository implements IUnitofWork<Listing> {
        // State variables
        private boolean commitFaults;

        public MockListingRepository() {
            this.commitFaults = false;
        }

        public boolean isCommitFaults() {
            return commitFaults;
        }

        public void setCommitFaults(boolean commitFaults) {
            this.commitFaults = commitFaults;
        }

        @Override
        public Listing read(FindConditionInjector injector, List<Object> param, String key) {
            return null;
        }

        @Override
        public List<Listing> readMulti(FindConditionInjector injector, List<Object> param, String key) {
            return null;
        }

        @Override
        public Listing read(FindConditionInjector injector, List<Object> param) {
            return null;
        }

        @Override
        public List<Listing> readMulti(FindConditionInjector injector, List<Object> param) {
            return null;
        }

        @Override
        public void registerNew(Listing entity) {

        }

        @Override
        public void registerModified(Listing entity) {

        }

        @Override
        public void registerDeleted(Listing entity) {

        }

        @Override
        public void commit(){
            // Throw exception if there are commit faults
        }
    }
}
