package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;

public class HubController {

    public void initialize() {
        System.out.println("Initializing Hub Controller");
    }

    public void openScanner() {
        System.out.println("Opening Scanner");
        SceneManager.switchScene(SceneView.SCANNER);
    }

    public void openCollection() {
        System.out.println("Opening Collection");
        SceneManager.switchScene(SceneView.COLLECTION);
    }

    public void openBattle() {
        System.out.println("Opening Battle");
        SceneManager.switchScene(SceneView.BATTLE);
    }
}
