package be.esi.prj.leagueofpokemons.model.core;

import be.esi.prj.leagueofpokemons.util.random.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class PokemonTest {

    private Pokemon water;
    private Pokemon fire;
    private Pokemon grass;
    private Pokemon fighting;
    private Pokemon lightning;

    @BeforeEach
    void setUp() {
        water = new Pokemon(new Card("water-test", "Testwater", 100, "url", Type.WATER));
        fire = new Pokemon(new Card("fire-test", "Testfire", 100, "url", Type.FIRE));
        grass = new Pokemon(new Card("grass-test", "Testgrass", 100, "url", Type.GRASS));
        fighting = new Pokemon(new Card("fighting-test", "Testfighting", 100, "url", Type.FIGHTING));
        lightning = new Pokemon(new Card("lightning-test", "Testlightning", 100, "url", Type.LIGHTNING));
    }

    @Test
    void testDefeatingPokemon() {
        Pokemon pokemon = grass;
        boolean expected = true;
        pokemon.takeDamage(pokemon.getMaxHP());
        boolean actual = pokemon.isDefeated();

        assertEquals(expected, actual);
    }

    @Test
    void testDefeatedPokemonCannotAttack() {
        Pokemon pokemon = grass;
        Pokemon defender = fire;
        pokemon.takeDamage(pokemon.getMaxHP());

        assertThrows(ModelException.class, () -> pokemon.attack(true, defender));
    }

    @Test
    void testCannotAttackDefeatedPokemon() {
        Pokemon pokemon = grass;
        Pokemon defender = fire;
        defender.takeDamage(defender.getMaxHP());

        assertThrows(ModelException.class, () -> pokemon.attack(true, defender));
    }

    @Test
    void testHealNotExceedingMaxHP() {
        Pokemon pokemon = grass;
        int expected = pokemon.getMaxHP();
        pokemon.heal(20);
        int actual = pokemon.getCurrentHP();

        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);
        assertEquals(expected, actual);
    }

    @Test
    void testHealCannotHealDefeatedPokemon() {
        Pokemon pokemon = grass;
        int expected = 0;
        pokemon.takeDamage(pokemon.getMaxHP());
        pokemon.heal(20);
        int actual = pokemon.getCurrentHP();

        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);
        assertEquals(expected, actual);
    }


    @Test
    void testDodgeAttackSuccessNoDamageTaken() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {

            Pokemon attacker = water;
            Pokemon defender = fire;

            // Applying dodge effect to attacker
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            // Make dodge chance to 100%
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            int expected = attacker.getCurrentHP();
            defender.attack(false, attacker);
            int actual = attacker.getCurrentHP();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testDodgeAttackSuccessCorrectAttackResult() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {

            Pokemon attacker = water;
            Pokemon defender = fire;

            // Applying dodge effect to attacker
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            // Make dodge chance to 100%
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            AttackResult expected = new AttackResult(0, Effect.EffectType.DODGE, "Attack dodged!");
            AttackResult actual = defender.attack(false, attacker);

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testDodgeAttackFailDamageTaken() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = water;
            Pokemon defender = fire;

            // Applying dodge effect to attacker
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            // Make dodge chance to 0%
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);
            int expected = attacker.getCurrentHP() - 30;
            defender.attack(false, attacker);
            int actual = attacker.getCurrentHP();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testDodgeAttackFailCorrectAttackResult() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = water;
            Pokemon defender = fire;

            // Applying dodge effect to attacker
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            // Make dodge chance to 0%
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);
            AttackResult expected = new AttackResult(30);
            AttackResult actual = defender.attack(false, attacker);

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testCritAttackSuccessDamageTakenMultiplicated() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {

            Pokemon attacker = fighting;
            Pokemon defender = water;

            // Applying crit effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            int expected = (int) (defender.getCurrentHP() - (30 * 1.5));
            attacker.attack(true, defender);
            int actual = defender.getCurrentHP();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testCritAttackSuccessCorrectAttackResult() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {

            Pokemon attacker = fighting;
            Pokemon defender = water;

            // Applying crit effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            AttackResult expected = new AttackResult(45, Effect.EffectType.CRIT, "Crit attack!");
            AttackResult actual = attacker.attack(true, defender);

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }



    //TODO : check if possible to chance the when to return 1.0
//    @Test
//    void testCritAttackFail() {
//        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
//
//            Pokemon attacker = fighting;
//            Pokemon defender = water;
//
//            // Applying crit effect
//            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
//            int expected = (int) (defender.getCurrentHP() - (30 * 1.5));
//            attacker.attack(true, defender);
//            int actual = defender.getCurrentHP();
//
//            System.out.println("Actual: " + actual);
//            System.out.println("Expected: " + expected);
//            assertEquals(expected, actual);
//        }
//    }

    @Test
    void testParalyzeAttackSuccessNoDamageDealt() {
        // Testing if attacker can not attack if paralyzed
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = lightning;
            Pokemon defender = fire;

            // Applying paralyzed effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            // Make paralyzed chance to 100%
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            int expected = attacker.getCurrentHP();
            defender.attack(false, attacker);
            int actual = attacker.getCurrentHP();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testParalyzeAttackSuccessCorrectAttackResult() {
        // Testing if attacker can not attack if paralyzed
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = lightning;
            Pokemon defender = fire;

            // Applying paralyzed effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            // Make paralyzed chance to 100%
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            AttackResult expected = new AttackResult(0, Effect.EffectType.PARALYZE, "Attack failed due to paralysis!");
            AttackResult actual =  defender.attack(false, attacker);

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testParalyzeAttackFailDamageDealt() {
        // Testing if attacker can attack if paralyzed but has higher chance to attack
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = lightning;
            Pokemon defender = fire;

            // Applying paralyzed effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            // Make paralyzed chance to 100%
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);
            int expected = attacker.getCurrentHP() - 30;
            defender.attack(false, attacker);
            int actual = attacker.getCurrentHP();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testParalyzeAttackFailCorrectAttackResult() {
        // Testing if attacker can attack if paralyzed but has higher chance to attack
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = lightning;
            Pokemon defender = fire;

            // Applying paralyzed effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            // Make paralyzed chance to 100%
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);
            AttackResult expected = new AttackResult(30);
            AttackResult actual = defender.attack(false, attacker);

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testFireAttackSuccessBurnDamage() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = fire;
            Pokemon defender = lightning;

            // Applying burn effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            Effect burnEffect = new Effect(Type.FIRE, Tier.TIER_I);
            int expected = defender.getCurrentHP() - burnEffect.getBurnDamage() - 30;
            attacker.attack(true, defender);
            int actual = defender.getCurrentHP();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testFireAttackSuccessCorrectAttackResult() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = fire;
            Pokemon defender = lightning;

            // Applying burn effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            Effect burnEffect = new Effect(Type.FIRE, Tier.TIER_I);
            AttackResult expected = new AttackResult(30 + burnEffect.getBurnDamage(), Effect.EffectType.BURN, "Burn applied!");
            AttackResult actual = attacker.attack(true, defender);

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testFireAttackFailBurnDamage() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = fire;
            Pokemon defender = lightning;

            // Applying burn effect
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);
            int expected = defender.getCurrentHP() - 30;
            attacker.attack(true, defender);
            int actual = defender.getCurrentHP();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testFireAttackFailCorrectAttackResult() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = fire;
            Pokemon defender = lightning;

            // Applying burn effect
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);
            AttackResult expected = new AttackResult(30);
            AttackResult actual = attacker.attack(true, defender);

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testFireAttackBurnKillDefender() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = fire;
            Pokemon defender = lightning;

            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            defender.takeDamage(50);
            boolean expected = true;
            attacker.attack(true, defender);
            boolean actual = defender.isDefeated();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testGrassAttackSuccessHealed() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = grass;
            Pokemon defender = lightning;

            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            Effect drainEffect = new Effect(Type.GRASS, Tier.TIER_I);
            attacker.takeDamage(50);
            int expected = attacker.getCurrentHP() + drainEffect.getDrainHeal();
            attacker.attack(true, defender);
            int actual = attacker.getCurrentHP();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testGrassAttackSuccessCorrectAttackResult() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = grass;
            Pokemon defender = lightning;

            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            AttackResult expected = new AttackResult(30, Effect.EffectType.DRAIN, "Draining!");
            AttackResult actual = attacker.attack(true, defender);

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testGrassAttackFailHealed() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = grass;
            Pokemon defender = lightning;

            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);
            attacker.takeDamage(50);
            int expected = attacker.getCurrentHP();
            attacker.attack(true, defender);
            int actual = attacker.getCurrentHP();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testDodgeEffectGoneAfterOneTurn() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = water;
            Pokemon defender1 = fire;
            Pokemon defender2 = grass;

            // Applying dodge effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender1);
            // Consuming dodge effect
            attacker.attack(false, defender2);

            Effect expected = null;
            Effect actual = attacker.getCurrentEffect();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testDodgeChanceReset() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = water;
            Pokemon defender1 = fire;
            Pokemon defender2 = grass;

            // Applying dodge effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender1);
            // Consuming dodge effect
            attacker.attack(false, defender2);

            double expected = 0.0;
            double actual = attacker.getDodge();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testParalyzeEffectGoneAfterOneTurn() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = lightning;
            Pokemon defender = fire;

            // Applying paralyzed effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);
            // Consuming paralyzed effect
            defender.attack(false, attacker);

            Effect expected = null;
            Effect actual = defender.getCurrentEffect();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testParalyzeChanceReset() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = lightning;
            Pokemon defender = fire;

            // Applying paralyzed effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);
            // Consuming paralyzed effect
            defender.attack(false, attacker);

            double expected = 0.0;
            double actual = defender.getParalyze();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testCritEffectGoneAfterOneTurn() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = fighting;
            Pokemon defender1 = fire;
            Pokemon defender2 = grass;

            // Applying crit effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender1);
            // Consuming crit effect
            attacker.attack(false, defender2);

            Effect expected = null;
            Effect actual = attacker.getCurrentEffect();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testCritChanceReset() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = fighting;
            Pokemon defender1 = fire;
            Pokemon defender2 = grass;

            // Applying crit effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender1);
            // Consuming crit effect
            attacker.attack(false, defender2);

            double expected = 0.0;
            double actual = attacker.getCrit();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testBurnEffectGoneAfterOneTurn() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = fire;
            Pokemon defender = lightning;

            // Applying crit effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            Effect expected = null;
            Effect actual = defender.getCurrentEffect();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testBurnDamageReset() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = fire;
            Pokemon defender = lightning;

            // Applying crit effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            int expected = 0;
            int actual = defender.getBurnDamage();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testDrainEffectGoneAfterOneTurn() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = grass;
            Pokemon defender = lightning;

            // Applying crit effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            Effect expected = null;
            Effect actual = attacker.getCurrentEffect();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testDrainHealReset() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = grass;
            Pokemon defender = lightning;

            // Applying crit effect
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            int expected = 0;
            int actual = attacker.getDrainHeal();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }


    @Test
    void testFailToApplyEffect() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = water;
            Pokemon defender = fire;

            // Make the effect fails to apply
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);
            Effect expected = null;
            attacker.attack(true, defender);
            Effect actual = attacker.getCurrentEffect();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testNotReplacingEffect() {
        // Test if pokemon paralyzed and tries to drain
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            Pokemon attacker = lightning;
            Pokemon defender = fighting;

            // Applying paralyzed effect to defender
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            attacker.attack(true, defender);

            // Defender is paralyzed, causing him to fail his attack
            // He shouldn't have the crit effect on him

            // Make the defender fails his attack
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);
            AttackResult expected = new AttackResult(0, Effect.EffectType.PARALYZE, "Attack failed due to paralysis!");
            AttackResult actual = defender.attack(true, attacker);

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }



    }
}