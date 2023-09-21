package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.LinkedList;

// a list of users
public class UserList {
    private List<User> list;

    // MODIFIES: this
    // EFFECTS: creates empty list of users
    public UserList() {
        list = new LinkedList<>();
    }

    // REQUIRES: username does not already exist in list
    // MODIFIES: this
    // EFFECTS: adds user to list
    public void addUser(User user) {
        list.add(user);
    }

    // REQUIRES: username does not already exist in list
    // MODIFIES: this
    // EFFECTS: adds user to list
    public void removeUser(User user) {
        list.remove(user);
    }

    // EFFECTS: get user by index
    public User getUser(int i) {
        return list.get(i);
    }

    // EFFECTS: returns number of users
    public int getLength() {
        return list.size();
    }

    // EFFECTS: return true if user list is empty
    public Boolean isEmpty() {
        return list.isEmpty();
    }

    // EFFECTS: returns user list as JSONArray objects
    public JSONArray toJsonArray() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject userObj = list.get(i).toJson();
            array.put(userObj);
        }
        return array;
    }

}
