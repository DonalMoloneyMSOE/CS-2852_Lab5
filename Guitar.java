/**
 * CS2852 - 011
 * Fall 2017
 * Lab 5: Guitar Synthesizer
 * Name: Donal Moloney
 * 10/01/17
 */
package Moloneyda.guitar.src;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * The Guitar class generates guitar sounds based on user input.
 * <p>
 * In order to use this class correctly, one must create a Guitar
 * object, add all of the desired notes to the object, and then
 * call the play() method.  The play() method will generate a
 * list of samples for all of the notes to be played (by calling
 * an internal method (jaffeSmith())) and then send them to the
 * audio output stream.
 */
public class Guitar {
    /*
    * Conversion factor from milliseconds to seconds
    */
    private static final double MILLISECONDS_TO_SECONDS = 0.001;

    /**
     * Default sample rate in Hz
     */
    private static final int DEFAULT_SAMPLE_RATE = 8000;

    /**
     * Default decay rate
     */
    private static final float DEFAULT_DECAY_RATE = 0.99f;

    /**
     * Queue of notes
     */
    private Queue<Note> notes;

    /**
     * Sample rate in samples per second
     */
    private int sampleRate;

    /**
     * Decay rate
     */
    private float decayRate;

    /**
     * Maximum sample rate in Hz
     */
    private static final int MAX_SAMPLE_RATE = 48000;

    /**
     * Minimum sample rate in Hz
     */
    private static final int MIN_SAMPLE_RATE = 8000;
    /**
     * Minimum decay rate
     */
    private static final float MIN_DECAY_RATE = 0.0f;

    /**
     * Maximum decay rate
     */
    private static final float MAX_DECAY_RATE = 1.0f;

    /**
     * An instance of SimpleAudio
     */
    private SimpleAudio audioPlayer;

    /**
     * Constructs a new Guitar object with the default sample rate
     * and decay rate.
     */
    public Guitar() {
        this(DEFAULT_SAMPLE_RATE, DEFAULT_DECAY_RATE);
    }

    /**
     * Constructs a new Guitar object with the specified parameters.
     * If an invalid sampleRate or decayRate is specified, the
     * default value will be used and an error message is sent to
     * System.err.
     *
     * @param sampleRate sample rate (between 8000 Hz and 48000 Hz)
     * @param decayRate  decay rate (between 0.0f and 1.0f)
     */
    public Guitar(int sampleRate, float decayRate) {
        notes = new LinkedList<>();
        if (decayRate < MIN_DECAY_RATE || MAX_DECAY_RATE < decayRate) {
            this.decayRate = DEFAULT_DECAY_RATE;
            this.sampleRate = sampleRate;
            this.audioPlayer = new SimpleAudio(sampleRate);
            System.err.println(
                    "decay rate is out of acceptable range: decay rate has been set to default");
        } else if (sampleRate < MIN_SAMPLE_RATE || MAX_SAMPLE_RATE < sampleRate) {
            this.sampleRate = DEFAULT_SAMPLE_RATE;
            this.audioPlayer = new SimpleAudio(sampleRate);
            this.decayRate = decayRate;
            System.err.println(
                    "Sample rate is out of acceptable range: sample rate has been set to default");
        } else {
            this.sampleRate = sampleRate;
            this.decayRate = decayRate;
            this.audioPlayer = new SimpleAudio(sampleRate);
        }
    }

    /**
     * Adds the specified note to this Guitar.
     *
     * @param note Note to be added.
     */
    public void addNote(Note note) {
        notes.offer(note);
    }

    /**
     * Generates the audio samples for the notes listed in the
     * current Guitar object by calling the jaffeSmith algorithm and
     * sends the samples to the speakers.
     *
     * @throws LineUnavailableException If audio line is unavailable.
     * @throws IOException              If any other input/output problem is encountered.
     */
    public void play() throws LineUnavailableException, IOException {
        List<Float> samples = jaffeSmith();
        audioPlayer.play(samples);
    }

    /**
     * Uses the Jaffe-Smith algorithm to generate the audio samples.
     * <br />Implementation note:<br />
     * Use Jaffe-Smith algorithm described on the assignment page
     * to generate a sequence of samples for each note in the list
     * of notes.
     *
     * @return List of samples comprising the pluck sound(s).
     */
    private List<Float> jaffeSmith() {
        List<Float> samples = new ArrayList<>();
        int size = notes.size();
        for (int i = 0; i < size; i++) {
            Queue<Float> periodSamples = new LinkedList<>();
            float previousSample = 0;
            Note aNote = notes.poll();
            float desiredFrequency = aNote.getFrequency();
            float samplesPerPeriod = sampleRate / desiredFrequency;
            for (int j = 0; j < samplesPerPeriod; j++) {
                double num = (Math.random() * 2 - 1);
                periodSamples.offer((float) num);
            }
            float numberOfSamples =
                    (float) (sampleRate * (aNote.getDuration() * MILLISECONDS_TO_SECONDS));
            for (int j = 0; j < numberOfSamples; j++) {
                Float currentPeriod = periodSamples.poll();
                float newSampleValue = ((previousSample + currentPeriod) / 2) * decayRate;
                samples.add(newSampleValue);
                periodSamples.offer(newSampleValue);
                previousSample = currentPeriod;
            }
        }
        return samples;
    }

    /**
     * Returns an array containing all the notes in this Guitar.
     * OPTIONAL
     *
     * @return An array of Notes in the Guitar object.
     */
    public Note[] getNotes() {
        throw new UnsupportedOperationException("Optional method not implemented");
    }

    /**
     * Creates a new file and writes to that file.
     * OPTIONAL
     *
     * @param file File to write to.
     * @throws IOException If any other input/output problem is encountered.
     */
    public void write(File file) throws IOException {
        throw new UnsupportedOperationException("Optional method not implemented");
    }
}
