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
}
