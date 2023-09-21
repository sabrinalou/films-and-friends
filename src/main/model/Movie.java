package model;

import org.json.JSONObject;

// a movie with title and director
public class Movie {
    protected String title;
    protected String director;

    // EFFECTS: instantiates a Movie with title and director
    public Movie(String title, String director) {
        this.title = title;
        this.director = director;
    }

    // EFFECTS: returns this movie title
    public String getTitle() {
        return title;
    }

    // EFFECTS: returns this movie director
    public String getDirector() {
        return director;
    }

    // EFFECTS: returns movie info as json object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("director", director);
        return json;
    }

}
