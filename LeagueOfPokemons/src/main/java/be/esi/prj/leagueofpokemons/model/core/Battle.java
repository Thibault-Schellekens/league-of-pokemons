package be.esi.prj.leagueofpokemons.model.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Battle {
    private Player player;
    private Opponent opponent;
    private CombatEntity currentTurn;
    private BattleStatus status;

    private ActionType playerAction;
    private ActionType opponentAction;
    private boolean inTurn;

    private final PropertyChangeSupport pcs;

    public Battle(Player player, Opponent opponent) {
        this.player = player;
        this.opponent = opponent;
        currentTurn = player;
        status = BattleStatus.NOT_STARTED;

        pcs = new PropertyChangeSupport(this);
    }

    public Player getPlayer() {
        return player;
    }

    public Opponent getOpponent() {
        return opponent;
    }

    public void start() {
        if (status != BattleStatus.NOT_STARTED) {
            throw new ModelException("Battle already started: " + status);
        }
        status = BattleStatus.IN_PROGRESS;
        inTurn = false;

        pcs.firePropertyChange("playerCurrentPokemon", null, player.getActivePokemon());
        pcs.firePropertyChange("opponentCurrentPokemon", null, opponent.getActivePokemon());
    }

    private boolean isTurnReady() {
        return playerAction != null && opponentAction != null;
    }

    public void prepareTurn(ActionType playerAction, ActionType opponentAction) {
        this.playerAction = playerAction;
        this.opponentAction = opponentAction;
    }

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

    public boolean isInTurn() {
        return inTurn;
    }

    public String getCurrentTurnName() {
        return currentTurn.getName();
    }

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

    private void handleAttackTurnResult(TurnResult result) {
        pcs.firePropertyChange("attackTurn", null, result);

        if (player.isDefeated()) {
            status = BattleStatus.OPPONENT_WON;
            inTurn = false;
            pcs.firePropertyChange("pokemonDefeated", null, result.defender());
            pcs.firePropertyChange("battleOver", null, opponent.getName());
        } else if (opponent.isDefeated()) {
            status = BattleStatus.PLAYER_WON;
            inTurn = false;
            pcs.firePropertyChange("pokemonDefeated", null, result.defender());
            pcs.firePropertyChange("battleOver", null, player.getName());
        } else if (result.isPokemonDefeated()) {
            inTurn = false;
            if (result.defender() == opponent) {
                currentTurn = opponent;
                swap(opponent.getNextPokemon(opponent.getActivePokemon()));
            }
            switchTurn();
            pcs.firePropertyChange("pokemonDefeated", null, result.defender());
        } else {
            inTurn = playerAction != null || opponentAction != null;
            switchTurn();
        }
    }

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

    private TurnResult executeTurn(CombatEntity attacker, CombatEntity defender, ActionType actionType) {
        if ((actionType == ActionType.BASIC_ATTACK || actionType == ActionType.SPECIAL_ATTACK)
                && attacker.isActivePokemonDead()) {
            throw new ModelException("You can not attack with a dead pokemon!");
        }
        boolean special = actionType == ActionType.SPECIAL_ATTACK;
        AttackResult result = attacker.performAttack(special, defender);
        return new TurnResult(attacker, defender, result.damage(), defender.getActivePokemonCurrentHP(), attacker.getActivePokemonCurrentHP(), defender.isActivePokemonDead(), result.effectType());
    }

    public boolean isOver() {
        return status == BattleStatus.OPPONENT_WON || status == BattleStatus.PLAYER_WON;
    }

    public String getWinner() {
        if (status == BattleStatus.PLAYER_WON) {
            return player.getName();
        } else if (status == BattleStatus.OPPONENT_WON) {
            return opponent.getName();
        }
        return null;
    }

    private void switchTurn() {
        // If turn is over, swap back to player
        if (!inTurn) {
            currentTurn = player;
        } else {
            currentTurn = (currentTurn == player) ? opponent : player;
        }
        pcs.firePropertyChange("turnChanged", null, currentTurn);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
