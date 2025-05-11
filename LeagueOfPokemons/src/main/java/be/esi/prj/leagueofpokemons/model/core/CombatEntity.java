package be.esi.prj.leagueofpokemons.model.core;

/**
 * Represents a combat entity in the game, such as a player or an opponent.
 * This class provides common functionality for managing a team's Pokémon and
 * performing actions during combat.
 */
public abstract class CombatEntity {
    protected String name;
    protected Team team;
    protected Pokemon activePokemon;

    /**
     * Constructs a new CombatEntity with the given name.
     *
     * @param name the name of the combat entity (player or opponent)
     */
    protected CombatEntity(String name) {
        this.name = name;
    }

    /**
     * Gets the currently active Pokémon for this combat entity.
     *
     * @return the active Pokémon
     */
    public Pokemon getActivePokemon() {
        return activePokemon;
    }

    /**
     * Checks if the currently active Pokémon is dead (defeated).
     *
     * @return true if the active Pokémon is defeated, otherwise false
     */
    public boolean isActivePokemonDead() {
        return activePokemon.isDefeated();
    }

    /**
     * Gets the first inactive Pokémon in the team.
     *
     * @return the first inactive Pokémon, or null if no inactive Pokémon exists
     */
    public Pokemon getSlot1Pokemon() {
        return getInactivePokemonByIndex(1);
    }

    /**
     * Gets the second inactive Pokémon in the team.
     *
     * @return the second inactive Pokémon, or null if no inactive Pokémon exists
     */
    public Pokemon getSlot2Pokemon() {
        return getInactivePokemonByIndex(2);
    }

    /**
     * Finds an inactive Pokémon in the team by its index.
     *
     * @param index the index of the inactive Pokémon to retrieve
     * @return the inactive Pokémon at the given index, or null if no inactive Pokémon exists
     */
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

    /**
     * Gets the name of this combat entity.
     *
     * @return the name of the combat entity
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this combat entity has been defeated (i.e., all Pokémon in the team are defeated).
     *
     * @return true if the combat entity is defeated, otherwise false
     */
    public boolean isDefeated() {
        return team.isDefeated();
    }

    /**
     * Gets the current HP of the active Pokémon.
     *
     * @return the current HP of the active Pokémon
     */
    public int getActivePokemonCurrentHP() {
        return activePokemon.getCurrentHP();
    }

    /**
     * Gets the URL of the card image for the active Pokémon.
     *
     * @return the URL of the card image
     */
    public String getCardUrl() {
        return activePokemon.getImageUrl();
    }

    /**
     * Performs an attack with the active Pokémon against an enemy's active Pokémon.
     *
     * @param special whether the attack is a special attack or not
     * @param enemy the enemy combat entity to attack
     * @return the result of the attack
     */
    public AttackResult performAttack(boolean special, CombatEntity enemy) {
        return activePokemon.attack(special, enemy.getActivePokemon());
    }

    /**
     * Swaps the current active Pokémon with a new one from the team.
     *
     * @param nextPokemon the Pokémon to swap to
     * @throws ModelException if the swap is invalid (e.g., same Pokémon, Pokémon not in the team)
     */
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

    /**
     * Selects the Pokémon team based on the given tier.
     * This method is abstract and must be implemented by subclasses.
     *
     * @param maxTier the maximum tier for the team selection
     */
    public abstract void selectTeam(Tier maxTier);

    /**
     * Gets the next Pokémon to be used against the enemy's Pokémon.
     * This method is abstract and must be implemented by subclasses.
     *
     * @param enemyPokemon the enemy's active Pokémon
     * @return the next Pokémon to use against the enemy
     */
    public abstract Pokemon getNextPokemon(Pokemon enemyPokemon);
}
