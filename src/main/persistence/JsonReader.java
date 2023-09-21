package persistence;

import model.Movie;
import model.MovieSystem;
import model.User;
import model.WatchedMovie;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads movie system from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MovieSystem read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMovieSystem(jsonObject);
    }

    // EFFECTS: parses movie system from JSONObject
    private MovieSystem parseMovieSystem(JSONObject jsonObject) {
        MovieSystem system = new MovieSystem(jsonObject.getString("group name"));
        JSONArray users = jsonObject.getJSONArray("users");
        JSONArray groupMovies = jsonObject.getJSONArray("group movies");

        for (int i = 0; i < users.length(); i++) {
            JSONObject userObj = users.getJSONObject(i);
            system.addUserToSystem(parseUser(userObj));
        }
        for (int i = 0; i < groupMovies.length(); i++) {
            JSONObject movObj = groupMovies.getJSONObject(i);
            system.addMovieToSystem(parseMovie(movObj));
        }
        return system;
    }

    // EFFECTS: parses watched movie from JSONobject
    private WatchedMovie parseMovie(JSONObject obj) {
        WatchedMovie wm = new WatchedMovie(obj.getString("title"), obj.getString("director"));

        JSONArray dates = obj.getJSONArray("user watch dates");
        JSONArray ratingsArray = obj.getJSONArray("user ratings");

        for (int i = 0; i < dates.length(); i++) {
            JSONObject dateObj = dates.getJSONObject(i);
            String name = dateObj.getString("username");
            String date = dateObj.getString("date");
            wm.setDate(name, date);
        }

        for (int i = 0; i < ratingsArray.length(); i++) {
            JSONObject ratingObj = ratingsArray.getJSONObject(i);
            String name = ratingObj.getString("username");
            int rating = ratingObj.getInt("rating");
            wm.setRating(name, rating);
        }

        return wm;
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: adds user from JSON object and returns it
    private User parseUser(JSONObject jsonObject) {
        String name = jsonObject.getString("username");
        User u = new User(name);
        addMovies(u, jsonObject);
        addWatchedMovies(u, jsonObject);
        return u;
    }


    // MODIFIES: u
    // EFFECTS: parses movies from JSON object and adds it to user
    private void addWatchedMovies(User u, JSONObject jsonObject) {
        JSONArray movieArray = jsonObject.getJSONArray("watched");

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject obj = movieArray.getJSONObject(i);
            String title = obj.getString("title");
            String dir = obj.getString("director");
            WatchedMovie wm = new WatchedMovie(title, dir);

            JSONArray ratingsArray = obj.getJSONArray("user ratings");
            JSONArray datesArray = obj.getJSONArray("user watch dates");
            addRatings(wm, ratingsArray);
            addDatesWatched(wm, datesArray);
            u.addMovieWatched(wm, wm.getDate(u));
        }
    }

    // MODIFIES: wm
    // EFFECTS: adds JSONArray's dates watched info to watched movie
    private void addDatesWatched(WatchedMovie wm, JSONArray datesArray) {
        for (int x = 0; x < datesArray.length(); x++) {
            JSONObject jsObj = datesArray.getJSONObject(x);
            String user = jsObj.getString("username");
            String date = jsObj.getString("date");
            wm.setDate(user, date);
        }
    }

    // MODIFIES: wm
    // EFFECTS: adds JSONArray's ratings info to watched movie object
    private void addRatings(WatchedMovie wm, JSONArray ratingsArray) {
        for (int x = 0; x < ratingsArray.length(); x++) {
            JSONObject jsObj = ratingsArray.getJSONObject(x);
            String user = jsObj.getString("username");
            int rating = jsObj.getInt("rating");
            wm.setRating(user, rating);
        }
    }

    // MODIFIES: u
    // EFFECTS: parses movies from JSON object and adds it to user
    private void addMovies(User u, JSONObject jsonObject) {
        JSONArray movieArray = jsonObject.getJSONArray("to-watch");
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject obj = movieArray.getJSONObject(i);
            String title = obj.getString("title");
            String dir = obj.getString("director");
            Movie m = new Movie(title, dir);
            u.addMovieToWatch(m);
        }
    }
}
