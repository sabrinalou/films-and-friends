package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//test class for JsonReader
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            MovieSystem sys = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyUser() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyMovieSystem.json");
        try {
            MovieSystem sys = reader.read();
            assertEquals("squad", sys.getGroupName());
            assertTrue(sys.getUsers().isEmpty());
            assertTrue(sys.getGroupMovies().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralUser() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralMovieSystem.json");
        try {
            MovieSystem sys = reader.read();
            assertEquals("squad", sys.getGroupName());
            UserList users = sys.getUsers();
            WatchedMovieList watched = sys.getGroupMovies();
            assertEquals(2, watched.getLength());
            assertEquals("a", watched.getMovie(0).getTitle());
            assertEquals("b", watched.getMovie(1).getTitle());
            checkUser("squad", users, watched, sys);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
