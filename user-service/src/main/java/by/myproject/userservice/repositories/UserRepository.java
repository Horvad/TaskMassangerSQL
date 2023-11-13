package by.myproject.userservice.repositories;

import by.myproject.userservice.core.DTO.ContentViewDTO;
import by.myproject.userservice.core.DTO.UserCreateDTO;
import by.myproject.userservice.core.DTO.UserViewDTO;
import by.myproject.userservice.core.enums.UserRole;
import by.myproject.userservice.core.enums.UserStatus;
import by.myproject.userservice.repositories.api.IUserRepository;
import by.myproject.userservice.repositories.dataSource.api.IDataSourceWrapper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

public class UserRepository implements IUserRepository {
    private final IDataSourceWrapper ds;

    public UserRepository(IDataSourceWrapper ds) {
        this.ds = ds;
    }

    private final String SAVE_SQL =
            "INSERT INTO user_service.user (UUID,mail,fio,password,role,status,dt_create,dt_update)" +
            "VALUES(?,?,?,?,?,?,?,?);";
    @Override
    public void save(UUID uuid, UserCreateDTO userCreateDTO) {
        try (Connection conn = ds.getConnection()){
            PreparedStatement ps = conn.prepareStatement(SAVE_SQL);
            ps.setObject(1,uuid);
            ps.setString(2,userCreateDTO.getMail());
            ps.setString(3,userCreateDTO.getFio());
            ps.setString(4,userCreateDTO.getPassword());
            ps.setString(5,userCreateDTO.getRole().toString());
            ps.setString(6,userCreateDTO.getStatus().toString());
            ps.setObject(7,LocalDateTime.now());
            ps.setObject(8,LocalDateTime.now());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String GET_PAGE_SQL =
            "SELECT uuid, mail, fio, role, status, dt_create, dt_update " +
                    "FROM user_service.user " +
                    "ORDER BY uuid " +
                    "LIMIT(?) " +
                    "OFFSET(?);";
    private final String GET_COUNT_SQL = "SELECT COUNT(uuid) AS count FROM user_service.user";
    @Override
    public ContentViewDTO getPage(int page, int size) {
        try (Connection conn = ds.getConnection()){
            List contentUsers = new ArrayList();
            PreparedStatement psGetCount = conn.prepareStatement(GET_COUNT_SQL);
            ResultSet rsGetCount = psGetCount.executeQuery();
            rsGetCount.next();
            int rowsPage = page*size;
            Integer count = rsGetCount.getInt("count");
            PreparedStatement psGetPage = conn.prepareStatement(GET_PAGE_SQL);
            psGetPage.setInt(1,size);
            psGetPage.setInt(2,rowsPage);
            ResultSet rsGetPage = psGetPage.executeQuery();
            while (rsGetPage.next()){
                UUID uuid = rsGetPage.getObject("uuid",UUID.class);
                String mail = rsGetPage.getString("mail");
                String fio = rsGetPage.getString("fio");
                UserRole role = UserRole.valueOf(rsGetPage.getString("role"));
                UserStatus status = UserStatus.valueOf(rsGetPage.getString("status"));
                LocalDateTime dt_create = rsGetPage.getTimestamp("dt_create").toLocalDateTime();
                LocalDateTime dt_update = rsGetPage.getTimestamp("dt_update").toLocalDateTime();
                contentUsers.add(new UserViewDTO(uuid,mail,fio,role,status,dt_create,dt_update));
            }
            ContentViewDTO contentViewDTO = new ContentViewDTO();
            contentViewDTO.setContent(contentUsers);
            contentViewDTO.setNumber(page);
            contentViewDTO.setSize(size);
            contentViewDTO.setTotalPages(count/size+1);
            contentViewDTO.setTotalElements(count);
            contentViewDTO.setNumber_of_elements(contentUsers.size());
            if(size>count||page==1){
                contentViewDTO.setFirst(true);
            } else {
                contentViewDTO.setFirst(false);
            }
            if(size>count||contentViewDTO.getNumber_of_elements()<size||page*size==count){
                contentViewDTO.setLast(true);
            } else {
                contentViewDTO.setLast(false);
            }
            return contentViewDTO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String GET_USER_UUID_SQL =
            "SELECT mail, fio, role, status, dt_create, dt_update " +
            "FROM user_service.user " +
            "WHERE uuid = (?);";
    @Override
    public UserViewDTO get(UUID uuid) {
        try (Connection conn = ds.getConnection()){
            PreparedStatement ps = conn.prepareStatement(GET_USER_UUID_SQL);
            ps.setObject(1,uuid);
            ResultSet rs = ps.executeQuery();
            if(rs.wasNull()){
                throw new IllegalArgumentException("Данного пользователя не существует");
            }
            UserViewDTO userViewDTO = null;
            while (rs.next()) {
                String mail = rs.getString("mail");
                String fio = rs.getString("fio");
                UserRole role = UserRole.valueOf(rs.getString("role"));
                UserStatus status = UserStatus.valueOf(rs.getString("status"));
                LocalDateTime dtCreate = rs.getObject("dt_create",LocalDateTime.class);
                LocalDateTime dtUpdate = rs.getObject("dt_update",LocalDateTime.class);
                if(mail==null){
                    throw new IllegalArgumentException("Данного пользователя не существует");
                }
                userViewDTO = new UserViewDTO(uuid,mail,fio,role,status,dtCreate,dtUpdate);
            }
            return userViewDTO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String UPDATE_USER_SQL =
            "UPDATE user_service.user " +
            "SET mail = ?, fio = ?, role = ?, status = ?, password = ?, dt_update = ?" +
            "WHERE uuid = ? AND dt_update = ?;";
    @Override
    public void update(UUID uuid, LocalDateTime dtUpdate, UserCreateDTO userUpdate) {
        try (Connection conn = ds.getConnection()){
            PreparedStatement ps = conn.prepareStatement(UPDATE_USER_SQL);
            ps.setString(1, userUpdate.getMail());
            ps.setString(2, userUpdate.getFio());
            ps.setString(3,userUpdate.getRole().toString());
            ps.setString(4,userUpdate.getStatus().toString());
            ps.setString(5,userUpdate.getPassword());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setObject(7,uuid);
            ps.setTimestamp(8,Timestamp.valueOf(dtUpdate));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String GET_DT_UPDATE_SQL =
            "SELECT dt_update FROM user_service.user WHERE uuid = ?;";
    @Override
    public LocalDateTime getDtUpdate(UUID uuid) {
        try (Connection conn = ds.getConnection()){
            PreparedStatement ps = conn.prepareStatement(GET_DT_UPDATE_SQL);
            ps.setObject(1,uuid);
            ResultSet rs = ps.executeQuery();
            LocalDateTime dtUpdate = null;
            while (rs.next()){
                dtUpdate = rs.getTimestamp("dt_update").toLocalDateTime();
            }
            if(dtUpdate==null){
                throw new IllegalArgumentException("Данного пользователя не существует");
            }
            return dtUpdate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String GET_PASSWORD_SQL =
            "SELECT password FROM user_service.user WHERE mail = ?;";
    @Override
    public String getPassword(String mail) {
        try (Connection conn = ds.getConnection()){
            PreparedStatement ps = conn.prepareStatement(GET_PASSWORD_SQL);
            ps.setString(1,mail);
            ResultSet rs = ps.executeQuery();
            String password = null;
            while (rs.next()){
                password = rs.getString("password");
            }
            if(password==null){
                throw new IllegalArgumentException("Пользователя с данной почтой не существует");
            }
            return password;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String EXIST_MAIL_SQL =
            "SELECT mail FROM user_service.user WHERE mail = ?;";
    @Override
    public boolean exist(String mail) {
        try (Connection conn = ds.getConnection()){
            PreparedStatement ps = conn.prepareStatement(EXIST_MAIL_SQL);
            ps.setString(1,mail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if(rs.getString("mail").equals(mail)) return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private final String EXIST_UUID_SQL =
            "SELECT uuid FROM user_service.user WHERE uuid = ?;";
    @Override
    public boolean exist(UUID uuid) {
        try (Connection conn = ds.getConnection()){
            PreparedStatement ps = conn.prepareStatement(EXIST_UUID_SQL);
            ps.setObject(1,uuid);
            UUID uuid1 = null;
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                uuid1 = rs.getObject("uuid",UUID.class);
            }
            if (uuid.equals(uuid1)) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private final String UPDATE_STATUS_SQL =
            "UPDATE user_service.user SET status = ? WHERE mail = ?, dt_update = ?;";
    @Override
    public void updateStatus(String mail, UserStatus status, LocalDateTime dtUpdate) {
        try (Connection conn = ds.getConnection()){
            PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS_SQL);
            ps.setString(1,status.toString());
            ps.setString(2,mail);
            ps.setObject(3,dtUpdate);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String GET_FROM_MAIL_SQL =
            "SELECT uuid, fio, role, status, dt_create, dt_update" +
                    "FROM user_service.user" +
                    "WHERE mail = ?;";
    @Override
    public UserViewDTO get(String mail) {
        try (Connection conn = ds.getConnection()){
            PreparedStatement ps = conn.prepareStatement(GET_FROM_MAIL_SQL);
            ps.setString(1,mail);
            ResultSet rs = ps.executeQuery();
            UserViewDTO userViewDTO = null;
            while (rs.next()){
                UUID uuid = rs.getObject("uuid",UUID.class);
                String fio = rs.getString("fio");
                UserRole role = UserRole.valueOf(rs.getString("role"));
                UserStatus status = UserStatus.valueOf(rs.getString("status"));
                LocalDateTime dtCreate = rs.getTimestamp("dt_create").toLocalDateTime();
                LocalDateTime dtUpdate = rs.getTimestamp("dt_update").toLocalDateTime();
                userViewDTO = new UserViewDTO(uuid,mail,fio,role,status,dtCreate,dtUpdate);
            }
            if(userViewDTO==null){
                throw new IllegalArgumentException("Пользователя не существует");
            }
            return userViewDTO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
