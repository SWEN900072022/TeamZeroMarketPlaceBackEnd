package Mapper;

import Entity.FixedPriceListing;
import Entity.FixedPriceListingImpl;
import Entity.Listing;
import Enums.ListingTypes;
import Injector.FindConditionInjector;
import Util.Util;

import java.sql.*;
import java.util.*;

public class FixedPriceListingMapper extends Mapper<FixedPriceListing> {
    private Connection conn = null;
    @Override
    public boolean insert(FixedPriceListing listing) {
        try {
            insertOperation(listing, false);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private FixedPriceListing insertOperation(FixedPriceListing listing, boolean shouldReturn) throws SQLException {
        PreparedStatement statement;

        if(conn == null) {
            conn = Util.getConnection();
        }
        statement = conn.prepareStatement(
                "INSERT INTO fixed_price_listing (general_listing_id, price, quantity) " +
                        "VALUES (? ,?, ?);",
                Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, listing.getId());
        statement.setInt(2, listing.getPrice());
        statement.setInt(3, listing.getQuantity());
        statement.execute();

        if(!shouldReturn) {
            return null;
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        while(generatedKeys.next()) {
            listing.setId(generatedKeys.getInt(1));
        }

        return listing;
    }

    @Override
    public boolean delete(FixedPriceListing listing) {
        return false;
    }

    @Override
    public boolean modify(FixedPriceListing listing) {
        try {
            PreparedStatement statement;

            if(conn == null) {
                conn = Util.getConnection();
            }

            statement = conn.prepareStatement(
                    "UPDATE fixed_price_listing as fpl set " +
                            "id=fpl2.id, " +
                            "price=fpl2.price, " +
                            "quantity=fpl2.quantity, " +
                            "general_listing_id=fpl2.general_listing_id " +
                            "from (values " +
                            "(?, ?, ?, ?) " +
                            ") as fpl2(id, price, quantity, general_listing_id) " +
                            "where fpl2.id=fpl.id;");
            statement.setInt(1, listing.getFplId());
            statement.setInt(2, listing.getPrice());
            statement.setInt(3, listing.getQuantity());
            statement.setInt(4, listing.getId());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public FixedPriceListing find(FindConditionInjector injector, List<Object> queryParam) {
        FixedPriceListing listing = new FixedPriceListingImpl(); // We default to a fixed price listing
        try {
            PreparedStatement statement;

            if(conn == null) {
                conn = Util.getConnection();
            }

            statement = conn.prepareStatement(injector.getSQLQuery());
            for(int i = 1; i <= queryParam.size(); i++) {
                Object param = queryParam.get(i-1);
                if(param instanceof Integer) {
                    statement.setInt(i, (Integer)param);
                } else if(param instanceof String) {
                    statement.setString(i, (String)param);
                }
            }
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while(rs.next()) {
                listing.setPrice(rs.getInt("price"));
                listing.setQuantity(rs.getInt("quantity"));
                listing.setFplId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            return null;
        }
        return listing;
    }
    public List<FixedPriceListing> findAllItems(FindConditionInjector injector){
        List <FixedPriceListing> allListings= new ArrayList<FixedPriceListing>();
        PreparedStatement statement;
        ResultSet rs;
        try{
            if(conn==null){
                conn = Util.getConnection();
            }
            statement = conn.prepareStatement(injector.getSQLQuery());
            rs = statement.executeQuery();
            while(rs.next()){
                FixedPriceListing listing = new FixedPriceListingImpl();
                listing.setPrice(rs.getInt("price"));
                listing.setQuantity(rs.getInt("quantity"));
                listing.setFplId(rs.getInt("id"));
                allListings.add(listing);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return allListings;
    }
}
