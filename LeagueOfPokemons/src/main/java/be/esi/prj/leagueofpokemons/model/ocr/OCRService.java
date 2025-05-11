package be.esi.prj.leagueofpokemons.model.ocr;

import be.esi.prj.leagueofpokemons.util.ImageProcessor;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;

/**
 * Service class responsible for performing Optical Character Recognition (OCR) on Pokémon card images.
 * This class uses the Tesseract OCR engine to extract information from Pokémon card images,
 * specifically the Pokémon's name and the local ID.
 */
public class OCRService {
    private ITesseract tesseract;

    /**
     * Constructs an OCRService instance and initializes the Tesseract OCR engine.
     * It sets the data path for Tesseract's language files and the page segmentation mode.
     */
    public OCRService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setPageSegMode(6); // Page segmentation mode for sparse text with single column.
    }

    /**
     * Scans a given Pokémon card image and extracts the Pokémon's name and local ID.
     * The image is processed to extract the name and ID regions, and Tesseract OCR is used to extract text.
     *
     * @param image the Pokémon card image to be scanned.
     * @return a {@link CardScanResult} containing the result of the scan (success flag, Pokémon name, and local ID).
     */
    public CardScanResult scanCardImage(BufferedImage image) {
        assertImageValid(image);
        try {
            BufferedImage nameRegion = ImageProcessor.preprocessImage(image, "pokemonName");
            tesseract.setVariable("tessedit_char_whitelist", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUWXYZ");
            String nameText = tesseract.doOCR(nameRegion).trim();

            tesseract.setVariable("tessedit_char_whitelist", "0123456789");
            BufferedImage localIdRegion = ImageProcessor.preprocessImage(image, "localId");
            int localId = Integer.parseInt(tesseract.doOCR(localIdRegion).trim());

            return new CardScanResult(true, nameText, localId);

        } catch (TesseractException | NumberFormatException e) {
            return new CardScanResult(false, null, -1);
        }
    }

    /**
     * Validates the image to ensure it meets the required dimensions.
     *
     * @param image the image to be validated.
     * @throws OCRException if the image size is not 600x825 pixels.
     */
    private void assertImageValid(BufferedImage image) {
        if (image.getWidth() != 600 || image.getHeight() != 825) {
            throw new OCRException("Image size must be 600x825: " + image.getWidth() + ", " + image.getHeight());
        }
    }
}
