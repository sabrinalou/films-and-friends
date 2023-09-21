package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

// tests for User methods
public class UserTest {
    private User me;
    private Movie m;

    @BeforeEach
    public void setUp() {
        me = new User("Sabrina");
        m = new Movie("a", "sam");

    }

    @Test
    public void testConstructor() {
        assertEquals("Sabrina", me.getName());
        assertTrue(me.getWatchedMovies().isEmpty());
        assertTrue(me.getToWatchMovies().isEmpty());
    }

    @Test
    public void testAddMovieToWatch() {
        me.addMovieToWatch(m);
        assertEquals(m, me.getToWatchMovie(0));
        assertTrue(me.getWatchedMovies().isEmpty());

        Movie b = new Movie("b", "sam");
        me.addMovieToWatch(b);
        assertEquals(m, me.getToWatchMovie(0));
        assertEquals(b, me.getToWatchMovie(1));
        assertTrue(me.getWatchedMovies().isEmpty());
    }

    @Test
    public void testAddMovieWatched() {
        WatchedMovie wm = new WatchedMovie(m.getTitle(), m.getDirector());
        me.addMovieWatched(wm, "230219");
        assertEquals(wm, me.getWatchedMovie(0));
        assertTrue(me.getToWatchMovies().isEmpty());

        WatchedMovie b = new WatchedMovie("b", "sam");
        me.addMovieWatched(b, "230219");
        assertEquals(wm, me.getWatchedMovie(0));
        assertEquals(b, me.getWatchedMovie(1));
        assertTrue(me.getToWatchMovies().isEmpty());
    }

    @Test
    public void testSetMovieRating() {
        WatchedMovie wm = new WatchedMovie(m.getTitle(), m.getDirector());
        me.addMovieWatched(wm, "230219");
        me.updateMovieRating(wm, 3);
        assertEquals(3, wm.getRating(me));
    }

    @Test
    public void testToJson() {
        me.addMovieToWatch(m);
        WatchedMovie wm = new WatchedMovie("b", "sam");
        me.addMovieWatched(wm, "230202");
        JSONObject obj = me.toJson();
        assertEquals("Sabrina", obj.getString("username"));
        JSONArray toWatchArray = obj.getJSONArray("to-watch");
        JSONArray watchedArray = obj.getJSONArray("watched");
        JSONObject movObj = toWatchArray.getJSONObject(0);
        String watchTitle = movObj.getString("title");
        JSONObject watchedMovObj = watchedArray.getJSONObject(0);
        String watchedTitle = watchedMovObj.getString("title");
        assertEquals("a", watchTitle);
        assertEquals("b", watchedTitle);
    }
}
