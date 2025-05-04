package be.esi.prj.leagueofpokemons.model.core;

import be.esi.prj.leagueofpokemons.util.random.RandomUtil;

import java.util.Objects;

public class Effect {
    private EffectType effectType;
    private Tier tier;

    public enum EffectType {
        BURN(Type.FIRE),
        DODGE(Type.WATER),
        DRAIN(Type.GRASS),
        PARALYZE(Type.LIGHTNING),
        CRIT(Type.FIGHTING),
        ;

        private final Type type;

        EffectType(Type type) {
            this.type = type;
        }

        public EffectTarget getTarget() {
            return switch (this) {
                case BURN, CRIT, DODGE -> EffectTarget.DEFENDER;
                case DRAIN, PARALYZE -> EffectTarget.ATTACKER;
            };
        }
    }

    public enum EffectTarget {
        ATTACKER,
        DEFENDER;
    }

    public Effect(Type type, Tier tier) {
        this.effectType = getEffectTypeFromType(type);
        this.tier = tier;
    }

    public EffectType getEffectType() {
        return effectType;
    }

    private EffectType getEffectTypeFromType(Type type) {
        for (EffectType effectType : EffectType.values()) {
            if (effectType.type == type) {
                return effectType;
            }
        }
        throw new ModelException("Invalid effect type: " + type);
    }

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

    public int getBurnDamage() {
        return 20;
    }

    public int getDrainHeal() {
        return 20;
    }

    public double getDodgeChance() {
        return 1.0;
    }

    public double getParalyzeChance() {
        return 1.0;
    }

    public double getCritChance() {
        return 1.0;
    }

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
