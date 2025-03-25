package be.esi.prj.leagueofpokemons.model.core;

public class Opponent extends CombatEntity {
    private int difficulty;

    public void createTeam() {

    }

    public ActionType determineAction() {
        return null;
    }

    @Override
    public void performAction(ActionType actionType, CombatEntity enemy) {

    }
}
