package be.esi.prj.leagueofpokemons.util.random;

/**
 * Utility class that provides random number generation methods.
 * This class is intended to simplify the usage of random number generation within the game.
 * It uses the default {@link Math#random()} method to generate random numbers.
 */
public class RandomUtil {

    private RandomUtil() {}

    /**
     * Generates a random double value between 0.0 (inclusive) and 1.0 (exclusive).
     * This method wraps {@link Math#random()} to provide a simple utility method for random double values.
     *
     * @return a random double value between 0.0 (inclusive) and 1.0 (exclusive).
     */
    public static double nextDouble() {
        return Math.random();
    }
}
