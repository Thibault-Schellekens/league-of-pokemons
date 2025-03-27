package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.animation.ScannerAnimation;
import be.esi.prj.leagueofpokemons.model.api.PokeApiService;
import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.ocr.CardScanResult;
import be.esi.prj.leagueofpokemons.model.ocr.OCRException;
import be.esi.prj.leagueofpokemons.model.ocr.OCRService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// TODO: Add Drag and Drop
// TODO: Add Information message (either fail or success)
public class ScannerController {
    private final OCRService ocrService;
    // Field to store the scanned card

    @FXML
    private Button fileExplorerBtn;
    @FXML
    private Button addCardBtn;
    @FXML
    private ImageView glowingHouse;
    @FXML
    private ImageView bag;
    @FXML
    private Line lineScanner;
    @FXML
    private Text successText;
    @FXML
    private Text failedText;


    public ScannerController() {
        ocrService = new OCRService();
    }

    public void initialize() {
        System.out.println("Initializing Scanner");
        ScannerAnimation.addGlowingAnimation(fileExplorerBtn, glowingHouse);
    }

    public void openFileExplorer() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Pokémon Card Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));
        File file = fileChooser.showOpenDialog(fileExplorerBtn.getScene().getWindow());
        handleCardScan(file);
    }

    public void addToCollection() {
        System.out.println("Adding to collection");
    }

    private void handleCardScan(File file) {
        reset();
        Platform.runLater(() -> ScannerAnimation.scanningLineAnimation(lineScanner));

        new Thread(() -> {
            try {
                BufferedImage imageToScan = ImageIO.read(file);
                CardScanResult scanResult = ocrService.scanCardImage(imageToScan);
                if (!scanResult.successful()) {
                    handleScanFailed();
                    return;
                }
                Card card = PokeApiService.getPokemonCard(scanResult.localId(), scanResult.pokemonName()).orElse(null);
                if (card == null) {
                    handleScanFailed();
                    return;
                }
                System.out.println(card);
                handleScanSuccess();
                // todo save card in db
            } catch (IOException | OCRException e) {
                handleScanFailed();
            }
        }).start();
    }

    private void reset() {
        addCardBtn.setDisable(true);
        successText.setOpacity(0.0);
        failedText.setOpacity(0.0);
        bag.setOpacity(0.4);
    }

    private void handleScanSuccess() {
        addCardBtn.setDisable(false);
        successText.setOpacity(1.0);
        ScannerAnimation.stopScanningLineAnimation(lineScanner);
        bag.setOpacity(1.0);
    }

    private void handleScanFailed() {
        addCardBtn.setDisable(true);
        failedText.setOpacity(1.0);
        ScannerAnimation.stopScanningLineAnimation(lineScanner);
        bag.setOpacity(0.4);
    }

}
