package be.esi.prj.leagueofpokemons.model.core;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Objects;

public class Pokemon {
    private Card card;
    private int currentHP;
    private Attack basicAttack;
    private Attack specialAttack;
    private Effect currentEffect;

    private BooleanProperty defeated;

    public Pokemon(Card card) {
        this.card = card;
        this.currentHP = card.getMaxHP();
        this.basicAttack = new Attack(card.getType(), card.getTier(), false);
        this.specialAttack = new Attack(card.getType(), card.getTier(), true);
        this.currentEffect = null;
        this.defeated = new SimpleBooleanProperty(false);
    }

    public Type getType() {
        return card.getType();
    }

    public BooleanProperty defeatedProperty() {
        return defeated;
    }

    public AttackResult attack(boolean special, Pokemon defender) {
        Attack attack = special ? specialAttack : basicAttack;

        int damage = attack.calculateDamage(defender.getType());
        defender.takeDamage(damage);

        if (attack.hasEffect(special, defender.getType())) {
            attack.applyEffect(defender);
        }

        System.out.println(defender.currentHP);
        return new AttackResult(damage);
    }

    public void takeDamage(int damage) {
        currentHP = Math.max(0, currentHP - damage);
        if (currentHP <= 0) {
            defeated.set(true);
        }
    }

    public boolean isDefeated() {
        return currentHP <= 0;
    }

    // Might replace int amount by Potion
    public void heal(int amount) {
        currentHP = Math.min(currentHP, amount + currentHP);
        defeated.set(false);
    }

    public void setCurrentEffect(Effect currentEffect) {
        this.currentEffect = currentEffect;
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


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return currentHP == pokemon.currentHP && Objects.equals(card, pokemon.card) && Objects.equals(basicAttack, pokemon.basicAttack) && Objects.equals(specialAttack, pokemon.specialAttack) && Objects.equals(currentEffect, pokemon.currentEffect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, currentHP, basicAttack, specialAttack, currentEffect);
    }
}
