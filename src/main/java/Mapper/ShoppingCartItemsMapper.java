package Mapper;

import Entity.ShoppingCartItem;
import Injector.ISQLInjector;
import Util.SQLUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartItemsMapper extends GeneralMapper<ShoppingCartItem> {
    @Override
    public boolean insert(ShoppingCartItem TEntity) {
        try {
            insertOperation(TEntity, false);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private void insertOperation(ShoppingCartItem TEntity, boolean shouldReturn) throws SQLException {
        PreparedStatement statement;

        if(conn == null) {
            conn = SQLUtil.getConnection();
        }

        statement = conn.prepareStatement(
                "INSERT INTO shoppingcartitems (userId, listingId) " +
                        "VALUES (?, ?);"
        );
        statement.setInt(1, TEntity.getUserId());
        statement.setInt(2, TEntity.getListingId());
        statement.execute();
    }

    @Override
    public boolean modify(ShoppingCartItem TEntity) {
        try {
            if(conn == null) {
                conn = SQLUtil.getConnection();
            }

            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE shoppingcartitems as sci set " +
                            "userId=sci2.userId, " +
                            "listingId=sci2.listingId " +
                            "from (values " +
                            "(?, ?)" +
                            ") as sci2(userId, listingId) " +
                            "where sci.userId=sci2.userId and sci.listingId=sci2.listingId;"
            );
            statement.setInt(1, TEntity.getUserId());
            statement.setInt(2, TEntity.getListingId());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public ShoppingCartItem find(ISQLInjector injector, List<Object> queryParam) {
        ShoppingCartItem sci = new ShoppingCartItem();
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            if(rs.next()) {
                sci.setUserId(rs.getInt("userId"));
                sci.setListingId(rs.getInt("listingId"));
            }
        } catch (SQLException e) {
            return null;
        }
        return sci;
    }

    @Override
    public List<ShoppingCartItem> findMulti(ISQLInjector injector, List<Object> queryParam) {
        List<ShoppingCartItem> sciList = new ArrayList<>();
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            while (rs.next()) {
                ShoppingCartItem sci = new ShoppingCartItem();
                sci.setUserId(rs.getInt("userId"));
                sci.setListingId(rs.getInt("listingId"));
                sciList.add(sci);
            }
        } catch (SQLException e) {
            return null;
        }
        return sciList;
    }
}
