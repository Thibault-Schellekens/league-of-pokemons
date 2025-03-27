package be.esi.prj.leagueofpokemons.model.core;

public class Battle {
    private Player player;
    private Opponent opponent;
    private CombatEntity currentTurn;
    private BattleStatus status;

    public void start() {

    }

    public TurnResult playTurn() {
        return null;
    }

    public boolean isOver() {
        return false;
    }

    // Might have to just return the pokemonName
    public CombatEntity getWinner() {
        return null;
    }

    private void switchTurn()  {

    }

    private void handlePlayerTurn() {

    }

    private void handleOpponentTurn() {

    }
}
