package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an opponent in the game, which extends {@link CombatEntity}.
 * The opponent has a difficulty level and a team of Pokémon that can be selected
 * for battle. The opponent can make decisions on which action to take based on
 * the player's Pokémon and their team configuration.
 */
public class Opponent extends CombatEntity {
    private int difficulty;
    private TeamBuilder teamBuilder;

    /**
     * Constructs an {@link Opponent} with the specified name and difficulty.
     *
     * @param name      The name of the opponent.
     * @param difficulty The difficulty level of the opponent, influencing the team selection.
     */
    public Opponent(String name, int difficulty) {
        super(name);
        this.difficulty = difficulty;
        this.teamBuilder = new TeamBuilder();
    }

    /**
     * Default constructor for the opponent with a default name ("Opponent") and difficulty (0).
     * Used for tests.
     */
    Opponent() {
        this("Opponent", 0);
    }

    /**
     * Selects the team of Pokémon for the opponent based on the maximum tier allowed.
     * The active Pokémon for the opponent is set as the first Pokémon in the team.
     *
     * @param maxTier The maximum tier of Pokémon allowed for the opponent's team.
     */
    @Override
    public void selectTeam(Tier maxTier) {
        team = teamBuilder.buildTeam(difficulty, maxTier);
        activePokemon = team.getPokemon(0);
    }

    /**
     * Decides which action the opponent will take during their turn.
     * The decision is based on the current status of the opponent's active Pokémon
     * and its effectiveness against the enemy Pokémon.
     *
     * @param enemyPokemon The player's active Pokémon.
     * @return The action type to be performed by the opponent, such as {@link ActionType#SWAP},
     *         {@link ActionType#SPECIAL_ATTACK}, or {@link ActionType#BASIC_ATTACK}.
     */
    public ActionType think(Pokemon enemyPokemon) {
        if (activePokemon.isDefeated()) {
            return ActionType.SWAP;
        }
        Type activeType = activePokemon.getType();
        Type enemyType = enemyPokemon.getType();
        if (activeType.isWeakAgainst(enemyType)) {
            for (Pokemon pokemon : getNonActivePokemons()) {
                if (!pokemon.getType().isWeakAgainst(enemyType)) {
                    return ActionType.SWAP;
                }
            }
        }

        if (!activeType.isWeakAgainst(enemyType) && activePokemon.getRemainingUse(true) > 0) {
            return ActionType.SPECIAL_ATTACK;
        }

        return ActionType.BASIC_ATTACK;
    }

    /**
     * Selects the next Pokémon from the opponent's team to engage in battle.
     * The selection is based on the effectiveness of the opponent's Pokémon against the enemy's Pokémon.
     *
     * @param enemyPokemon The enemy Pokémon to be faced in the battle.
     * @return The next Pokémon the opponent will use in the battle.
     */
    @Override
    public Pokemon getNextPokemon(Pokemon enemyPokemon) {
        Type enemyType = enemyPokemon.getType();
        Pokemon fallback = null;

        for (Pokemon pokemon : getNonActivePokemons()) {
            if (pokemon.getType().isEffectiveAgainst(enemyType)) {
                return pokemon;
            }

            if (!pokemon.getType().isWeakAgainst(enemyType)) {
                fallback = pokemon;
            }

            if (fallback == null) {
                fallback = pokemon;
            }
        }

        return fallback;
    }

    /**
     * Retrieves the list of Pokémon that are not the active Pokémon and are not defeated.
     *
     * @return A list of non-active Pokémon that are still available for battle.
     */
    private List<Pokemon> getNonActivePokemons() {
        List<Pokemon> pokemons = new ArrayList<>();
        for (int i = 0; i < team.getTeamSize(); i++) {
            Pokemon pokemon = team.getPokemon(i);
            if (pokemon != activePokemon && !pokemon.isDefeated()) {
                pokemons.add(pokemon);
            }
        }
        return pokemons;
    }
}
