package Mapper;

import Entity.FixedPriceListingImpl;
import Entity.Listing;
import Enums.ListingTypes;
import Injector.FindConditionInjector;
import Util.Util;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.*;

public class ListingMapper extends Mapper<Listing>{
    private Connection conn = null;
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

        if(conn == null) {
            conn = Util.getConnection();
        }

        statement = conn.prepareStatement(
                "INSERT INTO listing (description, title, created_by_id, type) " +
                        "VALUES (?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS
        );
        statement.setString(1, listing.getDescription());
        statement.setString(2, listing.getTitle());
        statement.setInt(3, listing.getCreatedById());
        statement.setString(4, listing.getTypeString());
        statement.execute();

        if(!shouldReturn) {
            return null; // Return an empty list if should return is false
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        while(generatedKeys.next()) {
            listing.setId(generatedKeys.getInt("id"));
        }
        return listing;
    }

    public boolean delete(Listing listingList) {
        return true;
    }

    public boolean modify(Listing listing) {
        try {
            if(conn == null) {
                conn = Util.getConnection();
            }

            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE listing as l set " +
                            "description=l2.description, " +
                            "title=l2.title, " +
                            "created_by_id=l2.created_by_id, " +
                            "id=l2.id, " +
                            "type=l2.type " +
                            "from (values " +
                            "(?, ?, ?, ?, ?)" +
                            ") as l2(description, title, created_by_id, id, type) " +
                            "where l.id=l2.id;");
            statement.setString(1, listing.getDescription());
            statement.setString(2, listing.getTitle());
            statement.setInt(3, listing.getCreatedById());
            statement.setInt(4, listing.getId());
            statement.setString(5, listing.getTypeString());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public Listing find(FindConditionInjector injector, List<Object> queryParam) {
        Listing listing = new FixedPriceListingImpl();
        try {
            PreparedStatement statement;

            if(conn == null) {
                conn = Util.getConnection();
            }

            statement = conn.prepareStatement(injector.getSQLQuery());
            for(int i = 1; i <= queryParam.size(); i++) {
                Object param = queryParam.get(i);
                if(param instanceof Integer) {
                    statement.setInt(i, (Integer)param);
                } else if(param instanceof String) {
                    statement.setString(i, (String)param);
                }
            }
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while(rs.next()) {
                listing.setDescription(rs.getString("description"));
                listing.setTitle(rs.getString("title"));
                listing.setType(ListingTypes.fromString(rs.getString("type")));
                listing.setId(rs.getInt("id"));
                listing.setCreatedById(rs.getInt("created_by_id"));
            }
        } catch (SQLException e) {
            return null;
        }
        return listing;
    }
}
