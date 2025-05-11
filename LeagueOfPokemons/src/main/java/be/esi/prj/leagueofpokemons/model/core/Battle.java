package be.esi.prj.leagueofpokemons.model.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Represents a battle between a player and an opponent, handling the combat logic,
 * turn-based actions, and determining the winner.
 * <p>
 * The battle involves managing the turns, actions (such as basic or special attacks),
 * swapping Pokémon, and tracking the progress of the battle until one side wins.
 * </p>
 */
public class Battle {
    private Player player;
    private Opponent opponent;
    private CombatEntity currentTurn;
    private BattleStatus status;

    private ActionType playerAction;
    private ActionType opponentAction;
    private boolean inTurn;

    private final PropertyChangeSupport pcs;

    /**
     * Constructs a Battle instance with the specified player and opponent.
     * The battle is initially in the NOT_STARTED state.
     *
     * @param player   the player participating in the battle
     * @param opponent the opponent participating in the battle
     */
    public Battle(Player player, Opponent opponent) {
        this.player = player;
        this.opponent = opponent;
        currentTurn = player;
        status = BattleStatus.NOT_STARTED;

        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Retrieves the current player in the game.
     *
     * @return the {@link Player} representing the current player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retrieves the opponent in the game.
     *
     * @return the {@link Opponent} representing the opponent
     */
    public Opponent getOpponent() {
        return opponent;
    }

    /**
     * Checks if it is the current player's turn.
     *
     * @return true if it is the current player's turn, false otherwise
     */
    public boolean isInTurn() {
        return inTurn;
    }

    /**
     * Retrieves the current turn's name.
     *
     * @return the {@link String} name representing the current turn.
     */
    public String getCurrentTurnName() {
        return currentTurn.getName();
    }


    /**
     * Starts the battle if it hasn't started already.
     * Sets the battle status to IN_PROGRESS and fires property changes for both players' Pokémon.
     */
    public void start() {
        if (status != BattleStatus.NOT_STARTED) {
            throw new ModelException("Battle already started: " + status);
        }
        status = BattleStatus.IN_PROGRESS;
        inTurn = false;

        pcs.firePropertyChange("playerCurrentPokemon", null, player.getActivePokemon());
        pcs.firePropertyChange("opponentCurrentPokemon", null, opponent.getActivePokemon());
    }

    /**
     * Prepares the actions for the player and opponent for the current turn.
     *
     * @param playerAction   the action chosen by the player
     * @param opponentAction the action chosen by the opponent
     */
    public void prepareTurn(ActionType playerAction, ActionType opponentAction) {
        this.playerAction = playerAction;
        this.opponentAction = opponentAction;
    }

    /**
     * Swaps the current Pokémon for the player or opponent.
     *
     * @param nextPokemon the Pokémon to swap to
     */
    private void swap(Pokemon nextPokemon) {
        if (status != BattleStatus.IN_PROGRESS) {
            throw new ModelException("Battle must be in progress: " + status);
        }
        if (nextPokemon.isDefeated()) {
            throw new ModelException("You can not swap to a dead pokemon");
        }

        Pokemon oldPokemon = currentTurn.getActivePokemon();

        currentTurn.swap(nextPokemon);

        pcs.firePropertyChange("swap", oldPokemon, nextPokemon);

        if (!oldPokemon.isDefeated() || oldPokemon.isDefeated() && currentTurn == opponent) {
            switchTurn();
        }
    }

    /**
     * Returns whether the current turn is ready for execution, meaning both player and opponent actions are prepared.
     *
     * @return true if both actions are ready, false otherwise
     */
    private boolean isTurnReady() {
        return playerAction != null && opponentAction != null;
    }

    /**
     * Executes the current turn, applying the actions for both the player and the opponent.
     * The turn result is processed, and the battle state is updated accordingly.
     */
    public void playTurn() {
        if (status != BattleStatus.IN_PROGRESS) {
            throw new ModelException("Battle must be in progress: " + status);
        }

        if (!inTurn && !isTurnReady()) {
            throw new ModelException("You must first prepare the turn!");
        }

        if (handleSwapAction()) return;

        TurnResult result;
        if (currentTurn == player && playerAction != null) {
            result = executeTurn(player, opponent, playerAction);
            playerAction = null;
        } else if (currentTurn == opponent && opponentAction != null) {
            result = executeTurn(opponent, player, opponentAction);
            opponentAction = null;
        } else {
            result = null;
        }

        if (result != null) {
            handleAttackTurnResult(result);
        }
    }

    /**
     * Handles the result of an attack turn, updating the battle state and checking for any Pokémon defeats.
     *
     * @param result the result of the turn's attack
     */
    private void handleAttackTurnResult(TurnResult result) {
        pcs.firePropertyChange("attackTurn", null, result);

        if (player.isDefeated()) {
            endBattle(BattleStatus.OPPONENT_WON, player, opponent);
        } else if (opponent.isDefeated()) {
            endBattle(BattleStatus.PLAYER_WON, opponent, player);
        } else if (result.isPokemonDefeated()) {
            handlePokemonDefeated(result);
        } else {
            inTurn = playerAction != null || opponentAction != null;
            switchTurn();
        }
    }

    /**
     * Ends the battle with the specified final status, announcing the winner.
     *
     * @param finalStatus the final status of the battle
     * @param defeated    the defeated combatant
     * @param winner      the winner of the battle
     */
    private void endBattle(BattleStatus finalStatus, CombatEntity defeated, CombatEntity winner) {
        status = finalStatus;
        inTurn = false;
        pcs.firePropertyChange("pokemonDefeated", null, defeated);
        pcs.firePropertyChange("battleOver", null, winner.getName());
    }

    /**
     * Handles the event when a Pokémon is defeated during the battle, triggering the next turn.
     *
     * @param result the result of the turn where the Pokémon was defeated
     */
    private void handlePokemonDefeated(TurnResult result) {
        inTurn = false;

        if (result.defender() == opponent) {
            currentTurn = opponent;
            swap(opponent.getNextPokemon(opponent.getActivePokemon()));
        }

        switchTurn();
        pcs.firePropertyChange("pokemonDefeated", null, result.defender());
    }

    /**
     * Handles the actions for swapping Pokémon.
     *
     * @return true if a swap action was handled, false otherwise
     */
    private boolean handleSwapAction() {
        if (playerAction == ActionType.SWAP) {
            inTurn = true;
            swap(player.getNextPokemon(opponent.getActivePokemon()));
            playerAction = null;
            return true;
        }
        if (opponentAction == ActionType.SWAP) {
            // In turn only if the player hasn't already played
            inTurn = playerAction != null;
            currentTurn = opponent;
            swap(opponent.getNextPokemon(player.getActivePokemon()));
            opponentAction = null;
            return true;
        }
        return false;
    }

    /**
     * Executes a turn for the specified attacker and defender, applying the action and returning the result.
     *
     * @param attacker      the Pokémon performing the attack
     * @param defender      the Pokémon being attacked
     * @param actionType    the type of action being performed
     * @return the result of the turn
     */
    private TurnResult executeTurn(CombatEntity attacker, CombatEntity defender, ActionType actionType) {
        if ((actionType == ActionType.BASIC_ATTACK || actionType == ActionType.SPECIAL_ATTACK)
                && attacker.isActivePokemonDead()) {
            throw new ModelException("You can not attack with a dead pokemon!");
        }
        boolean special = actionType == ActionType.SPECIAL_ATTACK;
        AttackResult result = attacker.performAttack(special, defender);
        return new TurnResult(attacker, defender, result.damage(), defender.getActivePokemonCurrentHP(), attacker.getActivePokemonCurrentHP(), defender.isActivePokemonDead(), result.effectType());
    }

    /**
     * Checks if the battle is over, either by the player or opponent winning.
     *
     * @return true if the battle is over, false otherwise
     */
    public boolean isOver() {
        return status == BattleStatus.OPPONENT_WON || status == BattleStatus.PLAYER_WON;
    }

    /**
     * Gets the name of the winner of the battle.
     *
     * @return the name of the winner if the battle is over, null otherwise
     */
    public String getWinner() {
        if (status == BattleStatus.PLAYER_WON) {
            return player.getName();
        } else if (status == BattleStatus.OPPONENT_WON) {
            return opponent.getName();
        }
        return null;
    }

    /**
     * Switches the turn between the player and the opponent.
     */
    private void switchTurn() {
        // If turn is over, swap back to player
        if (!inTurn) {
            currentTurn = player;
        } else {
            currentTurn = (currentTurn == player) ? opponent : player;
        }
        pcs.firePropertyChange("turnChanged", null, currentTurn);
    }

    /**
     * Adds a listener to receive property change events from the battle.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Removes a listener from receiving property change events from the battle.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
