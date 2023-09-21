package model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

// a list of watched movies
public class WatchedMovieList {
    private List<WatchedMovie> list;

    // MODIFIES: this
    // EFFECTS: creates list object to hold watched movies
    public WatchedMovieList() {
        list = new ArrayList<>();
    }

    // REQUIRES: 0 <= index <= length of list
    // EFFECTS: returns movie of given index
    public WatchedMovie getMovie(int index) {
        return list.get(index);
    }

    // REQUIRES: movie already in list
    // EFFECTS: returns watched movie in list
    public WatchedMovie getMovie(WatchedMovie m) {
        int index = list.indexOf(m);
        return list.get(index);
    }

    // REQUIRES: given movie is in list
    // EFFECTS: returns movie of given title
    public WatchedMovie getMovie(String title) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTitle().equals(title)) {
                return list.get(i);
            }
        }
        return null;
    }

    // EFFECTS: returns length of movie list
    public int getLength() {
        return list.size();
    }

    // EFFECTS: returns true if list is empty
    public Boolean isEmpty() {
        return list.isEmpty();
    }

    // REQUIRES: movie not already in list
    // MODIFIES: this
    // EFFECTS: adds movie to watchlist
    public void addMovie(WatchedMovie m) {
        list.add(m);
    }

    // REQUIRES: given movie is in list
    // MODIFIES: this
    // EFFECTS: removes movie from watchlist
    public void removeMovie(WatchedMovie m) {
        list.remove(m);
    }

    // EFFECTS: returns true if movie is not in list
    public Boolean containsMovie(WatchedMovie m) {
        return list.contains(m);
    }

    // EFFECTS: returns true if movie of given title is not in list
    public Boolean containsMovie(String title) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns WatchedMovieList as JSONArray
    public JSONArray moviesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (WatchedMovie wm : list) {
            jsonArray.put(wm.toJson());
        }
        return jsonArray;
    }

}
