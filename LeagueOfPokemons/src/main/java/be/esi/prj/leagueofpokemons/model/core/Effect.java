package be.esi.prj.leagueofpokemons.model.core;

import be.esi.prj.leagueofpokemons.util.random.RandomUtil;

import java.util.Objects;

/**
 * Represents an effect that can be applied to a Pokémon during a battle.
 * Effects can have different types and targets, affecting either the attacker or defender.
 */
public class Effect {
    private EffectType effectType;
    private Tier tier;

    /**
     * Enum representing the different types of effects that can be applied.
     * <ul>
     *     <li>{@link #BURN} - Causes the opponent to take damage over time.</li>
     *     <li>{@link #DODGE} - Increases the chance to avoid incoming attacks.</li>
     *     <li>{@link #DRAIN} - Heals the attacker by draining health from the defender.</li>
     *     <li>{@link #PARALYZE} - Reduces the defender's ability to attack or move.</li>
     *     <li>{@link #CRIT} - Increases the chance of landing a critical hit, dealing more damage.</li>
     * </ul>
     */
    public enum EffectType {
        BURN(Type.FIRE),
        DODGE(Type.WATER),
        DRAIN(Type.GRASS),
        PARALYZE(Type.LIGHTNING),
        CRIT(Type.FIGHTING);

        private final Type type;

        EffectType(Type type) {
            this.type = type;
        }

        /**
         * Determines the target of the effect.
         *
         * @return the target of the effect, either the attacker or the defender
         */
        public EffectTarget getTarget() {
            return switch (this) {
                case BURN, CRIT, DODGE -> EffectTarget.DEFENDER;
                case DRAIN, PARALYZE -> EffectTarget.ATTACKER;
            };
        }
    }

    /**
     * Enum representing the possible targets of an effect: either the attacker or the defender.
     */
    public enum EffectTarget {
        ATTACKER,
        DEFENDER;
    }

    /**
     * Constructs an Effect with the specified type and tier.
     *
     * @param type the type of the effect
     * @param tier the tier of the effect
     */
    public Effect(Type type, Tier tier) {
        this.effectType = getEffectTypeFromType(type);
        this.tier = tier;
    }

    /**
     * Gets the effect type of this effect.
     *
     * @return the effect type
     */
    public EffectType getEffectType() {
        return effectType;
    }

    /**
     * Converts a Type to its corresponding EffectType.
     *
     * @param type the type to convert
     * @return the corresponding EffectType
     * @throws ModelException if the type does not correspond to any effect type
     */
    private EffectType getEffectTypeFromType(Type type) {
        for (EffectType e : EffectType.values()) {
            if (e.type == type) {
                return e;
            }
        }
        throw new ModelException("Invalid effect type: " + type);
    }

    /**
     * Determines whether the effect can be applied based on the enemy's type and the effect's tier.
     *
     * @param enemyType the type of the enemy Pokémon
     * @return true if the effect can be applied, otherwise false
     */
    private boolean canApply(Type enemyType) {
        Type type = effectType.type;
        double baseChance = 0.5;

        if (type.isEffectiveAgainst(enemyType)) {
            baseChance *= 1.5;
        } else if (type.isWeakAgainst(enemyType)) {
            baseChance *= 0.5;
        }

        baseChance = baseChance + ((baseChance * (tier.ordinal() + 1)) / 15);
        return RandomUtil.nextDouble() < baseChance;
    }

    /**
     * Gets the burn damage dealt by the effect.
     *
     * @return the burn damage
     */
    public int getBurnDamage() {
        return 20;
    }

    /**
     * Gets the amount of health healed by the drain effect.
     *
     * @return the drain healing amount
     */
    public int getDrainHeal() {
        return 20;
    }

    /**
     * Gets the chance to dodge the effect.
     *
     * @return the dodge chance
     */
    public double getDodgeChance() {
        return 1.0;
    }

    /**
     * Gets the chance to paralyze the Pokémon.
     *
     * @return the paralyze chance
     */
    public double getParalyzeChance() {
        return 1.0;
    }

    /**
     * Gets the chance to land a critical hit.
     *
     * @return the crit chance
     */
    public double getCritChance() {
        return 1.0;
    }

    /**
     * Applies the effect to the specified Pokémon.
     *
     * @param pokemon the Pokémon to apply the effect to
     * @return true if the effect was applied, otherwise false
     */
    public boolean applyTurn(Pokemon pokemon) {
        if (!canApply(pokemon.getType())) {
            return false;
        }

        switch (effectType) {
            case BURN -> {
                pokemon.setBurnDamage(getBurnDamage());
            }
            case DODGE -> {
                pokemon.setDodge(getDodgeChance());
            }
            case DRAIN -> {
                pokemon.setDrainHeal(getDrainHeal());
            }
            case PARALYZE -> {
                pokemon.setParalyze(getParalyzeChance());
            }
            case CRIT -> {
                pokemon.setCrit(getCritChance());
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Effect effect = (Effect) o;
        return effectType == effect.effectType && tier == effect.tier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(effectType, tier);
    }

    @Override
    public String toString() {
        return "Effect{" +
                "effectType=" + effectType +
                ", tier=" + tier +
                '}';
    }
}
