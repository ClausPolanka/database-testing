package test.junitinaction;

import junitinaction.User;
import junitinaction.UserDaoJdbcImpl;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDaoJdbcImplTest {
    private static final String IGNORE_SCHEMA = null;
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
        IDataSet setupDataSet = getDataSet("/user.xml");
        DatabaseOperation.CLEAN_INSERT.execute(dbunitConnection, setupDataSet);
        User user = dao.getUserById(1);
        assertNotNull(user);
        assertEquals("Jeffrey", user.getFirstName());
        assertEquals("Lebowsky", user.getLastName());
        assertEquals("ElDuderino", user.getUsername());
    }

    private IDataSet getDataSet(String name) throws IOException, DataSetException {
        InputStream inputStream = getClass().getResourceAsStream(name);
        assertNotNull("file " + name + " not found in classpath", inputStream);
        Reader reader = new InputStreamReader(inputStream);
        FlatXmlDataSet dataSet = new FlatXmlDataSet(reader);
        return dataSet;
    }
}
