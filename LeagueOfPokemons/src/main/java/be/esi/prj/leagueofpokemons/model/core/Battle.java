package be.esi.prj.leagueofpokemons.model.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

public class Battle {
    private Player player;
    private Opponent opponent;
    private CombatEntity currentTurn;
    private BattleStatus status;

    private final PropertyChangeSupport pcs;

    public Battle(Player player, Opponent opponent) {
        this.player = player;
        this.opponent = opponent;
        currentTurn = (new Random().nextBoolean()) ? player : opponent;
        status = BattleStatus.NOT_STARTED;

        pcs = new PropertyChangeSupport(this);
    }

    public void start() {
        if (status != BattleStatus.NOT_STARTED) {
            throw new ModelException("Battle already started: " + status);
        }
        status = BattleStatus.IN_PROGRESS;
        System.out.println("Battle started");
        System.out.println("Current turn: " + currentTurn.getName());
    }

    public TurnResult playTurn() {
        if (status != BattleStatus.IN_PROGRESS) {
            throw new ModelException("Battle must be in progress: " + status);
        }

        if (currentTurn == player) {

        } else {

        }

        if (player.isDefeated()) {
            status = BattleStatus.OPPONENT_WON;
            System.out.println("Opponent won");
        } else if (opponent.isDefeated()) {
            status = BattleStatus.PLAYER_WON;
            System.out.println("Player won");
        } else {
            switchTurn();
        }

        return null;
    }

    public boolean isOver() {
        return status == BattleStatus.OPPONENT_WON || status == BattleStatus.PLAYER_WON;
    }

    // Might have to just return the id/name
    public CombatEntity getWinner() {
        if (status == BattleStatus.PLAYER_WON) {
            return player;
        } else if (status == BattleStatus.OPPONENT_WON) {
            return opponent;
        }
        return null;
    }

    private void switchTurn()  {
        currentTurn = (currentTurn == player) ? opponent : player;
    }

    private TurnResult handlePlayerTurn() {

        return null;
    }

    private TurnResult handleOpponentTurn() {

        return null;
    }

    private TurnResult executeTurn(CombatEntity attacker, CombatEntity defender, ActionType actionType) {
        attacker.performAction(actionType, defender);

        return null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
