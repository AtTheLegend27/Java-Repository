package gh2;

import deque.ArrayDeque;
import deque.Deque;

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor
    private Deque<Double> buffer;
    public GuitarString(double frequency) {
        int maxStorage = (int) (Math.round(SR / frequency));
        buffer = new ArrayDeque<>();
        for (int x = 0; x < maxStorage; x++) {
            buffer.addFirst((0.0));
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        int bufferSize = buffer.size();
        buffer = new ArrayDeque<>();
        for (int x = 0; x < bufferSize; x++) {
            buffer.addLast(Math.random() - 0.5);
        }
    }


    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double tac = ((buffer.removeFirst() + sample()) * DECAY) / 2;
        buffer.addLast(tac);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }
}
