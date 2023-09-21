package model;

import org.json.JSONObject;

// a user with a name, to-watch list, and watched list
public class User {
    private String name;
    private MovieList toWatch;
    private WatchedMovieList watched;

    // MODIFIES: this, toWatch, watched
    // EFFECTS: assigns user name, empty user watched list, and empty user to-watch list
    public User(String name) {
        this.name = name;
        watched = new WatchedMovieList();
        toWatch = new MovieList();
    }

    // REQUIRES: movie not already in user's watch list
    // MODIFIES: this and toWatchList
    // EFFECTS: adds movie to watch list
    public void addMovieToWatch(Movie m) {
        toWatch.addMovie(m);
    }

    // REQUIRES: movie not already in user's watched list
    // MODIFIES: this and WatchedList
    // EFFECTS: adds movie to watched list
    public void addMovieWatched(WatchedMovie wm, String date) {
        watched.addMovie(wm);
        wm.setDate(this.name, date);
    }

    // MODIFIES: this and WatchedMovie
    // EFFECTS: sets rating for WatchedMovie in user's watched list
    public void updateMovieRating(WatchedMovie wm, int rating) {
        WatchedMovie update = watched.getMovie(wm);
        update.setRating(this.name, rating);
    }

    // EFFECTS: returns user name
    public String getName() {
        return name;
    }

    // EFFECTS: returns list of watched movies
    public WatchedMovieList getWatchedMovies() {
        return watched;
    }

    // EFFECTS: returns list of movies on watchlist, "empty" if none
    public MovieList getToWatchMovies() {
        return toWatch;
    }

    // EFFECTS: returns movie of given index in watched list
    public WatchedMovie getWatchedMovie(int index) {
        return watched.getMovie(index);
    }

    // EFFECTS: returns movie of given index in to-watch list
    public Movie getToWatchMovie(int index) {
        return toWatch.getMovie(index);
    }

    // EFFECTS: returns user info as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("username", name);
        json.put("to-watch", toWatch.moviesToJson());
        json.put("watched", watched.moviesToJson());
        return json;
    }
}
