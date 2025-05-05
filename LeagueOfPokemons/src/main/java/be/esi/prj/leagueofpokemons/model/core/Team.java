package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private List<Pokemon> pokemons;
    private int maxSize;
    private Tier maxTier;

    public Team(Tier maxTier) {
        pokemons = new ArrayList<>();
        this.maxSize = 3;
        this.maxTier = maxTier;
    }

    public Pokemon getPokemon(int index) {
        if (index < 0 || index >= pokemons.size()) {
            return null;
        }
        return pokemons.get(index);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void addPokemon(Pokemon pokemon) {
        if (!canAddPokemon(pokemon)) {
            throw new ModelException("Can not add this pokemon: " + pokemon);
        }
        pokemons.add(pokemon);
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

    private boolean canAddPokemon(Pokemon pokemon) {
        if (pokemons.size() >= maxSize) {
            return false;
        }
        if (pokemons.contains(pokemon)) {
            return false;
        }
        return !pokemon.getTier().isGreater(maxTier);
    }

    public boolean isDefeated() {
        return pokemons.stream().allMatch(Pokemon::isDefeated);
    }

    public int getTeamSize() {
        return pokemons.size();
    }
}
