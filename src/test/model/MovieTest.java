package model;

import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

// tests for Movie methods
class MovieTest {
    private Movie a;

    @BeforeEach
    public void setUp() {
        a = new Movie("a", "sam");
    }

    @Test
    public void testConstructor() {
        assertEquals("a", a.getTitle());
        assertEquals("sam", a.getDirector());
    }

    @Test
    public void testToJson() {
        JSONObject obj = a.toJson();
        assertEquals("a", obj.getString("title"));
        assertEquals("sam", obj.getString("director"));
    }

}