package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// test class for MovieSystem
public class MovieSystemTest {
    private MovieSystem sys;

    @BeforeEach
    public void setUp() {
        sys = new MovieSystem("squad");
    }

    @Test
    public void testConstructor() {
        assertTrue(sys.getGroupMovies().isEmpty());
        assertTrue(sys.getUsers().isEmpty());
        assertEquals("squad", sys.getGroupName());
    }

    @Test
    public void testAddUser() {
        assertTrue(sys.getUsers().isEmpty());
        sys.addUserToSystem(new User("sab"));
        assertFalse(sys.getUsers().isEmpty());
        assertEquals("sab", sys.getUsers().getUser(0).getName());
    }

    @Test
    public void testAddMovie() {
        assertTrue(sys.getGroupMovies().isEmpty());
        WatchedMovie wm = new WatchedMovie("a", "sam");
        wm.setDate("sab", "230202");
        wm.setRating("sab", 3);
        sys.addMovieToSystem(wm);

        assertFalse(sys.getGroupMovies().isEmpty());
        assertEquals("a", sys.getGroupMovies().getMovie(0).getTitle());
    }

    @Test
    public void testEmptyToJson() {
        assertTrue(sys.toJson().getJSONArray("users").isEmpty());
        assertTrue(sys.toJson().getJSONArray("group movies").isEmpty());
    }

    @Test
    public void testGeneralToJson() {
        JSONObject obj = new JSONObject();
        User u1 = new User("sab");
        User u2 = new User("bob");
        Movie m1 = new Movie("a", "sam");
        Movie m2 = new Movie("b", "sam");

        JSONArray userArray = new JSONArray();
        JSONObject uObj1 = u1.toJson();
        JSONObject uObj2 = u2.toJson();
        userArray.put(uObj1);
        userArray.put(uObj2);

        JSONArray movArray = new JSONArray();
        JSONObject movObj1 = m1.toJson();
        JSONObject movObj2 = m2.toJson();
        movArray.put(movObj1);
        movArray.put(movObj2);

        obj.put("users", userArray);
        obj.put("group movies", movArray);
        assertEquals(uObj1, obj.getJSONArray("users").getJSONObject(0));
        assertEquals(uObj2, obj.getJSONArray("users").getJSONObject(1));
        assertEquals(movObj1, obj.getJSONArray("group movies").getJSONObject(0));
        assertEquals(movObj2, obj.getJSONArray("group movies").getJSONObject(1));

    }
}
