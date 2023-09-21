package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

// tests for WatchedMovieList methods
public class WatchedMovieListTest {
    private WatchedMovieList list;
    private WatchedMovie wm1;
    private WatchedMovie wm2;

    @BeforeEach
    public void setUp() {
        list = new WatchedMovieList();
        wm1 = new WatchedMovie("a", "sam");
        wm2 = new WatchedMovie("b", "sam");
    }

    @Test
    public void testConstructor() {
        assertTrue(list.isEmpty());
    }

    @Test
    public void testAddMovie() {
        assertTrue(list.isEmpty());
        assertFalse(list.containsMovie("a"));
        list.addMovie(wm1);
        assertEquals(wm1, list.getMovie(0));
        assertEquals(1, list.getLength());
        assertEquals(wm1, list.getMovie(wm1));
        assertEquals(wm1, list.getMovie("a"));
        assertTrue(list.containsMovie("a"));
        assertFalse(list.isEmpty());
    }

    @Test
    public void testAddMovieMultiple() {
        list.addMovie(wm1);
        list.addMovie(wm2);
        assertEquals(wm1, list.getMovie(0));
        assertEquals(wm2, list.getMovie(1));
        assertEquals(2, list.getLength());
    }

    @Test
    public void testRemoveMovie() {
        assertTrue(list.isEmpty());
        assertFalse(list.containsMovie(wm1));
        list.addMovie(wm1);
        assertEquals(wm1, list.getMovie(0));
        assertTrue(list.containsMovie(wm1));

        list.removeMovie(wm1);
        assertFalse(list.containsMovie("a"));
        assertTrue(list.isEmpty());
        assertFalse(list.containsMovie(wm1));
        assertEquals(0, list.getLength());
    }

    @Test
    public void testRemoveMovieMultiple() {
        assertTrue(list.isEmpty());
        list.addMovie(wm1);
        list.addMovie(wm2);
        assertEquals(wm1, list.getMovie(0));
        assertEquals(wm2, list.getMovie(1));
        assertEquals(2, list.getLength());
        assertTrue(list.containsMovie(wm1));
        assertFalse(list.containsMovie("cc"));
        assertNull(list.getMovie("cc"));

        list.removeMovie(wm1);
        assertEquals(wm2, list.getMovie(0));
        assertEquals(1, list.getLength());
        assertFalse(list.containsMovie(wm1));

        list.removeMovie(wm2);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testMoviesToJson() {
        list.addMovie(wm1);
        list.addMovie(wm2);
        JSONArray array = list.moviesToJson();
        JSONObject mov1 = array.getJSONObject(0);
        JSONObject mov2 = array.getJSONObject(1);
        assertEquals("a", mov1.getString("title"));
        assertEquals("b", mov2.getString("title"));
    }

}


