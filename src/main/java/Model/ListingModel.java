package Model;

import Entity.Filter;
import Entity.Listing;
import Enums.UserRoles;
import Injector.DeleteConditionInjector.DeleteIdInjector;
import Injector.FindConditionInjector.*;
import Injector.IInjector;
import Mapper.ListingMapper;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import Util.JWTUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListingModel {
    private IUnitofWork<Listing> repo;
    public ListingModel() {
        repo = new Repository<Listing>(new ListingMapper());
    }

    public ListingModel(IUnitofWork<Listing> repo) {
        this.repo = repo;
    }

    public boolean createListing(Listing listing, String jwt) {
        // Check to see if the jwt token is valid
        try{
            if(!JWTUtil.validateToken(jwt)) {
                // if not valid, return false
                return false;
            }
        } catch (Exception e) {
            // Something went wrong
            return false;
        }

        repo.registerNew(listing);
        try{
            repo.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<Listing> search(List<Filter> filterConditions, int limit, int offset, String jwt) {
        // Check to see if the jwt token is valid
        try{
            if(!JWTUtil.validateToken(jwt)) {
                // if not valid, return false
                return new ArrayList<>();
            }
        } catch (Exception e) {
            // Something went wrong
            return new ArrayList<>();
        }

        List<Listing> result = new ArrayList<>();

        if(filterConditions == null || filterConditions.isEmpty() ) {
            List<Object> param = new ArrayList<>();
            param.add(limit);
            param.add(offset);
            result = repo.readMulti(new FindAllInjector("listings"), param);
            return result;
        }

        // Populate the custom findInjector
        for(Filter filter : filterConditions) {
            IInjector inj = getInjector(filter.getFilterKey());

            List<Object> param = new ArrayList<>();
            param.add(filter.getFilterVal());
            param.add(limit);
            param.add(offset);
            List<Listing>temp = repo.readMulti(inj, param);

            if(result.size() == 0) {
                result = temp;
            } else {
                // This is an AND and we want to find the intersection
                Set<Listing> resultSet = result.stream()
                        .distinct()
                        .filter(temp::contains)
                        .collect(Collectors.toSet());
                result = new ArrayList<>(resultSet);
            }
        }
        return result;
    }

    public boolean delete(Integer listingId, String jwt) {
        // Check token, verify that the user can delete the listing and commit the change
        String role;
        try {
            if(!JWTUtil.validateToken(jwt)) {
                return false;
            }
            role = JWTUtil.getClaim("role", jwt);
        } catch (Exception e) {
            return false;
        }

        if(role == null || role == "") {
            return false;
        }

        // Switch between different users
        if(role.equals(UserRoles.SELLER.toString())) {
            // check to see if the seller owns the listing
            // delete if yes
            List<Object> param = new ArrayList<>();
            param.add(listingId);
            Listing listing = repo.read(new FindIdInjector("listings"), param);

            // Check to see if the listing is valid
            if(listing == null || (listing.isEmptyAuction() && listing.isEmptyFixedPrice())) {
                return false;
            }

            //Check to see if the jwt groupid is the same as the one in listings
            int groupId = Integer.parseInt(JWTUtil.getClaim("groupId", jwt));
            if(groupId != listing.getGroupId()) {
                // listing is not the groups, reject
                return false;
            }

            // We should have a valid listing and correctly owned listing
            // Deletion

            param = new ArrayList<>();
            param.add(listing.getListingId());

            listing.setInjector(new DeleteIdInjector("listings"));
            listing.setParam(param);

            repo.registerDeleted(listing);
        }

        if(role.equals(UserRoles.ADMIN.toString())) {
            // just delete the listing
            List<Object> param = new ArrayList<>();
            param.add(listingId);
            Listing listing = repo.read(new FindIdInjector("listings"), param);

            // Check to see if the listing is valid
            if(listing == null) {
                return false;
            }
            param = new ArrayList<>();
            param.add(listing.getListingId());

            listing.setInjector(new DeleteIdInjector("listings"));
            listing.setParam(param);
            repo.registerDeleted(listing);
        }

        try {
            repo.commit();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    private IInjector getInjector(String key) {
        switch(key) {
            case "title":
                return new FindTitleInjector();
            case "groupId":
                return new FindListingWithGroupIdInjector();
        }
        return null;
    }
}
