package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.core.*;
import be.esi.prj.leagueofpokemons.util.ImageProcessor;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

public class BattleController implements PropertyChangeListener {

    private Game game;
    private Battle battle;
    private Player player;
    private Opponent opponent;

    @FXML
    private Label currentPokemonNameLabel;
    @FXML
    private ImageView playerPokemonImage;
    @FXML
    private ImageView opponentPokemonImage;
    @FXML
    private Label playerPokemonNameLabel;
    @FXML
    private Label opponentPokemonNameLabel;
    @FXML
    private Text playerPokemonCurrentHPText;
    @FXML
    private Text playerPokemonMaxHPText;
    @FXML
    private Text opponentPokemonCurrentHPText;
    @FXML
    private Text opponentPokemonMaxHPText;
    @FXML
    private ProgressBar playerPokemonCurrentHPBar;
    @FXML
    private ProgressBar opponentPokemonCurrentHPBar;
    @FXML
    private Label slot1PokemonNameLabel;
    @FXML
    private ProgressBar slot1PokemonCurrentHPBar;
    @FXML
    private Label slot2PokemonNameLabel;
    @FXML
    private ProgressBar slot2PokemonCurrentHPBar;

    @FXML
    private Pane selectionPane;
    @FXML
    private Pane attackPane;
    @FXML
    private Pane teamPane;

    public void initialize() {
        System.out.println("Battle Controller Initialized");
        game = Game.getInstance();
        battle = game.startBattle();
        battle.addPropertyChangeListener(this);

        player = battle.getPlayer();
        opponent = battle.getOpponent();

        initializeSlotsPokemon();
        battle.start();
    }

    private void initializeSlotsPokemon() {
        Pokemon pokemonSlot1 = player.getSlot1Pokemon();
        slot1PokemonNameLabel.setText(pokemonSlot1.getName());
        slot1PokemonCurrentHPBar.setProgress((double) pokemonSlot1.getCurrentHP() / pokemonSlot1.getMaxHP());
        Pokemon pokemonSlot2 = player.getSlot2Pokemon();
        slot2PokemonNameLabel.setText(pokemonSlot2.getName());
        slot2PokemonCurrentHPBar.setProgress((double) pokemonSlot2.getCurrentHP() / pokemonSlot2.getMaxHP());
    }

    private void updatePokemonInfo(Pokemon pokemon, Label nameLabel, Text currentHPText, Text maxHPText) {
        nameLabel.setText(pokemon.getName());
        currentHPText.setText(String.valueOf(pokemon.getCurrentHP()));
        maxHPText.setText(String.valueOf(pokemon.getMaxHP()));
    }

    private Image buildPokemonImage(String imageUrl) {
        try {
            BufferedImage pokemonImage = ImageIO.read(new URI(imageUrl).toURL());
            BufferedImage pokemonRegion = ImageProcessor.extractRegion(pokemonImage, 50, 80, 500, 300);
            return SwingFXUtils.toFXImage(pokemonRegion, null);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePokemonChangeEvent(Pokemon pokemon, boolean isPlayerPokemon) {
        new Thread(() -> {
            String imageUrl = pokemon.getImageUrl();
            Image pokemonImage = buildPokemonImage(imageUrl);
            Platform.runLater(() -> {
                if (isPlayerPokemon) {
                    updatePokemonInfo(pokemon, playerPokemonNameLabel, playerPokemonCurrentHPText, playerPokemonMaxHPText);
                    playerPokemonImage.setImage(pokemonImage);
                    currentPokemonNameLabel.setText(pokemon.getName());
                } else {
                    updatePokemonInfo(pokemon, opponentPokemonNameLabel, opponentPokemonCurrentHPText, opponentPokemonMaxHPText);
                    opponentPokemonImage.setImage(pokemonImage);
                }
            });
        }).start();
    }

    public void normalAttack() {
        System.out.println("Normal attack");
        battle.playTurn(ActionType.BASIC_ATTACK);
    }

    public void specialAttack() {
        System.out.println("Special attack");
        battle.playTurn(ActionType.SPECIAL_ATTACK);
    }

    public void escape() {
        System.out.println("Escape");
        SceneManager.switchScene(SceneView.HUB);
    }

    public void swapToSelectionPane() {
        selectionPane.setVisible(true);
        attackPane.setVisible(false);
        teamPane.setVisible(false);
    }

    public void swapToAttackPane() {
        selectionPane.setVisible(false);
        attackPane.setVisible(true);
    }

    public void swapToTeamPane() {
        selectionPane.setVisible(false);
        teamPane.setVisible(true);
    }

    public void swapSlot1Pokemon() {
        Pokemon pokemonSlot1 = player.getSlot1Pokemon();
        player.swapActivePokemon(pokemonSlot1);
    }

    public void swapSlot2Pokemon() {
        Pokemon pokemonSlot2 = player.getSlot2Pokemon();
        player.swapActivePokemon(pokemonSlot2);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        switch (evt.getPropertyName()) {
            case "playerCurrentPokemon" -> handlePokemonChangeEvent((Pokemon) newValue, true);
            case "opponentCurrentPokemon" -> handlePokemonChangeEvent((Pokemon) newValue, false);
            case "turn" -> handleTurnEvent((TurnResult) newValue);
        }
    }

    private void handleTurnEvent(TurnResult turnResultEvent) {
        if (turnResultEvent.attacker() == player) {
            opponentPokemonCurrentHPText.setText(String.valueOf(turnResultEvent.defenderHP()));
            opponentPokemonCurrentHPBar.setProgress((double) turnResultEvent.defenderHP() / turnResultEvent.defender().getActivePokemon().getMaxHP());
        } else {
            playerPokemonCurrentHPText.setText(String.valueOf(turnResultEvent.defenderHP()));
            playerPokemonCurrentHPBar.setProgress((double) turnResultEvent.defenderHP() / turnResultEvent.defender().getActivePokemon().getMaxHP());
        }
    }
}
