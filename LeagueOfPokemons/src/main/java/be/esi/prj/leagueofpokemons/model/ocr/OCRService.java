package be.esi.prj.leagueofpokemons.model.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;

public class OCRService {
    private ITesseract tesseract;

    public OCRService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setPageSegMode(6);
    }

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

    private void assertImageValid(BufferedImage image) {
        if (image.getWidth() != 600 && image.getHeight() != 825) {
            throw new OCRException("Image size must be 600x825: " + image.getWidth() + ", " + image.getHeight());
        }
    }

}
