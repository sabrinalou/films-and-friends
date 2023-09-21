package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// test class for JsonWRiter
class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            MovieSystem sys = new MovieSystem("squad");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyUser() {
        try {
            MovieSystem sys = new MovieSystem("squad");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMovieSystem.json");
            writer.open();
            writer.write(sys);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMovieSystem.json");
            sys = reader.read();
            assertEquals("squad", sys.getGroupName());
            assertTrue(sys.getUsers().isEmpty());
            assertTrue(sys.getGroupMovies().isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralUser() {
        try {
            MovieSystem sys = new MovieSystem("squad");
            User u1 = new User("sab");
            User u2 = new User("elio");
            Movie m1 = new Movie("a", "sam");
            Movie m2 = new Movie("b", "sam");
            WatchedMovie first = new WatchedMovie(m1.getTitle(), m1.getDirector());
            WatchedMovie second = new WatchedMovie(m2.getTitle(), m2.getDirector());
            u1.addMovieWatched(first, "230204");
            u2.addMovieWatched(first, "230204");
            u2.addMovieWatched(second, "230204");
            first.setRating("sab", 5);
            first.setRating("elio", 4);
            u1.addMovieToWatch(m1);

            sys.addMovieToSystem(first);
            sys.addMovieToSystem(second);
            sys.addUserToSystem(u1);
            sys.addUserToSystem(u2);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralMovieSystem.json");
            writer.open();
            writer.write(sys);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralMovieSystem.json");
            sys = reader.read();
            assertEquals("squad", sys.getGroupName());
            UserList users = sys.getUsers();
            WatchedMovieList watched = sys.getGroupMovies();
            assertEquals(2, watched.getLength());
            assertEquals(m1.getTitle(), watched.getMovie(0).getTitle());
            assertEquals(m2.getTitle(), watched.getMovie(1).getTitle());
            checkUser("squad", users, watched, sys);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
