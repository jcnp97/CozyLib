package net.cozyvanilla.cozylib.modules.mysql.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetMapper<T> {
    T fromResultSet(ResultSet resultSet) throws SQLException;
}