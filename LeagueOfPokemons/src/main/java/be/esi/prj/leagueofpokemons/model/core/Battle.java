package be.esi.prj.leagueofpokemons.model.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Battle {
    private Player player;
    private Opponent opponent;
    private CombatEntity currentTurn;
    private BattleStatus status;

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

        pcs.firePropertyChange("playerCurrentPokemon", null, player.getActivePokemon());
        pcs.firePropertyChange("opponentCurrentPokemon", null, opponent.getActivePokemon());
    }

    public void swap(Pokemon nextPokemon) {
        if (status != BattleStatus.IN_PROGRESS) {
            throw new ModelException("Battle must be in progress: " + status);
        }
        if (nextPokemon.isDefeated()) {
            throw new ModelException("You can not swap to a dead pokemon");
        }

        Pokemon oldPokemon = currentTurn.getActivePokemon();

        currentTurn.swap(nextPokemon);

        pcs.firePropertyChange("swap", oldPokemon, nextPokemon);

        // Only works if opponent is second to play
        if (!oldPokemon.isDefeated() || oldPokemon.isDefeated() && currentTurn == opponent) {
            switchTurn();
        }
    }

    public String getCurrentTurnName() {
        return currentTurn.getName();
    }

    public void playTurn(ActionType action) {
        if (status != BattleStatus.IN_PROGRESS) {
            throw new ModelException("Battle must be in progress: " + status);
        }

        TurnResult result;
        if (currentTurn == player) {
            result = executeTurn(player, opponent, action);
        } else {
            result = executeTurn(opponent, player, action);
        }

        pcs.firePropertyChange("attackTurn", null, result);


        if (player.isDefeated()) {
            status = BattleStatus.OPPONENT_WON;
            pcs.firePropertyChange("battleOver", null, opponent.getName());
        } else if (opponent.isDefeated()) {
            status = BattleStatus.PLAYER_WON;
            pcs.firePropertyChange("battleOver", null, player.getName());
        } else if (result.isPokemonDefeated()) {
            switchTurn();
            pcs.firePropertyChange("pokemonDefeated", null, result.defender());
        } else {
            switchTurn();
        }

    }

    private TurnResult executeTurn(CombatEntity attacker, CombatEntity defender, ActionType actionType) {
        if ((actionType == ActionType.BASIC_ATTACK || actionType == ActionType.SPECIAL_ATTACK)
                && attacker.isActivePokemonDead()) {
            throw new ModelException("You can not attack with a dead pokemon!");
        }
        boolean special = actionType == ActionType.SPECIAL_ATTACK;
        AttackResult result = attacker.performAttack(special, defender);
        return new TurnResult(attacker, defender, defender.getActivePokemon().getCurrentHP(), defender.isActivePokemonDead(), result.effectType());
    }

    public boolean isOver() {
        return status == BattleStatus.OPPONENT_WON || status == BattleStatus.PLAYER_WON;
    }

    // Might have to just return the id/name
    public String getWinner() {
        if (status == BattleStatus.PLAYER_WON) {
            return player.getName();
        } else if (status == BattleStatus.OPPONENT_WON) {
            return opponent.getName();
        }
        return null;
    }

    private void switchTurn() {
        CombatEntity previousTurn = currentTurn;
        currentTurn = (currentTurn == player) ? opponent : player;

        pcs.firePropertyChange("turnChanged", previousTurn, currentTurn);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
