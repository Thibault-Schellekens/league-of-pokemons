package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;

public class BattleController {

    public void initialize() {
        System.out.println("Battle Controller Initialized");
    }

    public void escape() {
        System.out.println("Escape");
        SceneManager.switchScene(SceneView.HUB);
    }
}
