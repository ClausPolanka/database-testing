package junitinaction;

import java.sql.SQLException;

public interface UserDAO {
    long addUser(User user) throws SQLException;

     User getUserById(long id) throws SQLException;
}
