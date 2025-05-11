package be.esi.prj.leagueofpokemons.model.core;

/**
 * Represents the result of an attack, including the damage dealt and the effect applied.
 * <p>
 * This class encapsulates both the damage dealt by the attack and any effect type that the attack might apply.
 * </p>
 */
public record AttackResult(int damage, Effect.EffectType effectType) {

    /**
     * Constructs an AttackResult with the given damage and no effect.
     * <p>
     * If no effect is applied, the effect type will be set to null.
     * </p>
     *
     * @param damage the damage dealt by the attack
     */
    public AttackResult(int damage) {
        this(damage, null);
    }
}
