package test.junitinaction;

import junitinaction.User;
import junitinaction.UserDaoJdbcImpl;
import org.dbunit.Assertion;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.*;
import static test.junitinaction.EntitiesHelper.assertUser;
import static test.junitinaction.EntitiesHelper.newUser;

public class UserDaoJdbcImplTest {
    private static final String IGNORE_SCHEMA = null;
    private static final String USERS_TABLE = "users";
    private static final String[] ONLY_USER_TABLE = new String[] {USERS_TABLE};
    private static final String[] IGNORE_ID = new String[] { "id" };
    private static final String USER_XML = "/user.xml";

    private static UserDaoJdbcImpl dao = new UserDaoJdbcImpl();
    private static Connection connection;
    private static HsqldbConnection dbunitConnection;

    @BeforeClass
    public static void setupDatabase() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:db-testing;shutdown=true");
        dbunitConnection = new HsqldbConnection(connection, IGNORE_SCHEMA);
        dao.setConnection(connection);
        dao.createTables();
    }

    @AfterClass
    public static void closeDatabase() throws Exception {
        if (connection != null) {
            connection.close();
            connection = null;
        }
        if (dbunitConnection != null) {
            dbunitConnection.close();
            dbunitConnection = null;
        }
    }

    @Test
    public void testGetUserById() throws Exception {
        IDataSet setupDataSet = getDataSet(USER_XML);
        DatabaseOperation.CLEAN_INSERT.execute(dbunitConnection, setupDataSet);
        User user = dao.getUserById(1);
        assertUser(user);
    }

    @Test
    public void testAddUser() throws Exception {
        IDataSet setupDataSet = getDataSet(USER_XML);
        DatabaseOperation.DELETE_ALL.execute(dbunitConnection, setupDataSet);
        User user = newUser();
        long id = dao.addUser(user);
        assertTrue(id > 0);
        IDataSet actualDataSet = dbunitConnection.createDataSet(ONLY_USER_TABLE);
        Assertion.assertEqualsIgnoreCols(setupDataSet, actualDataSet, USERS_TABLE, IGNORE_ID);
    }

    private IDataSet getDataSet(String name) throws IOException, DataSetException {
        InputStream inputStream = getClass().getResourceAsStream(name);
        assertNotNull("file " + name + " not found in classpath", inputStream);
        Reader reader = new InputStreamReader(inputStream);
        FlatXmlDataSet dataSet = new FlatXmlDataSet(reader);
        return dataSet;
    }

}
