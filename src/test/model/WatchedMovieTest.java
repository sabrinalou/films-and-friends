package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

// tests for WatchedMovie methods
public class WatchedMovieTest {
    private WatchedMovie wm;
    private User u1;
    private User u2;

    @BeforeEach
    public void setUp() {
        wm = new WatchedMovie("a", "sam");
        u1 = new User("me");
        u1.addMovieWatched(wm, "230219");
        u2 = new User("somebody");
        u2.addMovieWatched(wm, "230119");
    }

    @Test
    public void testConstructor() {
        assertEquals("a", wm.getTitle());
        assertEquals("sam", wm.getDirector());
    }

    @Test
    public void testSetRating() {
        wm.setRating(u1.getName(), 3);
        assertEquals(3, wm.getRating(u1));
    }

    @Test
    public void testSetRatingMultipleUsers() {
        wm.setRating(u1.getName(), 1);
        assertEquals(1, wm.getRating(u1));

        wm.setRating(u2.getName(), 5);
        assertEquals(5, wm.getRating(u2));

        assertEquals(u1.getWatchedMovie(0), u2.getWatchedMovie(0));
    }

    @Test
    public void testSetDate() {
        assertEquals("2023/02/19", wm.getDate(u1));

        wm.setDate(u1.getName(), "230211");
        assertEquals("2023/02/11", wm.getDate(u1));

        wm.setDate(u2.getName(), "230211");
        assertEquals("2023/02/11", wm.getDate(u2));
    }

    @Test
    public void testCalcGroupRating() {
        wm.setRating(u1.getName(), 1);
        assertEquals(1, wm.getRating(u1));

        wm.setRating(u2.getName(), 5);
        assertEquals(5, wm.getRating(u2));

        assertEquals(u1.getWatchedMovie(0), u2.getWatchedMovie(0));
        assertEquals(3, wm.calcGroupRating());
    }

    @Test
    public void testToJson() {
        wm.setRating("me", 5);
        JSONObject obj = wm.toJson();
        String title = obj.getString("title");
        String dir = obj.getString("director");
        assertEquals("a", title);
        assertEquals("sam", dir);
        JSONArray userRatings = obj.getJSONArray("user ratings");
        JSONArray watchDates = obj.getJSONArray("user watch dates");
        assertEquals(1, userRatings.length());
        assertEquals(2, watchDates.length());
        assertEquals(5, userRatings.getJSONObject(0).getInt("rating"));
        assertEquals("me", watchDates.getJSONObject(0).getString("username"));
        assertEquals("230219", watchDates.getJSONObject(0).getString("date"));
        assertEquals("230119", watchDates.getJSONObject(1).getString("date"));
    }

}
