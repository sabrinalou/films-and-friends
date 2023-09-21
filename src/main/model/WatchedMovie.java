package model;

import org.json.*;

import java.util.ArrayList;
import java.util.HashMap;

// a movie that has a set of users' ratings and set of users' dates watched
public class WatchedMovie extends Movie {
    private HashMap<String, String> datesWatched;
    private HashMap<String, Integer> ratings;

    // MODIFIES: this
    // EFFECTS: assigns a value to title and director
    // creates a HashMap
    public WatchedMovie(String title, String director) {
        super(title, director);
        datesWatched = new HashMap<String, String>();
        ratings = new HashMap<String, Integer>();
    }

    // REQUIRES: rating is integer from 1-5
    // MODIFIES: this
    // EFFECTS: sets watched movie rating
    public void setRating(String name, int rating) {
        ratings.put(name, rating);
    }

    // REQUIRES: date in valid YYMMDD format
    // MODIFIES: this
    // EFFECTS: sets date watched for movie
    public void setDate(String name, String date) {
        datesWatched.put(name, date);
    }

    // REQUIRES: movie does not have null rating
    // EFFECTS: returns rating for movie
    public int getRating(User u) {
        return ratings.get(u.getName());
    }

    // EFFECTS: returns date watched in words
    public String getDate(User u) {
        String date = datesWatched.get(u.getName());
        String year = "20" + date.substring(0, 2) + "/";
        String month = date.substring(2, 4) + "/";
        String day = date.substring(4, 6);
        return year + month + day;
    }

    // EFFECTS: calculates average rating for movie
    public int calcGroupRating() {
        ArrayList<Integer> list = getAllRatings();
        int sum = 0;

        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
        }

        return sum / list.size();
    }

    // EFFECTS: gets all ratings for movie as arraylist
    private ArrayList<Integer> getAllRatings() {
        ArrayList<Integer> list = new ArrayList<>(this.ratings.values());
        return list;
    }

    // EFFECTS: returns movie info as json object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("director", director);
        json.put("user ratings", movieUserRatingsToJson());
        json.put("user watch dates", movieUserDatesToJson());
        return json;
    }

    // EFFECTS: returns jsonArray of user dates watched
    private JSONArray movieUserDatesToJson() {
        JSONArray array = new JSONArray();
        for (HashMap.Entry<String, String> entry : datesWatched.entrySet()) {
            String name = entry.getKey();
            String date = entry.getValue();
            JSONObject obj = watchDateToJson(name, date);
            array.put(obj);
        }
        return array;
    }

    // EFFECTS: returns jsonArray of user rating JSONObjects (contains "user", rating")
    private JSONArray movieUserRatingsToJson() {
        JSONArray array = new JSONArray();

        for (HashMap.Entry<String, Integer> entry : ratings.entrySet()) {
            String name = entry.getKey();
            int rating = entry.getValue();
            JSONObject obj = ratingToJson(name, rating);
            array.put(obj);
        }
        return array;
    }

    // EFFECTS: returns movie watch date info as json object
    private JSONObject watchDateToJson(String name, String date) {
        JSONObject json = new JSONObject();
        json.put("username", name);
        json.put("date", date);
        return json;
    }

    // EFFECTS: returns movie rating info as json object
    private JSONObject ratingToJson(String name, int rating) {
        JSONObject json = new JSONObject();
        json.put("username", name);
        json.put("rating", rating);
        return json;
    }

}
