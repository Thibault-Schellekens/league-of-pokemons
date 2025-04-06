package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;

public class CollectionController {

    public void initialize() {
        System.out.println("Initializing Collection Controller");
    }

    public void back() {
        SceneManager.switchScene(SceneView.HUB);
    }

}
