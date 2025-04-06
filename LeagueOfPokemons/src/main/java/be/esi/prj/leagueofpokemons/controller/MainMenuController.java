package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.util.GameManager;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;

public class MainMenuController {

    public void initialize() {
        System.out.println("Initializing MainMenu Controller");
        System.out.println("Initialized with player id : " + GameManager.getInstance().getCurrPlayerId());
    }

    public void openSettings() {
        System.out.println("Settings");
    }

    public void play() {
        System.out.println("Switching to Hub");
        SceneManager.switchScene(SceneView.HUB);
    }

    public void quit() {
        System.out.println("Quit");
    }

    public void save() {
        GameManager.getInstance().saveGame();
    }

    public void load() {
        System.out.println("Loading");
    }

    public void news() {
        System.out.println("News");
    }
}
