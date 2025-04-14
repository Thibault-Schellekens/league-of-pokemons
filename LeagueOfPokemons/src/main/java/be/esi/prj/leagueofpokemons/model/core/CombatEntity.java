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

    public Pokemon getSlot1Pokemon() {
        return getInactivePokemonByIndex(1);
    }

    public Pokemon getSlot2Pokemon() {
        return getInactivePokemonByIndex(2);
    }

    private Pokemon getInactivePokemonByIndex(int index) {
        int count = 0;
        for (int i = 0; i < team.getMaxSize(); i++) {
            Pokemon pokemon = team.getPokemon(i);
            if (pokemon != activePokemon) {
                count++;
                if (count == index) {
                    return pokemon;
                }
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

    public boolean swapActivePokemon(Pokemon pokemon) {
        activePokemon = pokemon;
        return false;
    }

    public boolean isDefeated() {
        return false;
    }

    public String getCardUrl() {
        return activePokemon.getImageUrl();
    }

    public abstract AttackResult performAction(ActionType actionType, CombatEntity enemy);

}
