package Mapper;

import Entity.FixedPriceListingImpl;
import Entity.Listing;
import Enums.ListingTypes;
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
                            listing.getTypeString(),
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
            return new ArrayList<>(); // Return an empty list if should return is false
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
        List<String> fieldsToBeUpdated = Listing.getListAttributes();
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE listing as l set ");
        Iterator<String> fItr = fieldsToBeUpdated.listIterator();

        while(fItr.hasNext()) {
            String field = fItr.next();
            sb.append(String.format("%s=l2.%s", field, field));
            if(fItr.hasNext()) {
                sb.append(",");
            }
        }

        sb.append(" from ( values ");
        Iterator<Listing> listingIterator = listingList.listIterator();
        while(listingIterator.hasNext()) {
            Listing listing = listingIterator.next();
            sb.append(String.format("(%d, %s, %s, %s, %d)",
                    listing.getId(),
                    listing.getType(),
                    listing.getDescription(),
                    listing.getTitle(),
                    listing.getCreatedById()));

            if(fItr.hasNext()) {
                sb.append(",");
            }
        }

        sb.append(" ) as l2(");
        fItr = fieldsToBeUpdated.listIterator();

        while(fItr.hasNext()) {
            String field = fItr.next();
            sb.append(String.format("%s", field));
            if(fItr.hasNext()) {
                sb.append(",");
            } else {
                sb.append(")");
            }
        }

        sb.append("where l.id=l2.id;");

        try {
            if(conn == null) {
                conn = Util.getConnection();
            }
            PreparedStatement statement = conn.prepareStatement(sb.toString());
            statement.execute();
        } catch (SQLException e) {
            // Something went wrong, return false
            return false;
        }
        return true;
    }

    public Map<Integer, Listing> find(Map<String, String> map) {
        return find(map, 0);
    }

    public Map<Integer, Listing> find(Map<String, String>map, int mode) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM listing");
        PreparedStatement statement;
        Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
        Map<Integer, Listing> list = new HashMap<>();
        ResultSet rs;

        if(itr.hasNext()) {
            sb.append(" WHERE ");
        } else {
            sb.append(";");
        }

        while(itr.hasNext()) {
            Map.Entry<String, String> entry = itr.next();
            sb.append(String.format("%s in %s", entry.getKey(), entry.getValue()));
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
                Listing listing = new FixedPriceListingImpl(); // We default to a fixed price listing
                listing.setDescription(rs.getString("description"));
                listing.setTitle(rs.getString("title"));
                listing.setType(ListingTypes.fromString(rs.getString("type")));
                listing.setId(rs.getInt("id"));
                listing.setCreatedById(rs.getInt("created_by_id"));
                list.put(listing.getId(), listing);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
}
