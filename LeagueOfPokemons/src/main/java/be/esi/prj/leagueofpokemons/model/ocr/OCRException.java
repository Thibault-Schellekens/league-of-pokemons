package be.esi.prj.leagueofpokemons.model.ocr;

/**
 * Custom exception class used for handling errors related to Optical Character Recognition (OCR) operations.
 * This exception is thrown when an issue occurs during the OCR process, such as a failure to scan or recognize a card.
 */
public class OCRException extends RuntimeException {

    /**
     * Constructs a new OCRException with the specified detail message.
     *
     * @param message the detail message, which provides information about the cause of the exception.
     */
    public OCRException(String message) {
        super(message);
    }
}
