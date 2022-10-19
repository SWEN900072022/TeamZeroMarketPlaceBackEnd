package Mapper;

import Domain.Bid;
import Injector.ISQLInjector;
import Util.SQLUtil;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BidMapper extends GeneralMapper<Bid>{
    @Override
    public boolean insert(Bid TEntity) {
        try {
            insertOperation(TEntity, false);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private void insertOperation(Bid bid, boolean shouldReturn) throws SQLException {
        PreparedStatement statement;

        statement = conn.prepareStatement(
                "INSERT INTO bids (listingid, userid, bidamount) " +
                        "VALUES (?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS
        );
        statement.setInt(1, bid.getListingId());
        statement.setInt(2, bid.getUserId());
        statement.setBigDecimal(3, bid.getBidAmount().getNumberStripped());
        statement.execute();
    }

    @Override
    public boolean modify(Bid TEntity) {
        try {

            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE bids as b set " +
                            "listingId = b2.listingId, " +
                            "userId = b2.userId, " +
                            "bidAmount = b2.bidAmount " +
                            "from (values " +
                            "(?, ?, ?)" +
                            ") as b2(listingId, userId, bidAmount) " +
                            "where b.listingId=b2.listingId and b.userId=b2.userId;");
            statement.setInt(1, TEntity.getListingId());
            statement.setInt(2, TEntity.getUserId());
            statement.setBigDecimal(3, TEntity.getBidAmount().getNumberStripped());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public Bid find(ISQLInjector injector, List<Object> queryParam) {
        Bid bid = Bid.create(0, 0, Money.of(0, Monetary.getCurrency("AUD")));
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            if(rs.next()) {
                bid = Bid.create(
                        rs.getInt("listingId"),
                        rs.getInt("userId"),
                        Money.of(rs.getBigDecimal("bidAmount"), Monetary.getCurrency("AUD")));
            }
        } catch (SQLException e) {
            return null;
        }
        return bid;
    }

    @Override
    public List<Bid> findMulti(ISQLInjector injector, List<Object> queryParam) {
        List<Bid> bidList = new ArrayList<>();
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            while(rs.next()) {
                Bid bid = Bid.create(
                        rs.getInt("listingId"),
                        rs.getInt("userId"),
                        Money.of(rs.getBigDecimal("bidAmount"), Monetary.getCurrency("AUD")));
                bidList.add(bid);
            }
        } catch (SQLException e) {
            return null;
        }
        return bidList;
    }
}
