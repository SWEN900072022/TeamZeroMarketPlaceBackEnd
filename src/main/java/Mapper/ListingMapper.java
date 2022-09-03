package Mapper;

import Entity.Listing;
import Util.Util;

import java.sql.*;
import java.util.*;

public class ListingMapper extends Mapper<Listing>{
    private Connection conn = null;
    public ListingMapper() {

    }

    public boolean insert(List<Listing> listingList) {
        try {
            insertOperation(listingList, false);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public List<Listing> insertWithResultSet(List<Listing> listingList) {
        try {
            listingList = insertOperation(listingList, true);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
        return listingList;
    }

    private List<Listing> insertOperation(List<Listing>listingList, boolean shouldReturn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        PreparedStatement statement;
        ListIterator<Listing> listingListIterator = listingList.listIterator();
        sb.append("INSERT INTO listing (description, title, type, created_by_id) VALUES");

        while(listingListIterator.hasNext()) {
            Listing listing = listingListIterator.next();
            sb.append(String.format("('%s','%s','%s', '%s')",
                            listing.getDescription(),
                            listing.getTitle(),
                            listing.getType(),
                            listing.getCreatedById()));
            if(!listingListIterator.hasNext()) {
                sb.append(";");
            } else {
                sb.append(",");
            }
        }

        if(conn == null) {
            conn = Util.getConnection();
        }
        statement = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
        statement.execute();

        if(!shouldReturn) {
            return new ArrayList<>(); // Return an empty list if shoudl return is false
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        listingListIterator = listingList.listIterator();
        while(generatedKeys.next() && listingListIterator.hasNext()) {
            Listing listing = listingListIterator.next();
            listing.setId(generatedKeys.getInt("id"));
        }
        return listingList;
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
