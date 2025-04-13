package be.esi.prj.leagueofpokemons.model.core;

public class Pokemon {
    private Card card;
    private int currentHP;
    private Attack basicAttack;
    private Attack specialAttack;
    private Effect currentEffect;

    public Pokemon(Card card) {
        this.card = card;
        this.currentHP = card.getMaxHP();
        this.basicAttack = new Attack(card.getType(), card.getTier(), false);
        this.specialAttack = new Attack(card.getType(), card.getTier(), true);
        this.currentEffect = null;
    }

    public Type getType() {
        return card.getType();
    }

    public AttackResult attack(boolean special, Pokemon defender) {
        Attack attack = special ? specialAttack : basicAttack;

        int damage = attack.calculateDamage(defender.getType());
        defender.takeDamage(damage);

        if (attack.hasEffect(special, defender.getType())) {
            attack.applyEffect(defender);
        }


        return new AttackResult();
    }

    public void takeDamage(int damage) {
        currentHP = Math.max(0, currentHP - damage);
    }

    public boolean isDefeated() {
        return currentHP <= 0;
    }

    // Might replace int amount by Potion
    public void heal(int amount) {
        currentHP = Math.min(currentHP, amount + currentHP);
    }

    public void setCurrentEffect(Effect currentEffect) {
        this.currentEffect = currentEffect;
    }
}
