package by.myproject.userservice.repositories.dataSource.api;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDataSourceWrapper extends AutoCloseable{
    Connection getConnection() throws SQLException;
}
