package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.core.Battle;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BattleController implements PropertyChangeListener {

    private Battle battle;

    @FXML
    private Pane selectionPane;
    @FXML
    private Pane attackPane;

    public void initialize() {
        System.out.println("Battle Controller Initialized");
    }

    public void swapToAttackPane() {
        selectionPane.setVisible(false);
        attackPane.setVisible(true);
    }

    public void swapToSelectionPane() {
        selectionPane.setVisible(true);
        attackPane.setVisible(false);
    }

    public void normalAttack() {
        System.out.println("Normal attack");
    }

    public void specialAttack() {
        System.out.println("Special attack");
    }

    public void escape() {
        System.out.println("Escape");
        SceneManager.switchScene(SceneView.HUB);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
