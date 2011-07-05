package test.junitinaction;

import junitinaction.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EntitiesHelper {
    private static final String USER_FIRST_NAME = "Jeffrey";
    private static final String USER_LAST_NAME = "Lebowsky";
    private static final String USER_USERNAME = "ElDuderino";

    public static User newUser() {
        User user = new User();
        user.setFirstName(USER_FIRST_NAME);
        user.setLastName(USER_LAST_NAME);
        user.setUsername(USER_USERNAME);
        return user;
    }

    public static void assertUser(User user) {
        assertNotNull(user);
        assertEquals("Jeffrey", user.getFirstName());
        assertEquals("Lebowsky", user.getLastName());
        assertEquals("ElDuderino", user.getUsername());
    }
}
