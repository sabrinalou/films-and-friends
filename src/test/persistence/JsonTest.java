package persistence;

import model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

// test class for JSON
public class JsonTest {
    protected void checkUser(String name, UserList users, WatchedMovieList groupMovies, MovieSystem sys) {
        assertEquals(name, sys.getGroupName());
        assertEquals(users, sys.getUsers());
        assertEquals(groupMovies, sys.getGroupMovies());
    }
}
