package be.esi.prj.leagueofpokemons.model.core;

/**
 * Enum representing the different types of actions that can be performed in the game.
 * These actions can be used for determining the behavior of a Pokémon during a turn in battle.
 * <p>
 * The action types include basic and special attacks, as well as the ability to swap Pokémon.
 * </p>
 */
public enum ActionType {
    /**
     * Represents a basic attack action performed by a Pokémon.
     * This is a standard attack that typically deals damage to the opponent.
     */
    BASIC_ATTACK,

    /**
     * Represents a special attack action performed by a Pokémon.
     * This can be a unique move that may have enhanced effects or damage compared to a basic attack.
     */
    SPECIAL_ATTACK,

    /**
     * Represents a swap action where a player switches out the current Pokémon for another in their team.
     * This can be used to gain a strategic advantage or replace a weakened Pokémon.
     */
    SWAP,
}
