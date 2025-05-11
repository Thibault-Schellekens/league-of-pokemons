package be.esi.prj.leagueofpokemons.model.ocr;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OCRServiceTest {
    private static OCRService ocrService;
    private static ClassLoader classLoader;

    @BeforeAll
    static void setUp() {
        ocrService = new OCRService();
        classLoader = OCRServiceTest.class.getClassLoader();
    }

    @Test
    void testInvalidImageSize() throws IOException {
        File file = new File(classLoader.getResource("ocr_samples/invalid_size.png").getFile());
        BufferedImage image = ImageIO.read(file);
        assertThrows(OCRException.class, () -> ocrService.scanCardImage(image));
    }

    // Test with pokemon back card 600x825
//    @Test
//    void testInvalidImage() throws IOException {
//        File file = new File(classLoader.getResource("ocr_samples/invalid_image.png").getFile());
//        BufferedImage image = ImageIO.read(file);
//        assertThrows(OCRException.class, () -> ocrService.scanCardImage(image));
//    }

    @Test
    void testInvalidSetCard() throws IOException {
        File file = new File(classLoader.getResource("ocr_samples/invalid_set_card.png").getFile());
        BufferedImage image = ImageIO.read(file);

        CardScanResult expected = new CardScanResult(false, null, -1);

        CardScanResult actual = ocrService.scanCardImage(image);

        assertEquals(expected, actual);
    }

    @Test
    void testValidCard() throws IOException {
        File file = new File(classLoader.getResource("ocr_samples/valid_card.png").getFile());
        BufferedImage image = ImageIO.read(file);

        CardScanResult expected = new CardScanResult(true, "Scorbunny", 31);

        CardScanResult actual = ocrService.scanCardImage(image);

        assertEquals(expected, actual);
    }
}