package be.esi.prj.leagueofpokemons.model.core;

import java.util.List;

public class Team {
    private List<Pokemon> pokemons;
    private int maxSize;
    private Tier maxTier;

    public Pokemon getFirstPokemon() {
        return pokemons.getFirst();
    }

    public boolean addPokemon(Pokemon pokemon) {
        return false;
    }

    public boolean removePokemon(Pokemon pokemon) {
        return false;
    }

    public boolean canAddPokemon(Pokemon pokemon) {
        return false;
    }

    public boolean isDefeated() {
        return false;
    }
}
