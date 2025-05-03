package be.esi.prj.leagueofpokemons.model.core;

import be.esi.prj.leagueofpokemons.util.random.RandomUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Objects;

public class Pokemon {
    private Card card;
    private int currentHP;
    private Attack attackBasic;
    private Attack attackSpecial;
    private Effect currentEffect;

    private double dodge;
    private double paralyze;
    private double crit;
    private int burnDamage;
    private int drainHeal;

    private BooleanProperty defeated = new SimpleBooleanProperty(false);

    public Pokemon(Card card) {
        this.card = card;
        this.currentHP = card.getMaxHP();
        this.attackBasic = new AttackBasic(card.getTier());
        this.attackSpecial = new AttackSpecial(card.getTier(), card.getType());
        this.currentEffect = null;
    }

    public Type getType() {
        return card.getType();
    }

    public Tier getTier() {
        return card.getTier();
    }

    public Effect getCurrentEffect() {
        return currentEffect;
    }

    public BooleanProperty defeatedProperty() {
        return defeated;
    }

    public IntegerProperty remainingUseProperty(boolean special) {
        return special ? attackSpecial.remainingUseProperty() : attackBasic.remainingUseProperty();
    }

    public boolean hasEffect(Effect.EffectType effectType) {
        return hasAnyEffect() &&
                currentEffect.getEffectType() == effectType;
    }

    public boolean hasAnyEffect() {
        return currentEffect != null;
    }

    public AttackResult attack(boolean special, Pokemon defender) {
        if (isDefeated()) {
            throw new ModelException("Defeated pokemon can not attack!");
        }
        if (defender.isDefeated()) {
            throw new ModelException("Can not attack a defeated pokemon!");
        }
        Attack attack = special ? attackSpecial : attackBasic;

        if (hasEffect(Effect.EffectType.DODGE)) {
            consumeEffect();
        }

        String message = "";
        int damage = attack.use(defender.getType());

        double chance = RandomUtil.nextDouble();
        if (hasEffect(Effect.EffectType.PARALYZE)) {
            if (chance < this.paralyze) {
                damage = 0;
                message  = "Attack failed due to paralysis!";
            }
            consumeEffect();
        }

        // We have to check if damage is > 0, in case the attacker attack failed due to paralyzing
        // If the attack fails, then no effect should be applied
        if (attack.hasEffect(special, defender.getType()) && damage > 0) {
            attack.applyEffect(this, defender);
        }

        if (defender.hasEffect(Effect.EffectType.DODGE)) {
            if (chance < defender.dodge) {
                damage = 0;
                message = "Attack dodged!";
            }
        }

        if (hasEffect(Effect.EffectType.CRIT)) {
            if (chance < this.crit) {
                damage = (int) (damage * 1.5);
                message = "Crit attack!";
            }
            consumeEffect();
        }

        if (defender.hasEffect(Effect.EffectType.BURN)) {
            damage += defender.burnDamage;
            message = "Burn applied!";
            defender.consumeEffect();
        }

        defender.takeDamage(damage);

        if (hasEffect(Effect.EffectType.DRAIN)) {
            int healAmount = drainHeal;
            heal(healAmount);
            message = "Draining!";
            consumeEffect();
        }

        return new AttackResult(damage, message);
    }

    public boolean applyEffect(Effect effect) {
        if (effect != null && !isDefeated()) {
            return effect.applyTurn(this);
        }
        return false;
    }

    // Non-private to use in tests
    void takeDamage(int damage) {
        currentHP = Math.max(0, currentHP - damage);
        if (currentHP == 0) {
            defeated.set(true);
        }
    }


    public boolean isDefeated() {
        return defeated.get();
    }

    public void heal(int amount) {
        if (!isDefeated()) {
            currentHP = Math.min(getMaxHP(), amount + currentHP);
            defeated.set(false);
        }
    }

    public void setCurrentEffect(Effect effect) {
        if (applyEffect(effect)) {
            this.currentEffect = effect;
        }
    }

    private void consumeEffect() {
        switch (currentEffect.getEffectType()) {
            case PARALYZE -> paralyze = 0.0;
            case DODGE -> dodge = 0.0;
            case CRIT -> crit = 0.0;
            case DRAIN -> drainHeal = 0;
            case BURN -> burnDamage = 0;
        }
        currentEffect = null;
    }

    public String getName() {
        return card.getName();
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getMaxHP() {
        return card.getMaxHP();
    }

    public String getImageUrl() {
        return card.getImageURL();
    }

    public double getDodge() {
        return dodge;
    }

    public double getParalyze() {
        return paralyze;
    }

    public double getCrit() {
        return crit;
    }

    public int getBurnDamage() {
        return burnDamage;
    }

    public int getDrainHeal() {
        return drainHeal;
    }

    // Check if parameter is between 0 and 1.0
    public void setDodge(double dodge) {
        this.dodge = dodge;
    }

    public void setParalyze(double paralyze) {
        this.paralyze = paralyze;
    }

    public void setCrit(double crit) {
        this.crit = crit;
    }

    public void setBurnDamage(int burnDamage) {
        this.burnDamage = burnDamage;
    }

    public void setDrainHeal(int drainHeal) {
        this.drainHeal = drainHeal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return currentHP == pokemon.currentHP && Objects.equals(card, pokemon.card) && Objects.equals(attackBasic, pokemon.attackBasic) && Objects.equals(attackSpecial, pokemon.attackSpecial) && Objects.equals(currentEffect, pokemon.currentEffect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, currentHP, attackBasic, attackSpecial, currentEffect);
    }

}
