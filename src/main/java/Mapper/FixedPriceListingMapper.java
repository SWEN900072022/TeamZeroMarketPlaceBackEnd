package Mapper;

import Entity.FixedPriceListing;
import Entity.FixedPriceListingImpl;
import Entity.Listing;
import Enums.ListingTypes;
import Util.Util;

import java.sql.*;
import java.util.*;

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
    public Map<Integer, FixedPriceListing> find(Map<String, String> map) {
        return find(map, 0);
    }

    @Override
    public Map<Integer, FixedPriceListing> find(Map<String, String> map, int mode) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM fixed_price_listing");
        PreparedStatement statement;
        Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
        Map<Integer, FixedPriceListing> list = new HashMap<>();
        ResultSet rs;

        if(itr.hasNext()) {
            sb.append(" WHERE ");
        } else {
            sb.append(";");
        }

        while(itr.hasNext()) {
            Map.Entry<String, String> entry = itr.next();
            sb.append(String.format("%s='%s'", entry.getKey(), entry.getValue()));
            if(itr.hasNext()) {
                if(mode == 0) { // 0 for and, 1 for or
                    sb.append(" AND ");
                } else {
                    sb.append(" OR ");
                }
            } else {
                sb.append(";");
            }
        }

        try {
            if(conn == null) {
                conn = Util.getConnection();
            }
            statement = conn.prepareStatement(sb.toString());
            statement.execute();

            rs = statement.getResultSet();
            while(rs.next()) {
                FixedPriceListing listing = new FixedPriceListingImpl(); // We default to a fixed price listing
                listing.setPrice(rs.getInt("price"));
                listing.setQuantity(rs.getInt("quantity"));
                listing.setFplId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
}
