package be.esi.prj.leagueofpokemons.model.core;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Attack {
    protected int damage;
    protected Tier tier;
    protected IntegerProperty remainingUse = new SimpleIntegerProperty();

    protected Attack(Tier tier) {
        this.tier = tier;
        this.damage = calculateBaseDamage(tier);
    }

    private int calculateBaseDamage(Tier tier) {
        return switch (tier) {
            case TIER_I -> 30;
            case TIER_II -> 50;
            case TIER_III -> 70;
            case TIER_IV -> 90;
            case TIER_V -> 110;
        };
    }

    public int use(Type enemyType) {
        if (remainingUse.get() <= 0) {
            throw new ModelException("You can not perform this attack anymore!");
        }
        remainingUse.set(remainingUse.get() - 1);
        return calculateDamage(enemyType);
    }

    protected abstract int calculateDamage(Type enemyType);

    // Reduces chance to apply effect if defenderType is weak
    public boolean hasEffect(boolean special, Type defenderType) {
        return special;
    }

    public abstract void applyEffect(Pokemon attacker, Pokemon defender);

    public IntegerProperty remainingUseProperty() {
        return remainingUse;
    }
}
