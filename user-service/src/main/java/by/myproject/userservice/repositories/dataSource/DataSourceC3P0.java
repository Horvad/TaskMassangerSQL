package by.myproject.userservice.repositories.dataSource;

import by.myproject.userservice.repositories.dataSource.api.IDataSourceWrapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceC3P0 implements IDataSourceWrapper {
    private ComboPooledDataSource cpds;

    public DataSourceC3P0(String driver,
                          String url,
                          String user,
                          String password) throws PropertyVetoException {
        this.cpds = new ComboPooledDataSource();
        this.cpds.setDriverClass(driver);
        this.cpds.setJdbcUrl(url);
        this.cpds.setUser(user);
        this.cpds.setPassword(password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }


    @Override
    public void close() throws Exception {
        this.cpds.close();
    }
}
