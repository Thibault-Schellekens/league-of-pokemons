package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.animation.ScannerAnimation;
import be.esi.prj.leagueofpokemons.model.api.PokeApiService;
import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.ocr.CardScanResult;
import be.esi.prj.leagueofpokemons.model.ocr.OCRService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

public class ScannerController {
    private final PokeApiService pokeApiService;
    private final OCRService ocrService;

    @FXML
    private Button fileExplorerBtn;
    @FXML
    private ImageView glowingHouse;
    @FXML
    private Line lineScanner;


    public ScannerController() {
        pokeApiService = new PokeApiService();
        ocrService = new OCRService();
    }

    public void initialize() {
        System.out.println("Initializing Scanner");
        ScannerAnimation.addGlowingAnimation(fileExplorerBtn, glowingHouse);
        fileExplorerBtn.setOnMouseClicked(_ -> {
            ScannerAnimation.scanningLineAnimation(lineScanner);
        });
    }

    public Card extractCardData() {
        return null;
    }

    private boolean isScanResultValid(CardScanResult scanResult) {
        return false;
    }

}
