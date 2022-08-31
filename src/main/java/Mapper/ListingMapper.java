package Mapper;

import Entity.Listing;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ListingMapper extends Mapper<Listing>{
    public ListingMapper() {

    }

    public boolean insert(List<Listing> listingList) {
        return true;
    }

    public boolean delete(List<Listing> listingList) {
        return true;
    }

    public boolean modify(List<Listing> listingList) {
        return true;
    }

    public List<Listing> find(Map<String, String> map) {
        return find(map, 0);
    }

    public List<Listing> find(Map<String, String>map, int mode) {
        return new ArrayList<>();
    }
}
