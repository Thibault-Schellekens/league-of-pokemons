package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a team of Pokémon in the game.
 * A team can contain a maximum of 3 Pokémon, and each Pokémon has a tier that cannot exceed the team's maximum tier.
 */
public class Team {
    private final List<Pokemon> pokemons;
    private final int maxSize;
    private final Tier maxTier;

    /**
     * Constructs a {@link Team} with the specified maximum tier.
     * The team can hold up to 3 Pokémon, and the Pokémon's tier cannot exceed the specified maximum tier.
     *
     * @param maxTier The maximum tier for the Pokémon in the team.
     */
    public Team(Tier maxTier) {
        pokemons = new ArrayList<>();
        this.maxSize = 3;
        this.maxTier = maxTier;
    }

    /**
     * Retrieves the Pokémon at the specified index in the team.
     *
     * @param index The index of the Pokémon in the team.
     * @return The Pokémon at the specified index, or {@code null} if the index is out of bounds.
     */
    public Pokemon getPokemon(int index) {
        if (index < 0 || index >= pokemons.size()) {
            return null;
        }
        return pokemons.get(index);
    }

    /**
     * Gets the maximum size of the team, which is 3.
     *
     * @return The maximum size of the team.
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Adds a Pokémon to the team. The Pokémon must not already exist in the team,
     * and its tier must not exceed the team's maximum tier.
     *
     * @param pokemon The Pokémon to be added to the team.
     * @throws ModelException If the Pokémon cannot be added (due to tier or duplication constraints).
     */
    public void addPokemon(Pokemon pokemon) {
        if (!canAddPokemon(pokemon)) {
            throw new ModelException("Cannot add this Pokémon: " + pokemon);
        }
        pokemons.add(pokemon);
    }

    /**
     * Replaces the Pokémon at the specified index with a new Pokémon.
     *
     * @param index   The index of the Pokémon to be replaced.
     * @param pokemon The new Pokémon to replace the old one.
     */
    public void setPokemon(int index, Pokemon pokemon) {
        pokemons.set(index, pokemon);
    }

    /**
     * Retrieves the index of the specified Pokémon in the team.
     *
     * @param pokemon The Pokémon to find.
     * @return The index of the Pokémon, or {@code -1} if the Pokémon is not in the team.
     */
    public int indexOf(Pokemon pokemon) {
        return pokemons.indexOf(pokemon);
    }

    /**
     * Removes the specified Pokémon from the team. Currently, this method always returns false
     * as removal is not implemented.
     *
     * @param pokemon The Pokémon to be removed.
     * @return {@code false} since removal is not implemented.
     */
    public boolean removePokemon(Pokemon pokemon) {
        return false;
    }

    /**
     * Checks whether the Pokémon can be added to the team.
     * A Pokémon can only be added if:
     * - The team has fewer than the maximum number of Pokémon.
     * - The Pokémon is not already in the team.
     * - The Pokémon's tier does not exceed the team's maximum tier.
     *
     * @param pokemon The Pokémon to check.
     * @return {@code true} if the Pokémon can be added, {@code false} otherwise.
     */
    private boolean canAddPokemon(Pokemon pokemon) {
        if (pokemons.size() >= maxSize) {
            return false;
        }
        if (pokemons.contains(pokemon)) {
            return false;
        }
        return !pokemon.getTier().isGreater(maxTier);
    }

    /**
     * Checks whether the team has been defeated. A team is considered defeated if all Pokémon in the team are defeated.
     *
     * @return {@code true} if all Pokémon in the team are defeated, {@code false} otherwise.
     */
    public boolean isDefeated() {
        return pokemons.stream().allMatch(Pokemon::isDefeated);
    }

    /**
     * Retrieves the number of Pokémon in the team.
     *
     * @return The number of Pokémon in the team.
     */
    public int getTeamSize() {
        return pokemons.size();
    }
}
