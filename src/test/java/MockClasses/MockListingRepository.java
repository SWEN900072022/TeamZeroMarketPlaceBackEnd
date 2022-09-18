package MockClasses;

import Entity.Listing;
import Injector.FindConditionInjector.FindGroupNameInListing;
import Injector.FindConditionInjector.FindListingWithGroupIdInjector;
import Injector.FindConditionInjector.FindTitleInjector;
import Injector.IInjector;
import UnitofWork.IUnitofWork;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MockListingRepository implements IUnitofWork<Listing> {
    private Listing listing = new Listing();
    public boolean isNull = false;
    public boolean commitException = false;
    public boolean isGroup = false;
    public boolean isBidEnd = false;
    private List<Listing>listingList = new ArrayList<>();

    public MockListingRepository() {
        listing = new Listing();
        listing.setListingId(1);
        listing.setQuantity(10);
        listing.setTitle("a");
        listing.setGroupId(1);
        listing.setStartTime(LocalDateTime.now().minusHours(2));
        listing.setEndTime(LocalDateTime.now().plusHours(2));

        Listing listing1 = new Listing();
        listing1.setTitle("b");
        listing1.setGroupId(2);

        Listing listing2 = new Listing();
        listing2.setTitle("a");
        listing2.setGroupId(2);

        listingList.add(listing);
        listingList.add(listing1);
        listingList.add(listing2);
    }

    @Override
    public Listing read(IInjector injector, List<Object> param, String key) {
        if(isNull) {
            return null;
        }

        if(!isGroup) {
            return new Listing();
        }

        if(isBidEnd) {
            listing.setEndTime(LocalDateTime.now().minusHours(1));
            return listing;
        }

        return listing;
    }

    @Override
    public List<Listing> readMulti(IInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public Listing read(IInjector injector, List<Object> param) {
        if(isNull) {
            return null;
        }

        if(!isGroup) {
            return new Listing();
        }

        if(isBidEnd) {
            listing.setEndTime(LocalDateTime.now().minusHours(1));
            return listing;
        }

        return listing;
    }

    @Override
    public List<Listing> readMulti(IInjector injector, List<Object> param) {
        if(param.size() == 0) {
            return this.listingList;
        }
        // Get the title param
        String paramString = (String)param.get(0);
        List<Listing> result = new ArrayList<>();
        // Check the injector and depending on which injector it is return the corrsponding list
        if (injector instanceof FindTitleInjector) {
            for(Listing listing : listingList) {
                if(Objects.equals(listing.getTitle(), paramString)) {
                    result.add(listing);
                }
            }
        } else if (injector instanceof FindListingWithGroupIdInjector) {
            for(Listing listing : listingList) {
                if(listing.getGroupId() == Integer.parseInt(paramString)) {
                    result.add(listing);
                }
            }
        }
        return result;
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
    public void commit() {
    }
}

