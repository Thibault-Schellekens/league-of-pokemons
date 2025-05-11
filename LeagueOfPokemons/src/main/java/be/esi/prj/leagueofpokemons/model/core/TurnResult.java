package be.esi.prj.leagueofpokemons.model.core;

/**
 * The {@link TurnResult} record represents the outcome of a turn in the battle.
 * It stores details about the attacking and defending combat entities, the damage dealt,
 * the resulting health of both entities, whether the defender's Pokémon is defeated,
 * and any effects applied during the turn.
 * <p>
 * This record is immutable and stores the following information:
 * </p>
 * <ul>
 *     <li>{@link #attacker()} - The combat entity that initiated the attack.</li>
 *     <li>{@link #defender()} - The combat entity that is being attacked.</li>
 *     <li>{@link #damage()} - The amount of damage dealt by the attacker.</li>
 *     <li>{@link #defenderHP()} - The remaining health points of the defender after the attack.</li>
 *     <li>{@link #attackerHP()} - The remaining health points of the attacker after the attack.</li>
 *     <li>{@link #isPokemonDefeated()} - Whether the defender's Pokémon was defeated in the turn.</li>
 *     <li>{@link #effectType()} - The type of effect applied during the turn (if any).</li>
 * </ul>
 */
public record TurnResult(
        /**
         * The combat entity that initiated the attack.
         */
        CombatEntity attacker,

        /**
         * The combat entity that is being attacked.
         */
        CombatEntity defender,

        /**
         * The amount of damage dealt by the attacker.
         */
        int damage,

        /**
         * The remaining health points of the defender after the attack.
         */
        int defenderHP,

        /**
         * The remaining health points of the attacker after the attack.
         */
        int attackerHP,

        /**
         * Whether the defender's Pokémon was defeated in the turn.
         */
        boolean isPokemonDefeated,

        /**
         * The type of effect applied during the turn (if any).
         */
        Effect.EffectType effectType
) {

    /**
     * Retrieves the type of the active Pokémon of the attacking combat entity.
     *
     * @return the {@link Type} representing the elemental type of the attacker's active Pokémon
     */
    public Type attackType() {
        return attacker.getActivePokemonType();
    }

    /**
     * Retrieves the maximum health points (HP) of the defender's currently active Pokémon.
     *
     * @return the maximum HP of the defender's active Pokémon
     */
    public int getDefenderMaxHP() {
        return defender.getActivePokemonMaxHP();
    }

    /**
     * Retrieves the maximum health points (HP) of the attacker's currently active Pokémon.
     *
     * @return the maximum HP of the attacker's active Pokémon
     */
    public int getAttackerMaxHP() {
        return attacker.getActivePokemonMaxHP();
    }

    /**
     * Checks whether a specific effect type is currently active on the attacker's
     * active Pokémon during the current turn.
     *
     * @param effectType the type of effect to check for on the attacker's active Pokémon
     * @return true if the specified effect type is active on the attacker's active Pokémon, false otherwise
     */
    public boolean hasEffectOnAttackerActivePokemon(Effect.EffectType effectType) {
        return attacker.hasEffectOnActivePokemon(effectType);
    }

    /**
     * Checks whether a specific effect type has an effect on the defender's currently active Pokémon.
     *
     * @param effectType the type of the effect to check (e.g., BURN, DODGE, DRAIN, etc.)
     * @return true if the specified effect type has an effect on the defender's active Pokémon, false otherwise
     */
    public boolean hasEffectOnDefenderActivePokemon(Effect.EffectType effectType) {
        return defender.hasEffectOnActivePokemon(effectType);
    }

    /**
     * Retrieves the name of the currently active Pokémon for the attacking combat entity.
     *
     * @return the name of the attacker's active Pokémon
     */
    public String getAttackerActivePokemonName() {
        return attacker.getActivePokemonName();
    }

    /**
     * Retrieves the name of the currently active Pokémon for the defender's combat entity.
     *
     * @return the name of the defender's active Pokémon
     */
    public String getDefenderActivePokemonName() {
        return defender.getActivePokemonName();
    }
}
