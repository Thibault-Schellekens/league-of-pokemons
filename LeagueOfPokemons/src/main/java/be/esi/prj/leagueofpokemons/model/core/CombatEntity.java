package be.esi.prj.leagueofpokemons.model.core;

public abstract class CombatEntity {
    protected String name;
    protected Team team;
    protected Pokemon activePokemon;

    public CombatEntity(String name) {
        this.name = name;
    }

    public Pokemon getActivePokemon() {
        return activePokemon;
    }

    public boolean isActivePokemonDead() {
        return activePokemon.isDefeated();
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

    public boolean isDefeated() {
        return team.isDefeated();
    }

    public int getActivePokemonCurrentHP() {
        return activePokemon.getCurrentHP();
    }

    public String getCardUrl() {
        return activePokemon.getImageUrl();
    }

    public AttackResult performAttack(boolean special, CombatEntity enemy) {
        return activePokemon.attack(special, enemy.getActivePokemon());
    }

    void swap(Pokemon nextPokemon) {
        if (nextPokemon == null) {
            throw new ModelException("Next pokemon is null!");
        }

        if (nextPokemon.equals(activePokemon)) {
            throw new ModelException("You can not swap to the same pokemon!");
        }

        int activeIndex = team.indexOf(activePokemon);
        int nextIndex = team.indexOf(nextPokemon);

        if (activeIndex == -1 || nextIndex == -1) {
            throw new ModelException("You can not swap to a Pokemon not in your team!");
        }

        team.setPokemon(activeIndex, nextPokemon);
        team.setPokemon(nextIndex, activePokemon);

        activePokemon = nextPokemon;
    }

    public abstract void selectTeam(Tier maxTier);

}
