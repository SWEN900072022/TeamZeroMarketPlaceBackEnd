package MockClasses;

import Domain.AuctionListing;
import Domain.FixedPriceListing;
import Domain.Listing;
import Enums.ListingTypes;
import Injector.FindConditionInjector.FindIdInjector;
import Injector.FindConditionInjector.FindListingWithGroupIdInjector;
import Injector.FindConditionInjector.FindOrderWIthGroupId;
import Injector.FindConditionInjector.FindTitleInjector;
import Injector.ISQLInjector;
import Mapper.Mapper;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockListingMapper implements Mapper<Listing> {
    // In-memory database
    public List<Listing> listings = new ArrayList<>();

    public MockListingMapper() {
        // Populate the listings with some dummy data
        Listing listing1 = Listing.create(
                FixedPriceListing.class,
                1,
                1,
                ListingTypes.FIXED_PRICE,
                "a",
                "a",
                5,
                Money.of(1, Monetary.getCurrency("AUD")),
                LocalDateTime.now(),
                LocalDateTime.now());

        Listing listing2 = Listing.create(
                AuctionListing.class,
                2,
                2,
                ListingTypes.AUCTION,
                "b",
                "b",
                2,
                Money.of(2, Monetary.getCurrency("AUD")),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2));

        listings.add(listing1);
        listings.add(listing2);
    }

    @Override
    public boolean insert(Listing TEntity) {
        listings.add(TEntity);
        return true;
    }

    @Override
    public boolean delete(Listing TEntity) {
        listings.removeIf(t -> (
                t.getListingId() == TEntity.getListingId()
                ));
        return true;
    }

    @Override
    public boolean modify(Listing TEntity) {
        delete(TEntity);
        insert(TEntity);
        return true;
    }

    @Override
    public Listing find(ISQLInjector injector, List<Object> queryParam) {
        for (Listing listing:listings) {
            if(injector instanceof FindTitleInjector) {
                if((String)queryParam.get(0) == listing.getTitle()) {
                    return listing;
                }
            }
            if(injector instanceof FindIdInjector) {
                if((Integer)queryParam.get(0) == listing.getListingId()) {
                    return listing;
                }
            }

            if(injector instanceof FindListingWithGroupIdInjector) {
                if((Integer)queryParam.get(0) == listing.getGroupId()) {
                    return listing;
                }
            }

            if(injector instanceof FindOrderWIthGroupId) {
                if((Integer)queryParam.get(0) == listing.getGroupId()) {
                    return listing;
                }
            }
        }
        return null;
    }

    @Override
    public List<Listing> findMulti(ISQLInjector injector, List<Object> queryParam) {
        List<Listing> result = new ArrayList<>();

        if(queryParam.size() == 0 || queryParam == null ) {
            return listings;
        }

        for (Listing listing:listings) {
            if(injector instanceof FindTitleInjector) {
                if((String)queryParam.get(0) == listing.getTitle()) {
                    result.add(listing);
                }
            }
            if(injector instanceof FindIdInjector) {
                if((Integer)queryParam.get(0) == listing.getListingId()) {
                    result.add(listing);
                }
            }

            if(injector instanceof FindListingWithGroupIdInjector) {
                if((Integer)queryParam.get(0) == listing.getGroupId()) {
                    result.add(listing);
                }
            }
        }

        return result;
    }

    @Override
    public void setConnection(Connection conn) {

    }
}
