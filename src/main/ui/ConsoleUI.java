package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// class represents console UI system
public class ConsoleUI {
    private static final String JSON_STORE = "./data/MovieSystem.json";
    private MovieSystem system;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private String username;  // current user's username or "exit" command
    private User currentUser; // current user logged in
    private Scanner prompt;  // prompt for user input

    // EFFECTS: constructs consule UI with scanner prompt system
    public ConsoleUI() throws FileNotFoundException {
        prompt = new Scanner(System.in);
        username = "";
        system = new MovieSystem("My Friends' Theatre");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        startUp();
    }

    // EFFECTS: begins system by prompting for user login and giving choices
    private void startUp() {
        while (!username.equalsIgnoreCase("exit")) {
            System.out.println("Welcome to Films and Friends! ");
            int menuChoice = 0;
            login();
            System.out.println("You are now logged in as '" + username + "'");
            provideUserChoices(menuChoice);
        }
    }

    // REQUIRES: menuChoice is valid int from available options
    // MODIFIES: this, users
    // EFFECTS: based on menu choice, creates a new user or logs in to user from list
    public void login() {
        UserList users = system.getUsers();
        if (users.isEmpty()) {
            System.out.println("\t'l' -> load user(s) from file");
            System.out.println("\tOR enter a username to create your first user in a new Theatre: ");
            username = prompt.next();
            if (username.equals("l")) {
                loadMovieSystem();
                login();
            } else {
                currentUser = new User(username);
            }
        } else {
            loadUsers(users);
        }
    }

    // MODIFIES: this
    // EFFECTS: gives user options and chooses current user based on choices
    private void loadUsers(UserList users) {
        System.out.println("Choose your number from the list of users below");

        for (int i = 1; i <= users.getLength(); i++) {
            System.out.println("\t" + i + ". " + users.getUser(i - 1).getName());
        }
        int i = users.getLength();
        System.out.println("OR");
        System.out.println("\t'" + (i + 1) + "'" + " -> make a new user");

        int userChoice = prompt.nextInt();

        if (userChoice == i + 1) {
            System.out.println("Enter a username: ");
            username = prompt.next();
            currentUser = new User(username);
        } else {
            currentUser = users.getUser(userChoice - 1);
        }
    }

    // MODIFIES: this
    // EFFECTS: reads and loads MovieSystem from JSON file
    private void loadMovieSystem() {
        try {
            system = jsonReader.read();
            System.out.println("Loaded " + system.getGroupName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // REQUIRES: menuChoice != 4 when called
    // EFFECTS: displays current user's lists and provides their options in a loop
    private void provideUserChoices(int menuChoice) {
        while (menuChoice < 4) {
            printUserMovies(currentUser.getToWatchMovies());
            printUserMovies(currentUser.getWatchedMovies());

            printUserChoices();
            menuChoice = prompt.nextInt();
            userAction(menuChoice);
        }
    }

    // REQUIRES: 1 <= menuChoice <= 3
    // EFFECTS: if menuChoice = 1 or 2, prompts for movie title and director and adds to appropriate list
    // if menuChoice = 3, group movies are presented and user is prompted for number to view its rating
    private void userAction(int menuChoice) {
        if (menuChoice < 3) {
            System.out.println("What's the title of the movie? ");
            prompt.nextLine();
            String title = prompt.nextLine();
            System.out.println("Who's the director? ");
            String dir = prompt.nextLine();
            editUserMovies(menuChoice, title, dir);

        } else if (menuChoice == 3) {
            System.out.println("Enter the number for which movie you'd like view the group rating for: ");
            viewGroupRating();
        } else if (menuChoice == 4) {
            system.addUserToSystem(currentUser);
            saveMovieSystem();
        }
    }

    // EFFECTS: saves movie system to JSON file
    private void saveMovieSystem() {
        try {
            jsonWriter.open();
            jsonWriter.write(system);
            jsonWriter.close();
            System.out.println("Saved " + system.getGroupName() + " in " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // REQUIRES: menuChoice = 1 or 2
    // MODIFIES: currentUser
    // EFFECTS: if menuChoice = 1, creates new Movie and adds to-watch list
    // if menuChoice = 2, asks for date, rating, and handles adding or updating a WatchedMovie
    // prints out confirmed result of adding movie
    private void editUserMovies(int menuChoice, String title, String dir) {
        switch (menuChoice) {
            case 1:
                Movie mov = new Movie(title, dir);
                currentUser.addMovieToWatch(mov);
                printMovieUpdate(title, currentUser.getName());
                break;

            case 2:
                System.out.println("when did you watch it? (enter a number in YYMMDD format) ");
                String date = prompt.next();

                System.out.print("Give " + title + " a rating (1-5): ");
                WatchedMovie wm = addWatchedMovieToUserSystem(title, dir, date);
                int rating = prompt.nextInt();
                wm.setRating(currentUser.getName(), rating);

                String name = currentUser.getName();
                title = wm.getTitle();
                rating = wm.getRating(currentUser);
                printWatchedMovieUpdate(name, rating, title, date);
                break;
        }
    }

    // EFFECTS: prints watched movie updated rating info
    private void printWatchedMovieUpdate(String name, int rating, String title, String date) {
        System.out.print(name + " watched " + title + " on " + date);
        System.out.println(" and rated it " + rating + " stars!");
    }

    // EFFECTS: prints movie update
    private void printMovieUpdate(String title, String name) {
        System.out.print(title + " was added to ");
        System.out.println(currentUser.getName() + "'s to-watch list.");
    }

    // REQUIRES: date is 6-digit string in YYMMDD format
    // MODIFIES: this, groupMovies, currentUser
    // EFFECTS: if groupMovies already contains movie of same title, returns movie already in list
    // otherwise makes and returns new watchedMovie with given title and dir and adds
    // adds appropriate watchedMovie to the current user
    private WatchedMovie addWatchedMovieToUserSystem(String title, String dir, String date) {
        WatchedMovie wm;
        WatchedMovieList groupMovies = system.getGroupMovies();
        if (groupMovies.containsMovie(title)) {
            wm = groupMovies.getMovie(title);
        } else {
            wm = new WatchedMovie(title, dir);
            system.addMovieToSystem(wm);
        }
        currentUser.addMovieWatched(wm, date);
        return wm;
    }

    // EFFECTS: prints out numbered movies in groupMovies and calculates group rating for movie by the number
    private void viewGroupRating() {
        WatchedMovieList groupMovies = system.getGroupMovies();
        for (int i = 1; i <= groupMovies.getLength(); i++) {
            System.out.println(i + ". " + groupMovies.getMovie(i - 1).getTitle());
        }
        int movieIndex = prompt.nextInt() - 1;
        WatchedMovie groupMovie = groupMovies.getMovie(movieIndex);
        int groupRating = groupMovie.calcGroupRating();
        String title = groupMovie.getTitle();
        System.out.print(title + " received a group rating of ");
        System.out.println(groupRating + " stars from your friends. ");
    }

    // EFFECTS: prints out current user's available choices
    private void printUserChoices() {
        System.out.println("Would you like to: ");
        System.out.println("1. add a movie to your to-watch list");
        // !!! System.out.println("2. remove a movie from your to-watch list");
        System.out.println("2. add a movie and rating to your watched list");
        System.out.println("3. see the group rating for a movie");
        System.out.println("4. save user to file and logout");
        System.out.println("5. logout without saving");
        System.out.println("(enter 1, 2, 3, 4, or 5)");
    }

    // EFFECTS: prints movies on user's to-watch list, or a message if empty
    private void printUserMovies(MovieList list) {
        System.out.println("Movies on to-watch list: ");

        if (list.isEmpty()) {
            System.out.println("- no movies added yet -");
        } else {
            for (int p = 1; p <= list.getLength(); p++) {
                System.out.println(p + ". " + list.getMovie(p - 1).getTitle());
            }
        }
    }

    // EFFECTS: prints movies on user's watched list, or a message if empty
    private void printUserMovies(WatchedMovieList list) {
        System.out.println("Movies on watched list: ");

        if (list.isEmpty()) {
            System.out.println("- no movies added yet -");
        } else {
            for (int p = 1; p <= list.getLength(); p++) {
                System.out.println(p + ". " + list.getMovie(p - 1).getTitle());
            }
        }
    }
}
