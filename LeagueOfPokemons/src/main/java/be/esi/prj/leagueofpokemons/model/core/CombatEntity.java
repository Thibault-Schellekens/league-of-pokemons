package be.esi.prj.leagueofpokemons.model.core;

public abstract class CombatEntity {
    protected int id;
    protected String name;
    protected Team team;
    protected Pokemon activePokemon;

    public Pokemon getActivePokemon() {
        return activePokemon;
    }

    public boolean swapActivePokemon(Pokemon pokemon) {
        return false;
    }

    public boolean isDefeated() {
        return false;
    }

    public abstract void performAction(ActionType actionType, CombatEntity enemy);
}
