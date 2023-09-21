package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// movie system object containing a list of users and the group's list of watched movies
public class MovieSystem implements Writable {
    private WatchedMovieList groupMovies;  // list of all unique watched movies by users in system
    private UserList users;  // list of users in system
    private String groupName;

    // EFFECTS: constructs a movie system with empty list of users, empty movie list, and a group name
    public MovieSystem(String name) {
        users = new UserList();
        groupMovies = new WatchedMovieList();
        groupName = name;
    }

    // MODIFIES: this
    // EFFECTS: adds given user to movie system
    public void addUserToSystem(User user) {
        users.addUser(user);
        EventLog.getInstance().logEvent(new Event("Added " + user.getName() + " to system."));
    }

    // MODIFIES: this
    // EFFECTS: adds movie to movie system
    public void addMovieToSystem(WatchedMovie wm) {
        groupMovies.addMovie(wm);
        EventLog.getInstance().logEvent(new Event("Added " + wm.getTitle() + " to group list."));
    }

    // EFFECTS: gets list of users
    public UserList getUsers() {
        return this.users;
    }

    // EFFECTS: gets group watched movie list
    public WatchedMovieList getGroupMovies() {
        return this.groupMovies;
    }

    // EFFECTS: gets list of users
    public String getGroupName() {
        return this.groupName;
    }

    // EFFECTS: returns user info as a JSON object
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        JSONArray userArray = users.toJsonArray();
        JSONArray movieArray = groupMovies.moviesToJson();
        object.put("group name", groupName);
        object.put("users", userArray);
        object.put("group movies", movieArray);
        return object;
    }
}
