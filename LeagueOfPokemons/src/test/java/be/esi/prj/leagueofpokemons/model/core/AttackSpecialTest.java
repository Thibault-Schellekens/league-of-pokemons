package be.esi.prj.leagueofpokemons.model.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttackSpecialTest {

    @Test
    void testDamageEffective() {
        Type attackType = Type.FIRE;
        Type effectiveType = Type.GRASS;
        Attack attack = new AttackSpecial(Tier.TIER_I, attackType);

        int expectedDamage = 30 * 2;
        int actualDamage = attack.calculateDamage(effectiveType);

        System.out.println("Actual: " + actualDamage);
        System.out.println("Expected: " + expectedDamage);

        assertEquals(expectedDamage, actualDamage);
    }

    @Test
    void testDamageWeak() {
        Type attackType = Type.FIRE;
        Type weakType = Type.WATER;

        Attack attack = new AttackSpecial(Tier.TIER_I, attackType);

        int expectedDamage = 30 / 2;
        int actualDamage = attack.calculateDamage(weakType);

        System.out.println("Actual: " + actualDamage);
        System.out.println("Expected: " + expectedDamage);
        assertEquals(expectedDamage, actualDamage);
    }

    @Test
    void testDamageNormal() {
        Type attackType = Type.FIRE;
        Type normalType = Type.FIRE;

        Attack attack = new AttackSpecial(Tier.TIER_I, attackType);

        int expectedDamage = 30;
        int actualDamage = attack.calculateDamage(normalType);

        System.out.println("Actual: " + actualDamage);
        System.out.println("Expected: " + expectedDamage);
        assertEquals(expectedDamage, actualDamage);
    }

    @Test
    void testCannotAttackNoRemainingUse() {

    }

    @Test
    void testApplyEffectEffective() {

    }

    @Test
    void testApplyEffectWeak() {}

}