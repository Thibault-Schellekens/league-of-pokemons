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

    /**
     * Constructs a Pokemon object with a given card.
     *
     * @param card the card associated with this Pokemon
     */
    public Pokemon(Card card) {
        this.card = card;
        this.currentHP = card.getMaxHP();
        this.attackBasic = new AttackBasic(card.getTier());
        this.attackSpecial = new AttackSpecial(card.getTier(), card.getType());
        this.currentEffect = null;
    }

    /**
     * Gets the type of the Pokemon.
     *
     * @return the {@link Type} of the Pokemon
     */
    public Type getType() {
        return card.getType();
    }

    /**
     * Gets the tier of the Pokemon.
     *
     * @return the {@link Tier} of the Pokemon
     */
    public Tier getTier() {
        return card.getTier();
    }

    /**
     * Gets the current effect applied to the Pokemon.
     *
     * @return the {@link Effect} applied to the Pokemon
     */
    public Effect getCurrentEffect() {
        return currentEffect;
    }

    /**
     * Gets the property representing whether the Pokemon is defeated.
     *
     * @return the {@link BooleanProperty} representing the defeated state
     */
    public BooleanProperty defeatedProperty() {
        return defeated;
    }

    /**
     * Gets the remaining uses property for either basic or special attack.
     *
     * @param special boolean indicating whether to get the special attack's remaining uses
     * @return the {@link IntegerProperty} representing the remaining uses
     */
    public IntegerProperty remainingUseProperty(boolean special) {
        return special ? attackSpecial.remainingUseProperty() : attackBasic.remainingUseProperty();
    }

    /**
     * Gets the remaining uses of either basic or special attack.
     *
     * @param special boolean indicating whether to get the special attack's remaining uses
     * @return the remaining uses of the attack
     */
    public int getRemainingUse(boolean special) {
        return special ? attackSpecial.remainingUseProperty().get() : attackBasic.remainingUseProperty().get();
    }

    /**
     * Checks if the Pokemon has a specific effect applied.
     *
     * @param effectType the type of effect to check for
     * @return true if the Pokemon has the specified effect, false otherwise
     */
    public boolean hasEffect(Effect.EffectType effectType) {
        return hasAnyEffect() &&
                currentEffect.getEffectType() == effectType;
    }

    /**
     * Checks if the Pokemon has any effect applied.
     *
     * @return true if the Pokemon has any effect, false otherwise
     */
    public boolean hasAnyEffect() {
        return currentEffect != null;
    }

    /**
     * Makes the Pokemon attack another Pokemon.
     *
     * @param special boolean indicating whether to use special attack
     * @param defender the Pokemon being attacked
     * @return the result of the attack, including damage and any applied effects
     * @throws ModelException if the Pokemon or defender is defeated
     */
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
        if (attack.hasEffect(special) && damage > 0) {
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

    /**
     * Handles the paralyze effect, which can prevent the attack from hitting.
     *
     * @param chance the random chance to trigger the effect
     * @return true if the paralyze effect was triggered, false otherwise
     */
    private boolean handleParalyzeEffect(double chance) {
        if (hasEffect(Effect.EffectType.PARALYZE)) {
            boolean triggered = chance < this.paralyze;
            consumeEffect();
            return triggered;
        }
        return false;
    }

    /**
     * Handles the dodge effect, which can cause the defender to avoid the attack.
     *
     * @param defender the defender of the attack
     * @param chance the random chance to trigger the effect
     * @return true if the dodge effect was triggered, false otherwise
     */
    private boolean handleDodgeEffect(Pokemon defender, double chance) {
        if (defender.hasEffect(Effect.EffectType.DODGE)) {
            return chance < defender.dodge;
        }
        return false;
    }

    /**
     * Handles the critical hit effect, which increases the damage of the attack.
     *
     * @param chance the random chance to trigger the effect
     * @return true if the critical hit effect was triggered, false otherwise
     */
    private boolean handleCritEffect(double chance) {
        if (hasEffect(Effect.EffectType.CRIT)) {
            boolean triggered = chance < this.crit;
            consumeEffect();
            return triggered;
        }
        return false;
    }

    /**
     * Handles the burn effect, which causes extra damage over time.
     *
     * @param defender the defender of the attack
     * @return true if the burn effect is active, false otherwise
     */
    private boolean handleBurnEffect(Pokemon defender) {
        return defender.hasEffect(Effect.EffectType.BURN);
    }

    /**
     * Handles the drain effect, which heals the attacker based on damage dealt.
     *
     * @return true if the drain effect was applied, false otherwise
     */
    private boolean handleDrainEffect() {
        if (hasEffect(Effect.EffectType.DRAIN)) {
            heal(drainHeal);
            consumeEffect();
            return true;
        }
        return false;
    }

    /**
     * Takes damage and updates the current health of the Pokemon.
     *
     * @param damage the amount of damage to apply
     */
    void takeDamage(int damage) {
        currentHP = Math.max(0, currentHP - damage);
        if (currentHP == 0) {
            defeated.set(true);
        }
    }

    /**
     * Checks if the Pokemon is defeated (health is 0).
     *
     * @return true if the Pokemon is defeated, false otherwise
     */
    public boolean isDefeated() {
        return defeated.get();
    }

    /**
     * Heals the Pokemon by a certain amount, ensuring it doesn't exceed its maximum HP.
     *
     * @param amount the amount of health to heal
     */
    public void heal(int amount) {
        if (!isDefeated()) {
            currentHP = Math.min(getMaxHP(), amount + currentHP);
            defeated.set(false);
        }
    }

    /**
     * Sets the current effect applied to the Pokemon.
     *
     * @param effect the effect to apply
     */
    public void setCurrentEffect(Effect effect) {
        if (applyEffect(effect)) {
            this.currentEffect = effect;
        }
    }

    /**
     * Applies the given effect to the Pokemon for one turn.
     *
     * @param effect the effect to apply
     * @return true if the effect was applied successfully, false otherwise
     */
    public boolean applyEffect(Effect effect) {
        if (effect != null && !isDefeated()) {
            return effect.applyTurn(this);
        }
        return false;
    }

    /**
     * Consumes the current effect, resetting related properties.
     */
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

    /**
     * Gets the name of the Pokemon.
     *
     * @return the name of the Pokemon
     */
    public String getName() {
        return card.getName();
    }

    /**
     * Gets the current health points of the Pokemon.
     *
     * @return the current HP of the Pokemon
     */
    public int getCurrentHP() {
        return currentHP;
    }

    /**
     * Gets the maximum health points of the Pokemon.
     *
     * @return the maximum HP of the Pokemon
     */
    public int getMaxHP() {
        return card.getMaxHP();
    }

    /**
     * Retrieves the URL of the Pokémon's image.
     *
     * @return the URL of the image associated with this Pokémon
     */
    public String getImageUrl() {
        return card.getImageURL();
    }

    /**
     * Retrieves the dodge probability of the Pokemon.
     *
     * @return the dodge probability as a double value, which represents the likelihood of the Pokemon dodging an attack
     */
    public double getDodge() {
        return dodge;
    }

    /**
     * Retrieves the paralyze probability of the Pokemon.
     *
     * @return the paralyze probability as a double value, which represents the likelihood of the Pokemon becoming paralyzed
     */
    public double getParalyze() {
        return paralyze;
    }

    /**
     * Retrieves the critical hit probability of the Pokemon.
     *
     * @return the critical hit probability as a double value, representing the likelihood of a critical hit occurring
     */
    public double getCrit() {
        return crit;
    }

    /**
     * Retrieves the burn damage value currently associated with the Pokémon.
     *
     * @return the burn damage value as an integer
     */
    public int getBurnDamage() {
        return burnDamage;
    }

    /**
     * Retrieves the drain heal value associated with the Pokémon.
     *
     * @return the drain heal value as an integer
     */
    public int getDrainHeal() {
        return drainHeal;
    }

    /**
     * Sets the dodge probability for the Pokemon.
     *
     * @param dodge the dodge probability as a double value, representing the likelihood
     *              of the Pokemon dodging an attack
     */
    public void setDodge(double dodge) {
        this.dodge = dodge;
    }

    /**
     * Sets the paralyze probability for the Pokemon.
     *
     * @param paralyze the paralyze probability as a double value, representing the likelihood
     *                 of the Pokemon becoming paralyzed
     */
    public void setParalyze(double paralyze) {
        this.paralyze = paralyze;
    }

    /**
     * Sets the critical hit probability for the Pokemon.
     *
     * @param crit the critical hit probability as a double value, representing the likelihood
     *             of a critical hit occurring
     */
    public void setCrit(double crit) {
        this.crit = crit;
    }

    /**
     * Sets the burn damage value for the Pokemon.
     *
     * @param burnDamage the burn damage value to set, representing the damage inflicted over time due to a burn effect
     */
    public void setBurnDamage(int burnDamage) {
        this.burnDamage = burnDamage;
    }

    /**
     * Sets the drain heal value for the Pokemon.
     *
     * @param drainHeal the drain heal value to set, representing the amount of health restored
     *                  when the Pokemon benefits from a drain effect
     */
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
