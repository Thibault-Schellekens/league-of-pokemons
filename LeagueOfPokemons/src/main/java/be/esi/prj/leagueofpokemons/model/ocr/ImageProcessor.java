package be.esi.prj.leagueofpokemons.model.ocr;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageProcessor {

    public static BufferedImage preprocessImage(BufferedImage original, String type) {
        BufferedImage newImage = null;
//        if (type.equals("hp")) {
//            newImage = extractRegion(original, original.getWidth() - 160, 30, 80, 50);
//            newImage = convertToGrayscale(newImage);
//            newImage = resizeImage(newImage, 400, 250);
//        }
        if (type.equals("pokemonName")) {
            newImage = extractRegion(original, 100, 20, 300, 50);
            newImage = convertToGrayscale(newImage);
        } else if (type.equals("localId")) {
            newImage = extractRegion(original, 90, original.getHeight() - 45, 32, 20);
            newImage = convertToGrayscale(newImage);
//            newImage = resizeImage(newImage, 400, 250);
        }

        return newImage;
    }

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

    private static BufferedImage extractRegion(BufferedImage original, int x, int y, int width, int height) {
        if (x + width > original.getWidth() || y + height > original.getHeight()) {
            throw new OCRException("Region out of bounds");
        }
        return original.getSubimage(x, y, width, height);
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());

        Graphics2D g2d = resizedImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);

        g2d.dispose();

        return resizedImage;
    }
}

