package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.List;

public class Opponent extends CombatEntity {
    private int difficulty;
    private TeamBuilder teamBuilder;

    public Opponent(String name, int difficulty) {
        super(name);
        this.difficulty = difficulty;
        this.teamBuilder = new TeamBuilder();
    }

    public Opponent() {
        this("Opponent", 0);
    }

    @Override
    public void selectTeam(Tier maxTier) {
        team = teamBuilder.buildTeam(difficulty, maxTier);
        activePokemon = team.getPokemon(0);
    }

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
