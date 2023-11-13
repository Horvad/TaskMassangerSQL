package by.myproject.userservice.repositories.api;

public interface IPersonalCodeRepository {
    void delete(String mail);
    void save(String mail, String code);
    String getCode(String mail);
}
