package model;

import org.json.JSONArray;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

// tests for UserList methods
public class UserListTest {
    private UserList list;
    private User me;

    @BeforeEach
    public void setUp() {
        list = new UserList();
        me = new User("sabrina");
    }

    @Test
    public void testConstructor() {
        assertTrue(list.isEmpty());
    }

    @Test
    public void testAddUser() {
        assertTrue(list.isEmpty());
        list.addUser(me);
        assertFalse(list.isEmpty());
        assertEquals(1, list.getLength());

        User x = new User("x");

        list.addUser(x);
        assertEquals(me, list.getUser(0));
        assertEquals(x, list.getUser(1));
        assertEquals(2, list.getLength());
    }

    @Test
    public void testRemoveUser() {
        list.addUser(me);
        assertFalse(list.isEmpty());
        list.removeUser(me);
        assertTrue(list.isEmpty());
        User x = new User("x");

        list.addUser(me);
        list.addUser(x);

        assertEquals(2, list.getLength());
        list.removeUser(me);
        assertEquals(1, list.getLength());
        list.removeUser(x);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testToJsonArray() {
        list.addUser(me);
        User x = new User("x");
        list.addUser(x);
        JSONArray array = new JSONArray();
        array.put(me.toJson());
        array.put(x.toJson());
        assertEquals(2, list.toJsonArray().length());
        assertEquals("sabrina", list.toJsonArray().getJSONObject(0).getString("username"));

    }

}
