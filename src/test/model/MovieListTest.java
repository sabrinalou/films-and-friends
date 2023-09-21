package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

// tests for MovieList methods
public class MovieListTest {
    private MovieList list;
    private Movie m;
    private Movie b;

    @BeforeEach
    public void setUp() {
        list = new MovieList();
        m = new Movie("a", "sam");
        b = new Movie("b", "sam");
    }

    @Test
    public void testConstructor() {
        assertTrue(list.isEmpty());
    }

    @Test
    public void testAddMovie() {
        assertTrue(list.isEmpty());
        list.addMovie(m);
        assertEquals(m, list.getMovie(0));
        assertEquals(1, list.getLength());
        assertFalse(list.isEmpty());
    }

    @Test
    public void testAddMovieMultiple() {
        list.addMovie(m);
        list.addMovie(b);
        assertEquals(m, list.getMovie(0));
        assertEquals(b, list.getMovie(1));
        assertEquals(2, list.getLength());
    }

    @Test
    public void testRemoveMovie() {
        assertTrue(list.isEmpty());
        assertFalse(list.containsMovie(m));
        list.addMovie(m);
        assertEquals(m, list.getMovie(0));
        assertTrue(list.containsMovie(m));

        list.removeMovie(m);
        assertTrue(list.isEmpty());
        assertFalse(list.containsMovie(m));
        assertEquals(0, list.getLength());
    }

    @Test
    public void testRemoveMovieMultiple() {
        assertTrue(list.isEmpty());
        list.addMovie(m);
        list.addMovie(b);
        assertEquals(m, list.getMovie(0));
        assertEquals(b, list.getMovie(1));
        assertEquals(2, list.getLength());
        assertTrue(list.containsMovie(m));

        list.removeMovie(m);
        assertEquals(b, list.getMovie(0));
        assertEquals(1, list.getLength());
        assertFalse(list.containsMovie(m));

        list.removeMovie(b);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testMovieListToJson() {
        list.addMovie(m);
        list.addMovie(b);
        JSONArray array = list.moviesToJson();
        assertEquals(2, array.length());
        assertEquals("a", array.getJSONObject(0).getString("title"));
        assertEquals("b", array.getJSONObject(1).getString("title"));
    }

}
