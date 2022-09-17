package Mapper;

import Entity.Bid;
import Entity.EntityObject;
import Injector.IInjector;
import Util.Util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class GeneralMapper<T> implements Mapper<T> {
    protected Connection conn = null;

    public ResultSet getResultSet(IInjector injector, List<Object> queryParam) throws SQLException {
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
            } else if(param instanceof BigDecimal) {
                statement.setBigDecimal(i, (BigDecimal) param);
            } else if(param instanceof Object) {
                statement.setObject(i, (Object) param);
            }
        }
        statement.execute();
        return statement.getResultSet();
    }

    public boolean delete(T TEntity) {
        EntityObject TEntityObject;
        if(TEntity instanceof EntityObject) {
            TEntityObject = (EntityObject) TEntity;
        } else {
            return false;
        }

        try {
            getResultSet(TEntityObject.getInjector(), TEntityObject.getParam());
        } catch(SQLException e) {
            return false;
        }
        return true;
    }
}
