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

    public int getRemainingUse(boolean special) {
        return special ? attackSpecial.remainingUseProperty().get() : attackBasic.remainingUseProperty().get();
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

        Effect.EffectType effectType = null;
        int damage = attack.use(defender.getType());

        double chance = RandomUtil.nextDouble();

        // Handle paralyze effect
        if (handleParalyzeEffect(chance)) {
            damage = 0;
            effectType = Effect.EffectType.PARALYZE;
        }

        // Handle dodge effect
        if (handleDodgeEffect(defender, chance)) {
            damage = 0;
            effectType = Effect.EffectType.DODGE;
        }

        // Apply attack effect if applicable
        if (attack.hasEffect(special, defender.getType()) && damage > 0) {
            attack.applyEffect(this, defender);
        }

        // Handle critical hit effect
        if (handleCritEffect(chance)) {
            damage = (int) (damage * 1.5);
            effectType = Effect.EffectType.CRIT;
        }

        // Handle burn effect
        if (handleBurnEffect(defender)) {
            damage += defender.burnDamage;
            effectType = Effect.EffectType.BURN;
            // We have to consume after, or else the burnDamage will be zero.
            defender.consumeEffect();
        }

        defender.takeDamage(damage);

        // Handle drain effect
        if (handleDrainEffect()) {
            effectType = Effect.EffectType.DRAIN;
        }

        return new AttackResult(damage, effectType);
    }

    private boolean handleParalyzeEffect(double chance) {
        if (hasEffect(Effect.EffectType.PARALYZE)) {
            boolean triggered = chance < this.paralyze;
            consumeEffect();
            return triggered;
        }
        return false;
    }

    private boolean handleDodgeEffect(Pokemon defender, double chance) {
        if (defender.hasEffect(Effect.EffectType.DODGE)) {
            return chance < defender.dodge;
        }
        return false;
    }

    private boolean handleCritEffect(double chance) {
        if (hasEffect(Effect.EffectType.CRIT)) {
            boolean triggered = chance < this.crit;
            consumeEffect();
            return triggered;
        }
        return false;
    }

    private boolean handleBurnEffect(Pokemon defender) {
        return defender.hasEffect(Effect.EffectType.BURN);
    }

    private boolean handleDrainEffect() {
        if (hasEffect(Effect.EffectType.DRAIN)) {
            heal(drainHeal);
            consumeEffect();
            return true;
        }
        return false;
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
        return currentHP == pokemon.currentHP && Objects.equals(card, pokemon.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, currentHP, attackBasic, attackSpecial, currentEffect);
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "card=" + card +
                ", currentHP=" + currentHP +
                ", attackBasic=" + attackBasic +
                ", attackSpecial=" + attackSpecial +
                ", currentEffect=" + currentEffect +
                ", dodge=" + dodge +
                ", paralyze=" + paralyze +
                ", crit=" + crit +
                ", burnDamage=" + burnDamage +
                ", drainHeal=" + drainHeal +
                ", defeated=" + defeated +
                '}';
    }
}
