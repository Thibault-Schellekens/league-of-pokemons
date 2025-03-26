package be.esi.prj.leagueofpokemons.model.ocr;

import be.esi.prj.leagueofpokemons.model.core.Card;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

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
            BufferedImage nameRegion = ImageProcessor.preprocessImage(image, "name");
            tesseract.setVariable("tessedit_char_whitelist", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUWXYZ");
            String nameText = tesseract.doOCR(nameRegion);

            BufferedImage hpRegion = ImageProcessor.preprocessImage(image, "hp");
            tesseract.setVariable("tessedit_char_whitelist", "0123456789");
            int hp = Integer.parseInt(tesseract.doOCR(hpRegion).trim());

            return new CardScanResult(true, nameText, hp);

        } catch (TesseractException e) {
            return new CardScanResult(false, null, -1);
        }
    }

    private void assertImageValid(BufferedImage image) {
        if (image.getWidth() != 600 && image.getHeight() != 825) {
            throw new OCRException("Image size must be 600x825: " + image.getWidth() + ", " + image.getHeight());
        }
    }


    public static void main(String[] args) throws IOException {
        String imageURL = "https://assets.tcgdex.net/en/swsh/swsh1/2/low.png";
        URL url = new URL(imageURL);
        BufferedImage image = ImageIO.read(url);
        OCRService ocrService = new OCRService();
        CardScanResult result = ocrService.scanCardImage(image);

        System.out.println(result);

    }
}
