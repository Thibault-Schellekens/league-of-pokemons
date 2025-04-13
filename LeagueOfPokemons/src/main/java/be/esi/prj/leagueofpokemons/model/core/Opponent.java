package be.esi.prj.leagueofpokemons.model.core;

public class Opponent extends CombatEntity {
    private int difficulty;

//    public Opponent(int id, String name, int difficulty) {
//        super(id, name, new Team());
//    }

    public void createTeam() {

    }

    public ActionType determineAction() {
        return null;
    }

    @Override
    public void performAction(ActionType actionType, CombatEntity enemy) {
        switch (actionType) {
            case SWAP -> {

            }
            case BASIC_ATTACK, SPECIAL_ATTACK -> {
                boolean isSpecial = actionType == ActionType.SPECIAL_ATTACK;
                AttackResult result = activePokemon.attack(isSpecial, enemy.getActivePokemon());
            }
        }
    }

    public ActionType think() {
        return null;
    }
}
