package ui;

import javax.swing.*;
import java.awt.*;

// represents an abstract movie window
public abstract class MovieUI extends JInternalFrame {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    // private Component theParent;
    protected JPanel panel;

    /**
     * Constructor
     * @param parent  the parent component
     */
    public MovieUI(Component parent, String window) {
        super(window, false, true, false, false);
        // theParent = parent;
        setSize(WIDTH, HEIGHT);
        setPosition(parent);
        setVisible(true);
        this.setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
        panel = new JPanel();
        panel.setSize(150, 150);
        panel.setBackground(Color.GRAY);
        add(panel);
    }

    /**
     * Sets the position of this remote control UI relative to parent component
     * @param parent   the parent component
     */
    private void setPosition(Component parent) {
        setLocation(parent.getWidth() - getWidth(), 0);
    }

}

