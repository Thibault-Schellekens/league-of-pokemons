package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private List<Pokemon> pokemons;
    private int maxSize;
    private Tier maxTier;

    public Team(){
        pokemons = new ArrayList<>();
        this.maxSize = 3;
    }

    public Pokemon getPokemon(int index) {
        return pokemons.get(index);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean addPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
        return false;
    }

    public void setPokemon(int index, Pokemon pokemon) {
        pokemons.set(index, pokemon);
    }

    public int indexOf(Pokemon pokemon) {
        return pokemons.indexOf(pokemon);
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
