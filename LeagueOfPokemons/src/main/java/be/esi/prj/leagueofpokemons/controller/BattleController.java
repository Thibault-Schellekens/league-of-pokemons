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

public class BattleController implements PropertyChangeListener {

    private Game game;
    private Battle battle;
    private Player player;
    private Opponent opponent;

    @FXML
    private Label playerCurrentPokemonNameLabel;
    @FXML
    private Label opponentCurrentPokemonNameLabel;
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
    @FXML
    private Pane opponentPane;


    public void initialize() {
        System.out.println("Battle Controller Initialized");
        game = Game.getInstance();
        battle = game.startBattle();
        battle.addPropertyChangeListener(this);

        player = battle.getPlayer();
        opponent = battle.getOpponent();

        initializeSlotsPokemon();
        initNameLabels();
        battle.start();
    }

    private void initNameLabels() {
        playerCurrentPokemonNameLabel.textProperty().bind(playerPokemonNameLabel.textProperty());
        opponentCurrentPokemonNameLabel.textProperty().bind(opponentPokemonNameLabel.textProperty());
    }

    private void initializeSlotsPokemon() {
        updateSlotPokemon(player.getSlot1Pokemon(), 1);
        updateSlotPokemon(player.getSlot2Pokemon(), 2);
    }

    private void updateSlotPokemon(Pokemon newSlotPokemon, int slot) {
        if (slot == 1) {
            slot1PokemonNameLabel.setText(newSlotPokemon.getName());
            slot1PokemonCurrentHPBar.setProgress((double) newSlotPokemon.getCurrentHP() / newSlotPokemon.getMaxHP());
        } else if (slot == 2) {
            slot2PokemonNameLabel.setText(newSlotPokemon.getName());
            slot2PokemonCurrentHPBar.setProgress((double) newSlotPokemon.getCurrentHP() / newSlotPokemon.getMaxHP());
        }
    }

    private void updatePokemonInfo(Pokemon pokemon, Label nameLabel, Text currentHPText, Text maxHPText, ProgressBar hpBar) {
        nameLabel.setText(pokemon.getName());
        currentHPText.setText(String.valueOf(pokemon.getCurrentHP()));
        maxHPText.setText(String.valueOf(pokemon.getMaxHP()));
        hpBar.setProgress((double) pokemon.getCurrentHP() / pokemon.getMaxHP());
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
                    updatePokemonInfo(pokemon, playerPokemonNameLabel, playerPokemonCurrentHPText, playerPokemonMaxHPText, playerPokemonCurrentHPBar);
                    playerPokemonImage.setImage(pokemonImage);
                } else {
                    updatePokemonInfo(pokemon, opponentPokemonNameLabel, opponentPokemonCurrentHPText, opponentPokemonMaxHPText, opponentPokemonCurrentHPBar);
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
        opponentPane.setVisible(false);
    }

    // Refactor these to take a Pane as parameters
    public void swapToAttackPane() {
        selectionPane.setVisible(false);
        attackPane.setVisible(true);
    }

    public void swapToTeamPane() {
        selectionPane.setVisible(false);
        teamPane.setVisible(true);
    }

    private void swapToOpponentPane() {
        selectionPane.setVisible(false);
        attackPane.setVisible(false);
        teamPane.setVisible(false);
        opponentPane.setVisible(true);
    }

    public void swapSlot1Pokemon() {
        Pokemon pokemonSlot1 = player.getSlot1Pokemon();
        battle.swap(pokemonSlot1);
    }

    public void swapSlot2Pokemon() {
        Pokemon pokemonSlot2 = player.getSlot2Pokemon();
        battle.swap(pokemonSlot2);
    }

    public void handleOpponentTurn() {
        ActionType action = opponent.think();
        switch (action) {
            case SWAP -> {
                Pokemon nextPokemon = opponent.getNextPokemon();
                battle.swap(nextPokemon);
            }
            case BASIC_ATTACK, SPECIAL_ATTACK -> battle.playTurn(action);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        switch (evt.getPropertyName()) {
            case "playerCurrentPokemon" -> handlePokemonChangeEvent((Pokemon) newValue, true);
            case "opponentCurrentPokemon" -> handlePokemonChangeEvent((Pokemon) newValue, false);
            case "swap" ->
                    handleSwapEvent((Pokemon) newValue, (Pokemon) evt.getOldValue(), player.getActivePokemon().equals(newValue));

            case "turn" -> handleTurnEvent((TurnResult) newValue);
            case "turnChanged" -> handleTurnChangedEvent((CombatEntity) newValue);
        }
    }

    private void handleTurnChangedEvent(CombatEntity newTurn) {
        if (newTurn == player) {
            swapToSelectionPane();
        } else {
            swapToOpponentPane();
        }
    }

    private void handleSwapEvent(Pokemon newPokemon, Pokemon oldPokemon, boolean isPlayerSwap) {
        handlePokemonChangeEvent(newPokemon, isPlayerSwap);

        if (isPlayerSwap) {
            if (oldPokemon.equals(player.getSlot1Pokemon())) {
                updateSlotPokemon(oldPokemon, 1);
            } else if (oldPokemon.equals(player.getSlot2Pokemon())) {
                updateSlotPokemon(oldPokemon, 2);
            }
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
