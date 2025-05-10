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

    public ScannerController() {
        ocrService = new OCRService();
        pokeApiService = new PokeApiService();
        audioManager = AudioManager.getInstance();
    }

    @Override
    public void init() {
        lineStartY = lineScanner.getLayoutY();
        ScannerAnimation.addGlowingAnimation(fileExplorerBtn, glowingHouse);
        setupDropZone();
    }

    public void back() {
        SceneManager.switchScene(SceneView.HUB);
    }

    public void openFileExplorer() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Pokémon Card Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));
        File file = fileChooser.showOpenDialog(fileExplorerBtn.getScene().getWindow());
        if (file != null) {
            handleCardScan(file);
        }
    }

    public void addToCollection() {
        if (scannedCard != null) {
            audioManager.playSound(AudioSound.SCANNER_ADD);
            Game.getInstance().getCollection().addCard(scannedCard);
        }
        reset();
    }

    public void cancelCard() {
        reset();
    }

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

    private void setupDropZone() {
        dropZone.setOnDragOver(this::handleDragOver);
        dropZone.setOnDragDropped(this::handleDragDropped);
        dropZone.setOnDragEntered(this::handleDragEntered);
        dropZone.setOnDragExited(this::handleDragExited);
    }

    private void handleDragEntered(DragEvent event) {
        dropZone.getStyleClass().add("drag-over");
        event.consume();
    }

    private void handleDragExited(DragEvent event) {
        dropZone.getStyleClass().remove("drag-over");
        event.consume();
    }

    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

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

    private void reset() {
        updateCollectionButtons(false);
        successText.setOpacity(0.0);
        failedText.setOpacity(0.0);
        ScannerAnimation.scanCompletedSuccess(cardImage, new Image(Objects.requireNonNull(getClass().getResource("/be/esi/prj/leagueofpokemons/pics/common/emptyCard.png")).toExternalForm()), dropZone);
        cardImage.getStyleClass().add("drop-image");
        scannedCard = null;
    }

    private void handleScanSuccess(Image newImage) {
        updateCollectionButtons(true);
        successText.setOpacity(1.0);
        failedText.setOpacity(0.0);
        ScannerAnimation.stopScanningLineAnimation(lineScanner, lineStartY);
        ScannerAnimation.scanCompletedSuccess(cardImage, newImage, dropZone);
    }

    private void handleScanFailed() {
        updateCollectionButtons(false);
        failedText.setOpacity(1.0);
        successText.setOpacity(0.0);
        ScannerAnimation.stopScanningLineAnimation(lineScanner, lineStartY);
    }

    private void updateCollectionButtons(boolean isSuccess) {
        addCardBtn.setDisable(!isSuccess);
        cancelCardBtn.setDisable(!isSuccess);
        double opacity = isSuccess ? 1.0 : 0.4;
        addCardImg.setOpacity(opacity);
        cancelCardImg.setOpacity(opacity);
    }
}
