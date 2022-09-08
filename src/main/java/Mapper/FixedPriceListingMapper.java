package Mapper;

import Entity.FixedPriceListing;
import Entity.Listing;
import Util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class FixedPriceListingMapper extends Mapper<FixedPriceListing> {
    private Connection conn = null;
    @Override
    public boolean insert(List<FixedPriceListing> listingList) {
        try {
            insertOperation(listingList, false);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private List<FixedPriceListing> insertOperation(List<FixedPriceListing>listingList, boolean shouldReturn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        PreparedStatement statement;
        ListIterator<FixedPriceListing> listingListIterator = listingList.listIterator();
        sb.append("INSERT INTO fixed_price_listing (general_listing_id, price, quantity) VALUES");

        while(listingListIterator.hasNext()) {
            FixedPriceListing listing = listingListIterator.next();
            sb.append(String.format("('%s','%s','%s')",
                            listing.getId(),
                            listing.getPrice(),
                            listing.getQuantity()));
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
            return new ArrayList<>();
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        listingListIterator = listingList.listIterator();
        while(generatedKeys.next() && listingListIterator.hasNext()) {
            FixedPriceListing listing = listingListIterator.next();
            listing.setId(generatedKeys.getInt(1));
        }

        return listingList;
    }

    @Override
    public boolean delete(List<FixedPriceListing> TEntity) {
        return false;
    }

    @Override
    public boolean modify(List<FixedPriceListing> TEntity) {
        return false;
    }

    @Override
    public List<FixedPriceListing> find(Map<String, String> map) {
        return null;
    }

    @Override
    public List<FixedPriceListing> find(Map<String, String> map, int mode) {
        return null;
    }
}
