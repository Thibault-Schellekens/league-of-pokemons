package be.esi.prj.leagueofpokemons.model.core;

public abstract class CombatEntity {
    protected int id;
    protected String name;
    protected Team team;
    protected Pokemon activePokemon;

//    public CombatEntity(int id, String name, Team team) {
//        this.id = id;
//        this.name = name;
//        this.team = team;
//        this.activePokemon = team.getFirstPokemon();
//    }

    public Pokemon getActivePokemon() {
        return activePokemon;
    }

    public String getName() {
        return name;
    }

    public boolean swapActivePokemon(Pokemon pokemon) {
        return false;
    }

    public boolean isDefeated() {
        return false;
    }

    public abstract void performAction(ActionType actionType, CombatEntity enemy);
}
