package be.esi.prj.leagueofpokemons.util;

import be.esi.prj.leagueofpokemons.model.ocr.OCRException;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A utility class for image processing tasks, including extracting regions from an image,
 * converting an image to grayscale, and enhancing contrast.
 * This class is designed for use in the OCR functionality for Pokémon card scanning.
 */
public class ImageProcessor {

    private ImageProcessor() {}

    /**
     * Preprocesses an image based on the specified type.
     * The preprocessing includes extracting a region of interest and converting it to grayscale.
     *
     * @param original the original image to preprocess.
     * @param type the type of the region to extract (e.g., "pokemonName", "localId").
     * @return a new BufferedImage with the extracted and processed region.
     * @throws OCRException if the region is out of bounds or an invalid type is provided.
     */
    public static BufferedImage preprocessImage(BufferedImage original, String type) {
        BufferedImage newImage = null;
        if (type.equals("pokemonName")) {
            newImage = extractRegion(original, 100, 20, 300, 50);
            newImage = convertToGrayscale(newImage);
        } else if (type.equals("localId")) {
            newImage = extractRegion(original, 90, original.getHeight() - 45, 32, 20);
            newImage = convertToGrayscale(newImage);
        }

        return newImage;
    }

    /**
     * Converts the given image to grayscale and enhances the contrast.
     * The contrast adjustment makes the grayscale image more distinguishable for OCR processing.
     *
     * @param original the image to convert to grayscale.
     * @return a new BufferedImage that is the grayscale version of the original image with enhanced contrast.
     */
    private static BufferedImage convertToGrayscale(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(original.getRGB(x, y));
                int grayValue = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());

                // Increase contrast
                grayValue = (int) Math.max(0, Math.min(255, (grayValue - 128) * 1.5 + 128));

                Color newColor = new Color(grayValue, grayValue, grayValue);
                newImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return newImage;
    }

    /**
     * Extracts a rectangular region from the original image.
     *
     * @param original the original image to extract the region from.
     * @param x the x-coordinate of the top-left corner of the region.
     * @param y the y-coordinate of the top-left corner of the region.
     * @param width the width of the region to extract.
     * @param height the height of the region to extract.
     * @return a new BufferedImage containing the extracted region.
     * @throws OCRException if the region is out of bounds of the original image.
     */
    public static BufferedImage extractRegion(BufferedImage original, int x, int y, int width, int height) {
        if (x + width > original.getWidth() || y + height > original.getHeight()) {
            throw new OCRException("Region out of bounds");
        }
        return original.getSubimage(x, y, width, height);
    }

}
