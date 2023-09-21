package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

// represents the movie system application UI
public class MovieSystemApp extends JFrame implements ActionListener, WindowListener {
    private static final String JSON_STORE = "./data/MovieSystem.json";
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private MovieSystem system;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private final JDesktopPane desktop;
    private JInternalFrame mainPanel;
    private JInternalFrame groupMoviesPanel;
    private JMenuBar mb;
    private JMenu userMenu;
    private JMenuItem newUser;
    private JPanel toWatchPanel;
    private JPanel watchedPanel;
    private JPanel ratingsPanel;

    // MODIFIES: this, users, groupMovies
    // EFFECTS: creates scanner for user prompt, user list, group movies list, initializes username, and starts system
    public MovieSystemApp() throws FileNotFoundException {
        system = new MovieSystem("My Friends' Theatre");
        addWindowListener(this);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());
        desktop.setLayout(null);
        setContentPane(desktop);
        setTitle("CPSC 210: Films and Friends");
        setSize(WIDTH, HEIGHT);
        setUpMainPanel();
        groupMoviesPanel = new JInternalFrame("Our Movie Ratings",
                true, false, true, false);
        groupMoviesPanel.setLayout(new FlowLayout());
        groupMoviesPanel.setSize(300, 400);
        desktop.add(groupMoviesPanel, BorderLayout.EAST);
        ratingsPanel = new JPanel();
        updateGroupMovies();
        mainPanel.setVisible(true);
        groupMoviesPanel.setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        centreOnScreen();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: updates groupMoviesPanel based on system
    private void updateGroupMovies() {
        ratingsPanel.removeAll();
        ratingsPanel.setLayout(new BoxLayout(ratingsPanel, BoxLayout.Y_AXIS));
        displayGroupRatings();
        ratingsPanel.setVisible(true);
        groupMoviesPanel.add(ratingsPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: displays each movie's ratings and buttons
    private void displayGroupRatings() {
        for (int i = 0; i < system.getGroupMovies().getLength(); i++) {
            JLabel movie = new JLabel();
            movie.setText(system.getGroupMovies().getMovie(i).getTitle());
            int groupRating = system.getGroupMovies().getMovie(i).calcGroupRating();
            JLabel rating = new JLabel("Group rating: " + groupRating);
            JLabel icon = new JLabel();
            if (groupRating == 1) {
                icon.setIcon(new ImageIcon("./src/main/ui/images/sadface.png"));
            } else if (groupRating <= 3) {
                icon.setIcon(new ImageIcon("./src/main/ui/images/midface.png"));
            } else if (groupRating == 4) {
                icon.setIcon(new ImageIcon("./src/main/ui/images/mildface.png"));
            } else {
                icon.setIcon(new ImageIcon("./src/main/ui/images/happyface.png"));
            }
            JPanel movieRatingPanel = new JPanel();
            movieRatingPanel.setBackground(Color.lightGray);
            movieRatingPanel.add(movie);
            movieRatingPanel.add(rating);
            movieRatingPanel.add(icon);
            movieRatingPanel.setVisible(true);
            ratingsPanel.add(movieRatingPanel);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets up main window for application
    private void setUpMainPanel() {
        mainPanel = new JInternalFrame("My Friends' Theatre",
                true, false, true, false);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setSize(400, 500);
        mb = new JMenuBar();
        userMenu = new JMenu("Users");
        mainPanel.add(mb, BorderLayout.NORTH);
        mb.add(userMenu);
        updateUserMenu();
        addFileMenu();
        desktop.add(mainPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: adds file menu to mainPanel
    private void addFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem load = new JMenuItem("Load saved users from file");
        load.addActionListener(new LoadFileAction());
        JMenuItem save = new JMenuItem("Save current users to file");
        save.addActionListener(new SaveFileAction());
        fileMenu.add(load);
        fileMenu.add(save);
        mb.add(fileMenu);
    }

    /**
     * Represents action to be taken when user clicks desktop
     * to switch focus. (Needed for key handling.)
     */
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            MovieSystemApp.this.requestFocusInWindow();
        }
    }

    /**
     * Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    // MODIFIES: this
    // EFFECTS: updates user menu interface with current system users
    private void updateUserMenu() {
        userMenu.removeAll();
        for (int i = 0; i < system.getUsers().getLength(); i++) {
            JMenuItem userItem = new JMenuItem(system.getUsers().getUser(i).getName());
            userItem.addActionListener(new UserAction(i));
            userMenu.add(userItem);
        }
        newUser = new JMenuItem("> Create New User");
        newUser.addActionListener(this);
        userMenu.add(newUser);
    }

    // MODIFIES: this
    // EFFECTS: updates user watchedPanel
    private void updateWatchedPanel(User u) {
        watchedPanel.removeAll();
        watchedPanel.setLayout(new BoxLayout(watchedPanel, BoxLayout.Y_AXIS));
        watchedPanel.setBackground(Color.gray);
        watchedPanel.setSize(WIDTH, HEIGHT);
        JLabel watchedLabel = new JLabel("Watched movie list:");
        watchedPanel.add(watchedLabel);
        displayWatchedMovies(u);
        JButton addWatchedMovieBtn = new JButton("add watched movie");
        addWatchedMovieBtn.addActionListener(new ReviewAction(u));
        watchedPanel.add(addWatchedMovieBtn);
        mainPanel.add(watchedPanel, BorderLayout.EAST);
    }

    // MODIFIES: this
    // EFFECTS: updates toWatchPanel
    private void updateWatchPanel(User u) {
        toWatchPanel.removeAll();
        toWatchPanel.setLayout(new BoxLayout(toWatchPanel, BoxLayout.Y_AXIS));
        toWatchPanel.setBackground(Color.gray);
        toWatchPanel.setSize(WIDTH, HEIGHT);
        JLabel toWatchLabel = new JLabel("To-watch movie list:");
        toWatchPanel.add(new JLabel(u.getName()));
        toWatchPanel.add(toWatchLabel);

        displayWatchMovies(u);
        JButton addWatchMovieBtn = new JButton("add movie");
        addWatchMovieBtn.addActionListener(new WatchAction(u));
        toWatchPanel.add(addWatchMovieBtn);
        mainPanel.add(toWatchPanel, BorderLayout.WEST);
        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: display user's watchlist with buttons
    private void displayWatchMovies(User u) {
        for (int i = 0; i < u.getToWatchMovies().getLength(); i++) {
            JLabel movie = new JLabel();
            movie.setText(u.getToWatchMovie(i).getTitle() + " by " +  u.getToWatchMovie(i).getDirector());
            JButton watchButton = new JButton("watch/review");
            watchButton.addActionListener(new ReviewAction(u, i));
            JButton removeMovieBtn = new JButton("remove movie");
            removeMovieBtn.addActionListener(new RemoveAction(u, i));
            toWatchPanel.add(movie);
            toWatchPanel.add(watchButton);
            toWatchPanel.add(removeMovieBtn);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays user's watched list
    private void displayWatchedMovies(User u) {
        for (int i = 0; i < u.getWatchedMovies().getLength(); i++) {
            JLabel movie = new JLabel();
            movie.setText(u.getWatchedMovie(i).getTitle() + " by " +  u.getWatchedMovie(i).getDirector());
            int rating = u.getWatchedMovie(i).getRating(u);
            JLabel ratingLab = new JLabel("rating: " + rating);
            JPanel movieRatingPanel = new JPanel();
            movieRatingPanel.setBackground(Color.lightGray);
            movieRatingPanel.add(movie);
            movieRatingPanel.add(ratingLab);
            watchedPanel.add(movieRatingPanel);
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

    // EFFECTS: if groupMovies already contains movie of same title, assigns wm and movie in list
    // otherwise makes and adds new watched movie
    // adds wm to user
    private void addWatchedMovieToSystem(WatchedMovie wm, User user) {
        user.addMovieWatched(wm, "23/03/29");
        WatchedMovieList groupMovies = system.getGroupMovies();
        if (groupMovies.containsMovie(wm.getTitle())) {
            wm = groupMovies.getMovie(wm.getTitle());
        } else {
            system.addMovieToSystem(wm);
        }
    }

    // MODIFIES: this
    // EFFECTS: makes a new user window when button is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newUser) {
            NewUserUI newUserWindow = new NewUserUI();
            desktop.add(newUserWindow);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    // EFFECTS: on close action, prints EventLog
    @Override
    public void windowClosing(WindowEvent e) {
        for (model.Event event : EventLog.getInstance()) {
            System.out.println(event.getDescription() + " at " + event.getDate());
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    // represents a 'new user' window in application
    private class NewUserUI extends JInternalFrame implements ActionListener {
        private static final int WIDTH = 300;
        private static final int HEIGHT = 200;
        private JButton button;
        private JTextField userField;

        // MODIFIES: this
        // EFFECTS: constructs a new JInternalFrame with fields and buttons
        public NewUserUI() {
            super("New User", false, true, false, false);
            this.setLayout(new BorderLayout());
            setSize(WIDTH, HEIGHT);
            JPanel panel = new JPanel();
            panel.setSize(150, 150);
            panel.setBackground(Color.GRAY);
            add(panel);
            userField = new JTextField();
            JLabel userLab = new JLabel("create username: ");
            userField.setColumns(15);
            panel.add(userLab);
            button = new JButton("Create");
            button.addActionListener(this);
            panel.add(userField, BorderLayout.WEST);
            panel.add(button, BorderLayout.SOUTH);
            setPosition(desktop);
            setVisible(true);
            revalidate();
            repaint();
        }

        // MODIFIES: this
        // EFFECTS: makes a new user and adds to system after button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == button) {
                User newUser = new User(userField.getText());
                system.addUserToSystem(newUser);
                updateUserMenu();
                userField.setText("");
                setVisible(false);
            }
        }

        // MODIFIES: this
        // EFFECTS: sets position of window
        private void setPosition(Component parent) {
            setLocation(parent.getWidth() - getWidth(), 0);
        }
    }

    // represents choosing a user action
    private class UserAction implements ActionListener {
        private int index;
        private User user;

        // MODIFIES: this
        // EFFECTS: constructs a user action, user, and index
        public UserAction(int userIndex) {
            index = userIndex;
            user = system.getUsers().getUser(index);
        }

        // MODIFIES: this
        // EFFECTS: updates toWatchPanel and watchedPanel for user on button click
        @Override
        public void actionPerformed(ActionEvent e) {
            if (toWatchPanel != null) {
                mainPanel.remove(toWatchPanel);
            }
            toWatchPanel = new JPanel();
            updateWatchPanel(user);

            if (watchedPanel != null) {
                mainPanel.remove(watchedPanel);
            }
            watchedPanel = new JPanel();
            updateWatchedPanel(user);
            revalidate();
            repaint();
        }
    }

    // represents an action to save system to file
    private class SaveFileAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveMovieSystem();  //writes to local file
        }
    }

    // represents an action load json file into system
    private class LoadFileAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadMovieSystem();  //updates system users
            updateUserMenu();
            updateGroupMovies();
        }
    }

    // represents an action to add a movie to watchlist
    private class WatchAction implements ActionListener {
        private User user;

        // EFFECTS: constructs a user
        public WatchAction(User u) {
            user = u;
        }

        // MODIFIES: thism addMovieWindow
        // EFFECTS: makes an 'add movie' window
        @Override
        public void actionPerformed(ActionEvent e) {
            NewMovieUI addMovieWindow = new NewMovieUI(user);
            desktop.add(addMovieWindow);
        }
    }

    // represents an action to add a watched movie
    private class ReviewAction implements ActionListener {
        private User user;
        private Movie movie;
        private String title;

        // EFFECTS: constructs an action with a user
        public ReviewAction(User u) {
            user = u;
        }

        // EFFECTS: constructs a review action with user, movie, and index
        public ReviewAction(User user, int movIndex) {
            this.user = user;
            movie = user.getToWatchMovie(movIndex);
            title = movie.getTitle();
        }

        // MODIFIES: this, reviewWindow
        // EFFECTS: makes an 'add watched movie' window
        @Override
        public void actionPerformed(ActionEvent e) {
            if (title == null) {
                NewWatchedMovieUI reviewWindow = new NewWatchedMovieUI(user);
                desktop.add(reviewWindow);
            } else {
                NewWatchedMovieUI reviewWindow = new NewWatchedMovieUI(user, movie);
                desktop.add(reviewWindow);
            }
        }
    }

    // represents the action to remove a movie from to-watch list
    private class RemoveAction implements ActionListener {
        private User user;
        private int index;

        // EFFECTS: constructs a remove action with user and index
        public RemoveAction(User user, int index) {
            this.user = user;
            this.index = index;
        }

        // MODIFIES: this
        // EFFECTS: removes movie from user's to-watch list and updates panel
        @Override
        public void actionPerformed(ActionEvent e) {
            user.getToWatchMovies().removeMovie(user.getToWatchMovie(index));
            updateWatchPanel(user);
        }
    }

    // represents a new movie window
    private class NewMovieUI extends MovieUI implements ActionListener {
        protected JButton button;
        protected JLabel movieLab;
        protected JTextField movieField;
        protected JLabel dirLab;
        protected JTextField dirField;
        protected User user;

        // EFFECTS: constructs a new movie window with fields and labels
        public NewMovieUI(User u) {
            super(desktop, "New Movie To-Watch");
            user = u;
            movieLab = new JLabel("Title: ");
            movieField = new JTextField();
            movieField.setColumns(15);
            dirLab = new JLabel("Director: ");
            dirField = new JTextField();
            dirField.setColumns(15);
            button = new JButton("Add movie");
            button.addActionListener(this);
            panel.add(movieLab);
            panel.add(movieField, BorderLayout.WEST);
            panel.add(dirLab);
            panel.add(dirField, BorderLayout.WEST);
            panel.add(button, BorderLayout.SOUTH);
            setVisible(true);
            revalidate();
            repaint();
        }

        // MODIFIES: this
        // EFFECTS: adds movie to user's watchlist and updates panels after button click
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == button) {
                Movie m = new Movie(movieField.getText(), dirField.getText());
                user.addMovieToWatch(m);
                updateWatchPanel(user);
                movieField.setText("");
                dirField.setText("");
                setVisible(false);
            }
        }
    }

    // represents a new watched movie window
    private class NewWatchedMovieUI extends NewMovieUI {
        private JLabel ratingLab;
        private JTextField ratingField;
        private Movie movie;

        // EFFECTS: constructs a new watched movie window with fields and labels
        public NewWatchedMovieUI(User u) {
            super(u);
            panel.remove(button);
            ratingLab = new JLabel("Rating (1-5): ");
            ratingField = new JTextField();
            ratingField.setColumns(15);
            panel.add(ratingLab);
            panel.add(ratingField, BorderLayout.WEST);
            button.setText("Save Rating");
            panel.add(button);
            setVisible(true);
            revalidate();
            repaint();
        }

        // EFFECTS: constructs a new watched movie window with filled in fields and labels
        public NewWatchedMovieUI(User u, Movie m) {
            super(u);
            this.movie = m;
            movieField.setText(m.getTitle());
            dirField.setText(m.getDirector());
            panel.remove(button);
            ratingLab = new JLabel("Rating (1-5): ");
            ratingField = new JTextField();
            ratingField.setColumns(15);
            panel.add(ratingLab);
            panel.add(ratingField, BorderLayout.WEST);
            button.setText("Save Rating");
            panel.add(button);
            setVisible(true);
            revalidate();
            repaint();
        }

        // MODIFIES: this
        // EFFECTS: adds watched movie to system and updates panels after button click
        @Override
        public void actionPerformed(ActionEvent e) {
            WatchedMovie wm = new WatchedMovie(movieField.getText(), dirField.getText());
            wm.setRating(user.getName(), Integer.parseInt(ratingField.getText()));
            addWatchedMovieToSystem(wm, user);
            if (user.getToWatchMovies().containsMovie(wm.getTitle(), wm.getTitle())) {
                user.getToWatchMovies().removeMovie(movie);
            }
            // !!!
            movieField.setText("");
            dirField.setText("");
            ratingField.setText("");
            updateWatchedPanel(user);
            updateWatchPanel(user);
            updateGroupMovies();
            setVisible(false);
        }
    }

}
