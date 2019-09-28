/**
 * CS2852 - 011
 * Fall 2017
 * Lab 5: Guitar Synthesizer
 * Name: Donal Moloney
 * 10/01/17
 */
package Moloneyda.guitar.src;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Program that reads in notes from a text file and plays a song
 * using the Guitar class to generate the sounds which are then
 * played by a SimpleAudio object.
 */
public class Lab5 {
    /**
     * This is the main method of the program that reads in notes from a text file and
     * plays a song
     * @param args Ignored
     */
    public static void main(String[] args) {
        try {
            int userSampleRate = getUserSpecSampleRate();
            float userDecayRate = getUserSpecDecayRate();
            Guitar guitar = new Guitar(userSampleRate, userDecayRate);
            String fileChooser = obtainFile();
            ArrayList<String> lines = readFile(fileChooser);
            for (int i = 0; i < lines.size(); i++) {
                Note newNote = parseNote(lines.get(i));
                if (!(newNote == null)) {
                    guitar.addNote(newNote);
                }
            }
            guitar.play();
        } catch (LineUnavailableException e) {
            System.err.println("Error!!!: The line you requested to play is unavailable!");
        } catch (IOException e) {
            System.err.println("Error!!!: Your I/O operation was failed or interrupted!");
        } catch (NullPointerException e) {
            System.err.println("Error!!!: You exited the file and did not choose a song!");
        } catch (NumberFormatException e) {
            System.err.println(
                    "Error!!!: You did not follow the specified input  or your file is in the " +
                            "invalid format!");

        }
    }


    /**
     * Returns a new Note created with the files parameters of frequency and duration
     *
     * @param line Description of a note with scientific pitch followed by duration in milliseconds.
     * @return Note represented by the String passed in.  Returns null if it is unable to parse
     * the note data correctly.
     */
    private static Note parseNote(String line) {
        Note newNote;
        String frequency;
        float duration;
        String[] splitString = line.split(" ");
        String frequencyString = splitString[0];
        if (frequencyString != null && !frequencyString.isEmpty()) {
            String durationString = splitString[1];
            frequency = frequencyString;
            duration = Float.parseFloat(durationString);
            newNote = new Note(frequency, duration);
            return newNote;
        }
        return null;
    }

    /**
     * This method takes the lines of the file and puts them into an array list
     *
     * @param file - the file the user chose to play the notes of
     * @return ArrayList containing the lines of the document
     * @throws IOException - If there is a problem reading the file
     */
    private static ArrayList readFile(String file) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader bufferedReader;
        FileReader fileReader;
        fileReader = new FileReader(file);
        bufferedReader = new BufferedReader(fileReader);
        String currentLine;
        while ((currentLine = bufferedReader.readLine()) != null) {
            if (!currentLine.equals("\n")) {
                lines.add(currentLine);
            }
        }
        return lines;
    }

    /**
     * This method obtains the file that the user chose to play
     *
     * @return fileChoice - the file path of the file the user chose as a string
     * @throws NullPointerException - if the user exits the selection box and doesn't choose a file
     */
    public static String obtainFile() throws NullPointerException {
        String fileChoice = null;
        String[] buttons = {"Take me out to the ball game", "Deck the halls", "Cancel"};
        int buttonPress = JOptionPane.showOptionDialog(null, "Choose a file to play" + " music",
                                                       "File Selector",
                                                       JOptionPane.INFORMATION_MESSAGE, 0, null,
                                                       buttons, buttons[1]);
        if (buttonPress == 0) {
            fileChoice = "src/ballGame.txt";
        } else if (buttonPress == 1) {
            fileChoice = "src/deckTheHalls.txt";
        }
        return fileChoice;
    }

    /**
     * This method gets the sample rate from the user
     *
     * @return checkValid - the validated user chosen sample rate as an integer
     * @throws NumberFormatException if the user inters something that's not a number or exits
     *                               Dialog box
     */
    public static int getUserSpecSampleRate() throws NumberFormatException {
        String userSpecSampleRate = JOptionPane.showInputDialog(null, "Enter a number between 8,000"
                +" & 48,000 to represent your sample rate in Hertz");
        Integer checkValid = Integer.parseInt(userSpecSampleRate);
        if (userSpecSampleRate instanceof String) {
            return checkValid;
        }
        return 0;
    }

    /**
     * This method gets the user decay rate the user inputs
     *
     * @return The chosen user decay rate as a float
     */
    public static float getUserSpecDecayRate() {
        String userSpecDecayRate = JOptionPane.showInputDialog(null,"Enter a number between 0.0 and"
                +" 1.0 to represent your decay rate");
        Float checkValid = Float.parseFloat(userSpecDecayRate);
        if (checkValid instanceof Float) {
            return checkValid;
        }
        return 0;
    }
}
