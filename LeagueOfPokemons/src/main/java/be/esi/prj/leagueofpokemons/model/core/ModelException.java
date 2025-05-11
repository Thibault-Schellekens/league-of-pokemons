package be.esi.prj.leagueofpokemons.model.core;

/**
 * Custom exception class used to signal errors in the model layer of the application.
 * This exception extends {@link RuntimeException} and can be used to indicate
 * model-specific issues such as invalid game state or invalid operations.
 */
public class ModelException extends RuntimeException {

    /**
     * Constructs a new {@link ModelException} with the specified detail message.
     *
     * @param message The detail message, which will be saved for later retrieval
     *                by the {@link Throwable#getMessage()} method.
     */
    public ModelException(String message) {
        super(message);
    }
}
