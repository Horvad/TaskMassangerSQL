package by.myproject.userservice.repositories;

import by.myproject.userservice.repositories.api.IPersonalCodeRepository;
import by.myproject.userservice.repositories.dataSource.api.IDataSourceWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonalCodeRepository implements IPersonalCodeRepository {
    private final IDataSourceWrapper ds;
    private final String getCodeSQL = "SELECT code FROM user_service.verivication WHERE mail = ?;";
    private final String deledeSQL = "DELETE FROM user_service.verification WHERE mail = ?;";
    private final String saveSQL = "INSERT INTO user_service.verification (mail, code) VALUES(?,?);";

    public PersonalCodeRepository(IDataSourceWrapper ds) {
        this.ds = ds;
    }
    @Override
    public void delete(String mail) {
        String code = getCode(mail);
        try (Connection conn = ds.getConnection()){
            PreparedStatement ps = conn.prepareStatement(deledeSQL);
            ps.setString(1,mail);
            ps.executeUpdate();
        } catch (SQLException e) {
            save(mail,code);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(String mail, String code) {
        try (Connection conn = ds.getConnection()){
            PreparedStatement ps = conn.prepareStatement(saveSQL);
            ps.setString(1,mail);
            ps.setString(2,code);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getCode(String mail) {
        try (Connection conn = ds.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(getCodeSQL);
            preparedStatement.setString(1,mail);
            ResultSet resultSet = preparedStatement.executeQuery();
            String code = null;
            while (resultSet.next()){
                code = resultSet.getString("code");
            }
            return code;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
