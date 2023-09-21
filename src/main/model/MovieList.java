package model;

import org.json.JSONArray;

import java.util.LinkedList;
import java.util.List;

// a user's list of to-watch movies
public class MovieList {
    private List<Movie> list;

    // MODIFIES: this
    // EFFECTS: creates an empty list for movies
    public MovieList() {
        list = new LinkedList<>();
    }

    // REQUIRES: movie not already in list
    // MODIFIES: this
    // EFFECTS: adds movie to watchlist
    public void addMovie(Movie m) {
        list.add(m);
        EventLog.getInstance().logEvent(new Event("Added movie: " + m.getTitle()));
    }

    // REQUIRES: given movie is in list
    // MODIFIES: this
    // EFFECTS: removes movie from watchlist
    public void removeMovie(Movie m) {
        list.remove(m);
        EventLog.getInstance().logEvent(new Event("Removed movie: " + m.getTitle()));
    }

    // EFFECTS: returns true if movie is not in list
    public Boolean containsMovie(String title, String dir) {
        for (int i = 0; i < list.size(); i++) {
            Movie m = list.get(i);
            if (m.getTitle().equalsIgnoreCase(title) && m.getDirector().equalsIgnoreCase(dir)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: gets length of movie list
    public int getLength() {
        return list.size();
    }

    // REQUIRES: 0 <= index <= length of list
    // EFFECTS: returns movie of given index
    public Movie getMovie(int index) {
        return list.get(index);
    }

    // EFFECTS: returns true if list is empty
    public Boolean isEmpty() {
        return list.isEmpty();
    }

    // EFFECTS: returns true if movie is in list
    public Boolean containsMovie(Movie m) {
        return list.contains(m);
    }

    // EFFECTS: returns movieList as JSONArray
    public JSONArray moviesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Movie m : list) {
            jsonArray.put(m.toJson());
        }
        return jsonArray;
    }
}
