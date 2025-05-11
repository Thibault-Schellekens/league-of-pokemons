package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.animation.BattleAnimation;
import be.esi.prj.leagueofpokemons.animation.EffectAnimation;
import be.esi.prj.leagueofpokemons.model.core.*;
import be.esi.prj.leagueofpokemons.util.ImageProcessor;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import be.esi.prj.leagueofpokemons.util.audio.AudioManager;
import be.esi.prj.leagueofpokemons.util.audio.AudioSound;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


/**
 * Controller for managing the Pokémon battle screen. Handles interactions, UI updates,
 * and responses to model changes during a battle.
 */
public class BattleController implements ControllerFXML, PropertyChangeListener {

    private Game game;
    private Battle battle;
    private Player player;
    private Opponent opponent;

    private final AudioManager audioManager;

    @FXML
    private SettingsController settingsMenuController;

    @FXML
    private Text currentStageText;
    @FXML
    private Label playerCurrentPokemonNameLabel;
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
    private Label winnerNameLabel;
    @FXML
    private Label basicAttackRemainingUseLabel;
    @FXML
    private Label specialAttackRemainingUseLabel;

    @FXML
    private Pane selectionPane;
    @FXML
    private Pane attackPane;
    @FXML
    private Pane teamPane;
    @FXML
    private Pane inTurnPane;
    @FXML
    private Pane battleOverPane;


    @FXML
    private ErrorController errorPanelController;
    @FXML
    private Circle playerSlot1Indicator;
    @FXML
    private Circle playerSlot2Indicator;
    @FXML
    private Circle playerSlot3Indicator;
    @FXML
    private Circle opponentSlot1Indicator;
    @FXML
    private Circle opponentSlot2Indicator;
    @FXML
    private Circle opponentSlot3Indicator;

    @FXML
    private Pane effectPane;
    @FXML
    private Label effectLabel;

    @FXML
    private ImageView playerPokemonTypeImage;
    @FXML
    private ImageView opponentPokemonTypeImage;

    @FXML
    private ImageView slot1PokemonTypeImage;
    @FXML
    private ImageView slot2PokemonTypeImage;

    public BattleController() {
        audioManager = AudioManager.getInstance();
    }

    /**
     * Initializes the battle controller by setting up the game state, player and opponent references,
     * UI elements, and starting the battle music and logic.
     */
    @Override
    public void init() {
        game = Game.getInstance();
        battle = game.startBattle();
        battle.addPropertyChangeListener(this);

        player = battle.getPlayer();
        opponent = battle.getOpponent();

        currentStageText.setText("Battle " + game.getCurrentStage());
        initializeSlotsPokemon();
        initNameLabels();
        initPokemonIndicators();
        battle.start();
        audioManager.playSound(AudioSound.IN_BATTLE, 0.2);
    }

    /**
     * Opens the settings panel using the associated settings controller.
     */
    @FXML
    private void openSettings() {
        if (settingsMenuController != null) {
            settingsMenuController.showSettings();
        }
    }

    private void updateRemainingUseLabel(Pokemon pokemon) {
        basicAttackRemainingUseLabel.textProperty().bind(pokemon.remainingUseProperty(false).asString());
        specialAttackRemainingUseLabel.textProperty().bind(pokemon.remainingUseProperty(true).asString());
    }

    private void updatePokemonTypeImage(Pokemon pokemon, boolean isPlayerPokemon) {
        Image image = getTypeImage(pokemon);
        if (isPlayerPokemon) {
            playerPokemonTypeImage.setImage(image);
        } else {
            opponentPokemonTypeImage.setImage(image);
        }
    }

    private Image getTypeImage(Pokemon pokemon) {
        Type type = pokemon.getType();
        String imageUrl = type.name() + "_type.png";
        return new Image(getClass().getResource("/be/esi/prj/leagueofpokemons/pics/common/" + imageUrl).toExternalForm());
    }

    private void initPokemonIndicators() {
        updatePokemonIndicator(playerSlot1Indicator, player.getActivePokemon());
        updatePokemonIndicator(playerSlot2Indicator, player.getSlot1Pokemon());
        updatePokemonIndicator(playerSlot3Indicator, player.getSlot2Pokemon());
        updatePokemonIndicator(opponentSlot1Indicator, opponent.getActivePokemon());
        updatePokemonIndicator(opponentSlot2Indicator, opponent.getSlot1Pokemon());
        updatePokemonIndicator(opponentSlot3Indicator, opponent.getSlot2Pokemon());
    }

    private void updatePokemonIndicator(Circle indicator, Pokemon pokemon) {
        if (pokemon == null) {
            indicator.setFill(Color.TRANSPARENT);
        } else {
            ObjectBinding<Color> fillBinding = Bindings.createObjectBinding(() ->
                            pokemon.isDefeated() ? Color.GRAY : Color.RED,
                    pokemon.defeatedProperty()
            );
            indicator.fillProperty().bind(fillBinding);
        }
    }

    private void initNameLabels() {
        playerCurrentPokemonNameLabel.textProperty().bind(playerPokemonNameLabel.textProperty());
    }

    private void initializeSlotsPokemon() {
        updateSlotPokemon(player.getSlot1Pokemon(), 1);
        updateSlotPokemon(player.getSlot2Pokemon(), 2);
    }

    private void updateSlotPokemon(Pokemon newSlotPokemon, int slot) {
        if (slot == 1) {
            slot1PokemonNameLabel.setText(newSlotPokemon.getName());
            slot1PokemonCurrentHPBar.setProgress((double) newSlotPokemon.getCurrentHP() / newSlotPokemon.getMaxHP());
            slot1PokemonTypeImage.setImage(getTypeImage(newSlotPokemon));
        } else if (slot == 2) {
            slot2PokemonNameLabel.setText(newSlotPokemon.getName());
            slot2PokemonCurrentHPBar.setProgress((double) newSlotPokemon.getCurrentHP() / newSlotPokemon.getMaxHP());
            slot2PokemonTypeImage.setImage(getTypeImage(newSlotPokemon));
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
            errorPanelController.displayError(e.getMessage());
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/esi/prj/leagueofpokemons/pics/common/emptyCard.png")));
        }
    }

    private void handlePokemonChangeEvent(Pokemon pokemon, boolean isPlayerPokemon, boolean isDead) {
        new Thread(() -> {
            String imageUrl = pokemon.getImageUrl();
            Image pokemonImage = buildPokemonImage(imageUrl);
            Runnable updateFunction = () -> {
                Platform.runLater(() -> {
                    if (isPlayerPokemon) {
                        updateRemainingUseLabel(pokemon);
                        updatePokemonInfo(pokemon, playerPokemonNameLabel, playerPokemonCurrentHPText, playerPokemonMaxHPText, playerPokemonCurrentHPBar);
                        playerPokemonImage.setImage(pokemonImage);
                        playerPokemonImage.setOpacity(1.0);
                    } else {
                        updatePokemonInfo(pokemon, opponentPokemonNameLabel, opponentPokemonCurrentHPText, opponentPokemonMaxHPText, opponentPokemonCurrentHPBar);
                        opponentPokemonImage.setImage(pokemonImage);
                        opponentPokemonImage.setOpacity(1.0);
                    }
                    updatePokemonTypeImage(pokemon, isPlayerPokemon);
                });
            };
            if (isDead && BattleAnimation.isAnimationPlaying()) {
                BattleAnimation.setFunction(updateFunction);
            } else {
                updateFunction.run();
            }
        }).start();
    }

    /**
     * Escapes from the current battle and returns to the HUB screen, stopping battle music.
     */
    @FXML
    private void escape() {
        audioManager.stopSound(AudioSound.IN_BATTLE);
        battle.removePropertyChangeListener(this);
        SceneManager.switchScene(SceneView.HUB);
    }

    /**
     * Returns to the HUB scene, stopping the battle music and advancing to the next stage.
     */
    @FXML
    private void backToHub() {
        audioManager.stopSound(AudioSound.BATTLE_WON);
        battle.removePropertyChangeListener(this);
        game.nextStage();
        SceneManager.switchScene(SceneView.HUB);
    }

    /**
     * Displays the selection pane in the UI.
     */
    @FXML
    private void swapToSelectionPane() {
        hidePanes();
        selectionPane.setVisible(true);
    }

    /**
     * Displays the attack pane in the UI.
     */
    @FXML
    private void swapToAttackPane() {
        hidePanes();
        attackPane.setVisible(true);
    }

    /**
     * Displays the team pane in the UI.
     */
    @FXML
    private void swapToTeamPane() {
        hidePanes();
        teamPane.setVisible(true);
    }

    private void swapToOpponentPane() {
        hidePanes();
        inTurnPane.setVisible(true);
    }

    private void swapToBattleOverPane() {
        hidePanes();
        battleOverPane.setVisible(true);
    }

    private void hidePanes() {
        selectionPane.setVisible(false);
        attackPane.setVisible(false);
        teamPane.setVisible(false);
        inTurnPane.setVisible(false);
    }

    /**
     * Switches the player's active Pokémon to the Pokémon in slot 1 and triggers a turn.
     */
    @FXML
    private void swapSlot1Pokemon() {
        Pokemon pokemonSlot1 = player.getSlot1Pokemon();
        player.setNextPokemon(pokemonSlot1);

        battle.prepareTurn(ActionType.SWAP, opponent.think(player.getActivePokemon()));
        playTurnHandler();
    }

    /**
     * Switches the player's active Pokémon to the Pokémon in slot 2 and triggers a turn.
     */
    @FXML
    private void swapSlot2Pokemon() {
        Pokemon pokemonSlot2 = player.getSlot2Pokemon();
        player.setNextPokemon(pokemonSlot2);

        battle.prepareTurn(ActionType.SWAP, opponent.think(player.getActivePokemon()));
        playTurnHandler();
    }

    /**
     * Executes a normal (basic) attack by the player's active Pokémon.
     */
    @FXML
    private void normalAttack() {
        battle.prepareTurn(ActionType.BASIC_ATTACK, opponent.think(player.getActivePokemon()));

        playTurnHandler();
    }

    /**
     * Executes a special attack by the player's active Pokémon.
     */
    @FXML
    private void specialAttack() {
        battle.prepareTurn(ActionType.SPECIAL_ATTACK, opponent.think(player.getActivePokemon()));

        playTurnHandler();
    }

    /**
     * Plays the current turn in the battle, handling model exceptions.
     */
    @FXML
    private void playTurnHandler() {
        try {
            battle.playTurn();
        } catch (ModelException e) {
            errorPanelController.displayError(e.getMessage());
        }
    }

    /**
     * Listens for property changes in the model and delegates appropriate handlers
     * based on the type of event.
     *
     * @param evt the property change event received from the model
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();

        switch (evt.getPropertyName()) {
            case "playerCurrentPokemon" -> handlePokemonChangeEvent((Pokemon) newValue, true, false);
            case "opponentCurrentPokemon" -> handlePokemonChangeEvent((Pokemon) newValue, false, false);
            case "swap" ->
                    handleSwapEvent((Pokemon) newValue, (Pokemon) evt.getOldValue(), player.getActivePokemon().equals(newValue));

            case "attackTurn" -> handleAttackTurnEvent((TurnResult) newValue);
            case "turnChanged" -> handleTurnChangedEvent();

            case "pokemonDefeated" -> handlePokemonDefeatedEvent((CombatEntity) newValue);

            case "battleOver" -> handleBattleOverEvent((String) newValue);

            default -> System.err.println("Unhandled property change event: " + evt.getPropertyName());
        }
    }

    private void handleBattleOverEvent(String winnerName) {
        audioManager.stopSound(AudioSound.IN_BATTLE);
        if (winnerName.equals(player.getName())) {
            audioManager.playSound(AudioSound.BATTLE_WON);
        }
        winnerNameLabel.setText(winnerName);
        swapToBattleOverPane();
    }

    private void handlePokemonDefeatedEvent(CombatEntity defender) {
        audioManager.playSound(AudioSound.KO_EFFECT);
        if (defender == player) {
            BattleAnimation.playDeathAnimation(playerPokemonImage);
            swapToTeamPane();
        } else {
            BattleAnimation.playDeathAnimation(opponentPokemonImage);
        }
    }

    private void handleTurnChangedEvent() {
        if (battle.isInTurn()) {
            swapToOpponentPane();
        } else {
            swapToSelectionPane();
        }
    }

    private void handleSwapEvent(Pokemon newPokemon, Pokemon oldPokemon, boolean isPlayerSwap) {
        handlePokemonChangeEvent(newPokemon, isPlayerSwap, oldPokemon.isDefeated());
        if (isPlayerSwap) {
            if (oldPokemon.equals(player.getSlot1Pokemon())) {
                updateSlotPokemon(oldPokemon, 1);
            } else if (oldPokemon.equals(player.getSlot2Pokemon())) {
                updateSlotPokemon(oldPokemon, 2);
            }
        }
    }

    private void handleAttackTurnEvent(TurnResult turnResultEvent) {
        if (turnResultEvent.damage() > 0) {
            handleAttackSound(turnResultEvent.attackType());
            double newHPBarValue = (double) turnResultEvent.defenderHP() / turnResultEvent.getDefenderMaxHP();
            if (turnResultEvent.attacker() == player) {
                BattleAnimation.playDamageAnimation(opponentPokemonImage, opponentPokemonCurrentHPBar, newHPBarValue, opponentPokemonCurrentHPText, turnResultEvent.defenderHP());
            } else {
                BattleAnimation.playDamageAnimation(playerPokemonImage, playerPokemonCurrentHPBar, newHPBarValue, playerPokemonCurrentHPText, turnResultEvent.defenderHP());
            }
        }
        if (turnResultEvent.effectType() == Effect.EffectType.DRAIN) {
            double newHPBarValue = (double) turnResultEvent.attackerHP() / turnResultEvent.getAttackerMaxHP();
            if (turnResultEvent.attacker() == player) {
                BattleAnimation.playRestoreHealthAnimation(playerPokemonCurrentHPBar, newHPBarValue, playerPokemonCurrentHPText, turnResultEvent.attackerHP());
            } else {
                BattleAnimation.playRestoreHealthAnimation(opponentPokemonCurrentHPBar, newHPBarValue, opponentPokemonCurrentHPText, turnResultEvent.attackerHP());
            }
        }


        handleEffectMessage(turnResultEvent);

        Effect.EffectType effectType = turnResultEvent.effectType();
        if (effectType != null) {
            handleEffectAnimation(effectType, turnResultEvent);
            handleEffectSound(effectType);
        }
    }

    private void handleEffectSound(Effect.EffectType effectType) {
        String soundString = "EFFECT_" + effectType.name();
        AudioSound sound = AudioSound.valueOf(soundString);
        audioManager.playSound(sound);
    }

    private void handleAttackSound(Type type) {
        String soundString = "ATTACK_" + type.name();
        AudioSound sound = AudioSound.valueOf(soundString);
        audioManager.playSound(sound);
    }

    private void handleEffectAnimation(Effect.EffectType effectType, TurnResult turnResultEvent) {
        Effect.EffectTarget effectTarget = effectType.getTarget();
        ImageView targetImageView;

        if (effectTarget == Effect.EffectTarget.DEFENDER) {
            targetImageView = (turnResultEvent.attacker() == player) ? opponentPokemonImage : playerPokemonImage;
        } else { // ATTACKER
            targetImageView = (turnResultEvent.attacker() == player) ? playerPokemonImage : opponentPokemonImage;
        }
        Pane parentPane = (Pane) targetImageView.getParent();
        EffectAnimation.playEffectAnimation(targetImageView, parentPane, effectType);
    }

    private void handleEffectMessage(TurnResult turnResultEvent) {
        if (turnResultEvent.hasEffectOnAttackerActivePokemon(Effect.EffectType.DODGE)) {
            effectPane.setVisible(true);
            effectLabel.setText("dodge active on " + turnResultEvent.getAttackerActivePokemonName());
        } else if (turnResultEvent.hasEffectOnDefenderActivePokemon(Effect.EffectType.PARALYZE)) {
            effectPane.setVisible(true);
            effectLabel.setText("paralized active on " + turnResultEvent.getDefenderActivePokemonName());
        }


        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            effectPane.setVisible(false);
            effectLabel.setText("");
        });
        pause.play();
    }

}
