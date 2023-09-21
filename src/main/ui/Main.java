package ui;

import java.io.FileNotFoundException;

// main method, creates MovieSystem object
public class Main {

    public static void main(String[] args) {
        try {
            //new MovieSystemApp();
            new ConsoleUI();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
