package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.animation.BattleAnimation;
import be.esi.prj.leagueofpokemons.animation.EffectAnimation;
import be.esi.prj.leagueofpokemons.model.core.*;
import be.esi.prj.leagueofpokemons.util.ImageProcessor;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
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

public class BattleController implements ControllerFXML, PropertyChangeListener {

    private Game game;
    private Battle battle;
    private Player player;
    private Opponent opponent;

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

    // Might encapsulate into its own FXML + Controller
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

    @Override
    public void init() {
        game = Game.getInstance();
        battle = game.startBattle();
        battle.addPropertyChangeListener(this);

        player = battle.getPlayer();
        opponent = battle.getOpponent();

        initializeSlotsPokemon();
        initNameLabels();
        initPokemonIndicators();
        battle.start();
    }

    private void updateRemainingUseLabel(Pokemon pokemon) {
        basicAttackRemainingUseLabel.textProperty().bind(pokemon.remainingUseProperty(false).asString());
        specialAttackRemainingUseLabel.textProperty().bind(pokemon.remainingUseProperty(true).asString());
    }

    private void updatePokemonTypeImage(Pokemon pokemon, boolean isPlayerPokemon) {
        Type type = pokemon.getType();
        String imageUrl = type.name() + "_type.png";
        Image image = new Image(getClass().getResource("/be/esi/prj/leagueofpokemons/pics/common/" + imageUrl).toExternalForm());
        if (isPlayerPokemon) {
            playerPokemonTypeImage.setImage(image);
        } else {
            opponentPokemonTypeImage.setImage(image);
        }
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

    @FXML
    private void escape() {
        battle.removePropertyChangeListener(this);
        SceneManager.switchScene(SceneView.HUB);
    }

    @FXML
    private void backToHub() {
        battle.removePropertyChangeListener(this);
        game.nextStage();
        SceneManager.switchScene(SceneView.HUB);
    }

    @FXML
    private void swapToSelectionPane() {
        hidePanes();
        selectionPane.setVisible(true);
    }

    @FXML
    private void swapToAttackPane() {
        hidePanes();
        attackPane.setVisible(true);
    }

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

    @FXML
    private void swapSlot1Pokemon() {
        Pokemon pokemonSlot1 = player.getSlot1Pokemon();
        player.setNextPokemon(pokemonSlot1);

        battle.prepareTurn(ActionType.SWAP, opponent.think(player.getActivePokemon()));
        playTurnHandler();
    }

    @FXML
    private void swapSlot2Pokemon() {
        Pokemon pokemonSlot2 = player.getSlot2Pokemon();
        player.setNextPokemon(pokemonSlot2);

        battle.prepareTurn(ActionType.SWAP, opponent.think(player.getActivePokemon()));
        playTurnHandler();
    }

    @FXML
    private void normalAttack() {
        battle.prepareTurn(ActionType.BASIC_ATTACK, opponent.think(player.getActivePokemon()));

        playTurnHandler();
    }

    @FXML
    private void specialAttack() {
        battle.prepareTurn(ActionType.SPECIAL_ATTACK, opponent.think(player.getActivePokemon()));

        playTurnHandler();
    }

    @FXML
    private void playTurnHandler() {
        try {
            battle.playTurn();
        } catch (ModelException e) {
            errorPanelController.displayError(e.getMessage());
        }
    }

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
        winnerNameLabel.setText(winnerName);
        swapToBattleOverPane();
    }

    private void handlePokemonDefeatedEvent(CombatEntity defender) {
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
        updatePokemonTypeImage(newPokemon, isPlayerSwap);
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
            double newHPBarValue = (double) turnResultEvent.defenderHP() / turnResultEvent.defender().getActivePokemon().getMaxHP();
            if (turnResultEvent.attacker() == player) {
                BattleAnimation.playDamageAnimation(opponentPokemonImage, opponentPokemonCurrentHPBar, newHPBarValue, opponentPokemonCurrentHPText, turnResultEvent.defenderHP());
            } else {
                BattleAnimation.playDamageAnimation(playerPokemonImage, playerPokemonCurrentHPBar, newHPBarValue, playerPokemonCurrentHPText, turnResultEvent.defenderHP());
            }
        }
        if (turnResultEvent.effectType() == Effect.EffectType.DRAIN) {
            double newHPBarValue = (double) turnResultEvent.attackerHP() / turnResultEvent.attacker().getActivePokemon().getMaxHP();
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
        }
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
        if (turnResultEvent.attacker().getActivePokemon().hasEffect(Effect.EffectType.DODGE)) {
            effectPane.setVisible(true);
            effectLabel.setText("dodge active on " + turnResultEvent.attacker().getActivePokemon().getName());
        } else if (turnResultEvent.defender().getActivePokemon().hasEffect(Effect.EffectType.PARALYZE)) {
            effectPane.setVisible(true);
            effectLabel.setText("paralized active on " + turnResultEvent.defender().getActivePokemon().getName());
        }


        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            effectPane.setVisible(false);
            effectLabel.setText("");
        });
        pause.play();
    }

}
