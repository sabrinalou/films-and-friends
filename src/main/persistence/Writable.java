package persistence;

import org.json.JSONObject;

//interface for class that is writable as JSON
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}