package be.esi.prj.leagueofpokemons.model.core;

public class Opponent extends CombatEntity {
    private int difficulty;

//    public Opponent(int id, String name, int difficulty) {
//        super(id, name, new Team());
//    }

    public void createTeam() {
        team = new Team();
        team.addPokemon(new Pokemon(new Card("swsh1-31", "Scorbunny", 70, "https://assets.tcgdex.net/en/swsh/swsh1/31/high.png", Type.FIRE)));
        team.addPokemon(new Pokemon(new Card("swsh3-94", "Hippowdon", 150, "https://assets.tcgdex.net/en/swsh/swsh3/94/high.png", Type.FIGHTING)));
        activePokemon = team.getPokemon(0);
    }

    @Override
    public AttackResult performAction(ActionType actionType, CombatEntity enemy) {
        switch (actionType) {
            case SWAP -> {

            }
            case BASIC_ATTACK, SPECIAL_ATTACK -> {
                boolean isSpecial = actionType == ActionType.SPECIAL_ATTACK;
                AttackResult result = activePokemon.attack(isSpecial, enemy.getActivePokemon());
            }
        }

        return null;
    }

    public ActionType think() {
        if (activePokemon.isDefeated()) {
            return ActionType.SWAP;
        }
        return ActionType.BASIC_ATTACK;
    }

    public Pokemon getNextPokemon() {
        if (activePokemon == team.getPokemon(0)) {
            return team.getPokemon(1);
        } else {
            return team.getPokemon(0);
        }
    }
}
