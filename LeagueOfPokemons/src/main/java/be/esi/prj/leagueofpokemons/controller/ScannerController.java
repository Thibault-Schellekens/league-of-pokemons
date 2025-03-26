package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.api.PokeApiService;
import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.ocr.CardScanResult;
import be.esi.prj.leagueofpokemons.model.ocr.OCRService;

public class ScannerController {
    private final PokeApiService pokeApiService;
    private final OCRService ocrService;

    public ScannerController() {
        pokeApiService = new PokeApiService();
        ocrService = new OCRService();
    }

    public Card extractCardData() {
        return null;
    }

    private boolean isScanResultValid(CardScanResult scanResult) {
        return false;
    }
}
