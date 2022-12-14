package Mapper;

import Domain.Listing;
import Enums.ListingTypes;
import Injector.ISQLInjector;
import Util.SQLUtil;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ListingMapper extends GeneralMapper<Listing> {
    public ListingMapper() {

    }

    public boolean insert(Listing listing) {
        try {
            insertOperation(listing, false);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public Listing insertWithResultSet(Listing listing) {
        try {
            listing = insertOperation(listing, true);
        } catch (SQLException e) {
            return null;
        }
        return listing;
    }

    private Listing insertOperation(Listing listing, boolean shouldReturn) throws SQLException {
        PreparedStatement statement;

        statement = conn.prepareStatement(
                "INSERT INTO listings (groupId, type, title, description, quantity, price, startTime, endTime) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS
        );
        statement.setInt(1, listing.getGroupId());
        statement.setString(2, listing.getType().toString());
        statement.setString(3, listing.getTitle());
        statement.setString(4, listing.getDescription());
        statement.setInt(5, listing.getQuantity());
        statement.setBigDecimal(6, listing.getPrice().getNumberStripped());
        statement.setObject(7, listing.getStartTime());
        statement.setObject(8, listing.getEndTime());
        statement.execute();

        if(!shouldReturn) {
            return null; // Return an empty list if should return is false
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        while(generatedKeys.next()) {
            listing.setListingId(generatedKeys.getInt("listingId"));
        }
        return listing;
    }

    public boolean modify(Listing listing) {
        try {

            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE listings as l set " +
                            "groupid=l2.groupId, " +
                            "type=l2.type, " +
                            "title=l2.title, " +
                            "description=l2.description, " +
                            "quantity=l2.quantity, " +
                            "price=l2.price, " +
                            "starttime=l2.startTime, " +
                            "endtime=l2.endTime," +
                            "listingid = l2.listingId " +
                            "from (values " +
                            "(?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                            ") as l2(groupId, type, title, description, quantity, price, startTime, endTime, listingId) " +
                            "where l.listingId=l2.listingId;");
            statement.setInt(1, listing.getGroupId());
            statement.setString(2, listing.getType().toString());
            statement.setString(3, listing.getTitle());
            statement.setString(4, listing.getDescription());
            statement.setInt(5, listing.getQuantity());
            statement.setBigDecimal(6, listing.getPrice().getNumberStripped());
            statement.setObject(7, listing.getStartTime());
            statement.setObject(8, listing.getEndTime());
            statement.setObject(9, listing.getListingId());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public Listing find(ISQLInjector injector, List<Object> queryParam) {
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            if(rs.next()) {
                return Listing.create(
                        rs.getInt("listingId"),
                        rs.getInt("groupId"),
                        ListingTypes.fromString(rs.getString("type")),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("quantity"),
                        Money.of(rs.getBigDecimal("price"), Monetary.getCurrency("AUD")),
                        rs.getObject("startTime", LocalDateTime.class),
                        rs.getObject("endTime", LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public List<Listing> findMulti(ISQLInjector injector, List<Object> queryParam) {
        List<Listing> listingList = new ArrayList<>();
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            while(rs.next()) {
                Listing listing = Listing.create(
                        rs.getInt("listingId"),
                        rs.getInt("groupId"),
                        ListingTypes.fromString(rs.getString("type")),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("quantity"),
                        Money.of(rs.getBigDecimal("price"), Monetary.getCurrency("AUD")),
                        rs.getObject("startTime", LocalDateTime.class),
                        rs.getObject("endTime", LocalDateTime.class)
                );
                listingList.add(listing);
            }
        } catch (SQLException e) {
            return null;
        }
        return listingList;
    }


}
