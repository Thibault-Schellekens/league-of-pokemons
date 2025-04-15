package be.esi.prj.leagueofpokemons.model.core;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Attack {
    private Type type;
    private int damage;

    private IntegerProperty remainingUse = new SimpleIntegerProperty();

    // Might be added later
    private Effect effect;

    public Attack(Type type, Tier tier, boolean special) {
        this.type = type;
        if (special) {
            this.damage = 0;
            this.remainingUse.set(3);
        } else {
            this.damage = calculateBaseDamage(tier);
            this.remainingUse.set(20);
        }
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

    public int calculateDamage(Type enemyType) {
        if (remainingUse.get() <= 0) {
            throw new ModelException("You can not perform this attack anymore!");
        }
        remainingUse.set(remainingUse.get() - 1);
        if (this.type.isEffectiveAgainst(enemyType)) {
            return damage * 2;
        } else if (this.type.isWeakAgainst(enemyType)) {
            return (int) (damage * 0.5);
        } else {
            return damage;
        }
    }

    public boolean hasEffect(boolean special, Type defenderType) {
        return false;
    }

    public void applyEffect(Pokemon toApply) {
    }

    public IntegerProperty remainingUseProperty() {
        return remainingUse;
    }
}
