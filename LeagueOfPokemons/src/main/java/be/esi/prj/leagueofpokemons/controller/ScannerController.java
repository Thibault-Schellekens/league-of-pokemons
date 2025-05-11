package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.animation.ScannerAnimation;
import be.esi.prj.leagueofpokemons.model.api.PokeApiService;
import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.ocr.CardScanResult;
import be.esi.prj.leagueofpokemons.model.ocr.OCRException;
import be.esi.prj.leagueofpokemons.model.ocr.OCRService;
import be.esi.prj.leagueofpokemons.util.*;
import be.esi.prj.leagueofpokemons.util.audio.AudioManager;
import be.esi.prj.leagueofpokemons.util.audio.AudioSound;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Controller for handling the Pokémon card scanning process, allowing users to scan cards via OCR,
 * add them to their collection, and interact with a drag-and-drop interface.
 * <p>
 * This controller manages the scanning, displaying of scan results, and managing the collection of scanned Pokémon cards.
 * </p>
 */
public class ScannerController implements ControllerFXML {
    private final AudioManager audioManager;
    private final OCRService ocrService;
    private final PokeApiService pokeApiService;

    private Card scannedCard;

    @FXML
    private Button fileExplorerBtn;
    @FXML
    private Button addCardBtn;
    @FXML
    private Button cancelCardBtn;
    @FXML
    private ImageView addCardImg;
    @FXML
    private ImageView cancelCardImg;
    @FXML
    private Pane dropZone;
    @FXML
    private ImageView cardImage;
    @FXML
    private ImageView glowingHouse;
    @FXML
    private Line lineScanner;
    @FXML
    private Text successText;
    @FXML
    private Text failedText;

    private double lineStartY;

    /**
     * Constructor for the ScannerController.
     * Initializes OCR service, PokeAPI service, and audio manager.
     */
    public ScannerController() {
        ocrService = new OCRService();
        pokeApiService = new PokeApiService();
        audioManager = AudioManager.getInstance();
    }

    /**
     * Initializes the controller, setting up the scanning line and drop zone.
     */
    @Override
    public void init() {
        lineStartY = lineScanner.getLayoutY();
        ScannerAnimation.addGlowingAnimation(fileExplorerBtn, glowingHouse);
        setupDropZone();
    }

    /**
     * Navigates back to the Hub scene.
     */
    public void back() {
        SceneManager.switchScene(SceneView.HUB);
    }

    /**
     * Opens the file explorer to select an image for Pokémon card scanning.
     *
     * @see FileChooser
     */
    public void openFileExplorer() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Pokémon Card Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));
        File file = fileChooser.showOpenDialog(fileExplorerBtn.getScene().getWindow());
        if (file != null) {
            handleCardScan(file);
        }
    }

    /**
     * Adds the scanned card to the user's collection.
     * The card is only added if a valid scan result was obtained.
     */
    public void addToCollection() {
        if (scannedCard != null) {
            audioManager.playSound(AudioSound.SCANNER_ADD);
            Game.getInstance().getCollection().addCard(scannedCard);
        }
        reset();
    }

    /**
     * Cancels the card scan operation and resets the UI.
     */
    public void cancelCard() {
        reset();
    }

    /**
     * Handles the actual card scanning using OCR.
     * <p>
     * This method processes the selected file, performs OCR to extract card data,
     * and uses the PokeApi service to fetch card details.
     * </p>
     *
     * @param file The image file to be scanned.
     */
    private void handleCardScan(File file) {
        audioManager.playSound(AudioSound.POKEBALL_WOBBLE);
        ScannerAnimation.scanningLineAnimation(lineScanner);
        new Thread(() -> {
            try {
                BufferedImage imageToScan = ImageIO.read(file);
                CardScanResult scanResult = ocrService.scanCardImage(imageToScan);
                if (!scanResult.successful()) {
                    handleScanFailed();
                    return;
                }
                Card card = pokeApiService.getPokemonCard(scanResult.localId(), scanResult.pokemonName()).orElse(null);
                if (card == null) {
                    handleScanFailed();
                    return;
                }
                handleScanSuccess(new Image(card.getImageURL()));
                scannedCard = card;
            } catch (IOException | OCRException e) {
                handleScanFailed();
            }
        }).start();
    }

    /**
     * Sets up the drop zone for drag-and-drop functionality.
     */
    private void setupDropZone() {
        dropZone.setOnDragOver(this::handleDragOver);
        dropZone.setOnDragDropped(this::handleDragDropped);
        dropZone.setOnDragEntered(this::handleDragEntered);
        dropZone.setOnDragExited(this::handleDragExited);
    }

    /**
     * Handles the event when a file drag enters the drop zone.
     */
    private void handleDragEntered(DragEvent event) {
        dropZone.getStyleClass().add("drag-over");
        event.consume();
    }

    /**
     * Handles the event when a file drag exits the drop zone.
     */
    private void handleDragExited(DragEvent event) {
        dropZone.getStyleClass().remove("drag-over");
        event.consume();
    }

    /**
     * Handles the event when a drag operation is over the drop zone.
     */
    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    /**
     * Handles the event when a file is dropped into the drop zone.
     */
    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            File file = db.getFiles().getFirst();
            handleCardScan(file);
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    /**
     * Resets the UI elements to their default states.
     */
    private void reset() {
        updateCollectionButtons(false);
        successText.setOpacity(0.0);
        failedText.setOpacity(0.0);
        ScannerAnimation.scanCompletedSuccess(cardImage, new Image(Objects.requireNonNull(getClass().getResource("/be/esi/prj/leagueofpokemons/pics/common/emptyCard.png")).toExternalForm()), dropZone);
        cardImage.getStyleClass().add("drop-image");
        scannedCard = null;
    }

    /**
     * Handles the case where the scan was successful and updates the UI accordingly.
     *
     * @param newImage The image of the scanned Pokémon card.
     */
    private void handleScanSuccess(Image newImage) {
        updateCollectionButtons(true);
        successText.setOpacity(1.0);
        failedText.setOpacity(0.0);
        ScannerAnimation.stopScanningLineAnimation(lineScanner, lineStartY);
        ScannerAnimation.scanCompletedSuccess(cardImage, newImage, dropZone);
    }

    /**
     * Handles the case where the scan failed and updates the UI accordingly.
     */
    private void handleScanFailed() {
        updateCollectionButtons(false);
        failedText.setOpacity(1.0);
        successText.setOpacity(0.0);
        ScannerAnimation.stopScanningLineAnimation(lineScanner, lineStartY);
    }

    /**
     * Updates the buttons for adding or canceling the card to the collection.
     *
     * @param isSuccess A boolean indicating whether the scan was successful.
     */
    private void updateCollectionButtons(boolean isSuccess) {
        addCardBtn.setDisable(!isSuccess);
        cancelCardBtn.setDisable(!isSuccess);
        double opacity = isSuccess ? 1.0 : 0.4;
        addCardImg.setOpacity(opacity);
        cancelCardImg.setOpacity(opacity);
    }
}
