package be.esi.prj.leagueofpokemons.model.core;

import be.esi.prj.leagueofpokemons.util.random.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EffectTest {

    private Pokemon pokemon;

    @BeforeEach
    void setUp() {
        pokemon = new Pokemon(new Card("test-water", "Water", 100, "url", Type.WATER));
    }

    @Test
    void testDodgeEffectApplyAlwaysSuccess() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);

            Effect effect = new Effect(Type.WATER, Tier.TIER_I);
            effect.applyTurn(pokemon);

            double expectedDodge = effect.getDodgeChance();
            double actualDodge = pokemon.getDodge();

            System.out.println("Actual: " + expectedDodge);
            System.out.println("Expected: " + actualDodge);
            assertEquals(expectedDodge, actualDodge);
        }
    }

    @Test
    void testDodgeEffectApplyAlwaysFail() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);

            Effect effect = new Effect(Type.WATER, Tier.TIER_I);
            effect.applyTurn(pokemon);

            double expectedDodge = 0.0;
            double actualDodge = pokemon.getDodge();


            System.out.println("Actual: " + expectedDodge);
            System.out.println("Expected: " + actualDodge);
            assertEquals(expectedDodge, actualDodge);
        }
    }

    @Test
    void testBurnEffectApplyAlwaysSuccess() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);

            Effect effect = new Effect(Type.FIRE, Tier.TIER_I);
            effect.applyTurn(pokemon);

            int expected = effect.getBurnDamage();
            int actual = pokemon.getBurnDamage();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);

        }
    }

    @Test
    void testBurnEffectApplyAlwaysFail() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);

            Effect effect = new Effect(Type.FIRE, Tier.TIER_I);
            effect.applyTurn(pokemon);

            int expected = 0;
            int actual = pokemon.getBurnDamage();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testDrainEffectApplyAlwaysSuccess() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);

            Effect effect = new Effect(Type.GRASS, Tier.TIER_I);
            effect.applyTurn(pokemon);

            int expected = effect.getDrainHeal();
            int actual = pokemon.getDrainHeal();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);

        }
    }

    @Test
    void testDrainEffectApplyAlwaysFail() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);

            Effect effect = new Effect(Type.GRASS, Tier.TIER_I);
            effect.applyTurn(pokemon);

            int expected = 0;
            int actual = pokemon.getDrainHeal();

            System.out.println("Actual: " + actual);
            System.out.println("Expected: " + expected);
            assertEquals(expected, actual);
        }
    }

    @Test
    void testParalyzeEffectApplyAlwaysSuccess() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);

            Effect effect = new Effect(Type.LIGHTNING, Tier.TIER_I);
            effect.applyTurn(pokemon);

            double expectedParalyze = effect.getParalyzeChance();
            double actualParalyze = pokemon.getParalyze();

            System.out.println("Actual: " + actualParalyze);
            System.out.println("Expected: " + expectedParalyze);
            assertEquals(expectedParalyze, actualParalyze);
        }
    }

    @Test
    void testParalyzeEffectApplyAlwaysFail() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);

            Effect effect = new Effect(Type.LIGHTNING, Tier.TIER_I);
            effect.applyTurn(pokemon);

            double expectedParalyze = 0.0;
            double actualParalyze = pokemon.getParalyze();

            System.out.println("Actual: " + actualParalyze);
            System.out.println("Expected: " + expectedParalyze);
            assertEquals(expectedParalyze, actualParalyze);
        }
    }

    @Test
    void testCritEffectApplyAlwaysSuccess() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            mocked.when(RandomUtil::nextDouble).thenReturn(0.0);

            Effect effect = new Effect(Type.FIGHTING, Tier.TIER_I);
            effect.applyTurn(pokemon);

            double expectedCrit = effect.getCritChance();
            double actualCrit = pokemon.getCrit();

            System.out.println("Actual: " + actualCrit);
            System.out.println("Expected: " + expectedCrit);
            assertEquals(expectedCrit, actualCrit);
        }
    }

    @Test
    void testCritEffectApplyAlwaysFail() {
        try (MockedStatic<RandomUtil> mocked = mockStatic(RandomUtil.class)) {
            mocked.when(RandomUtil::nextDouble).thenReturn(1.0);

            Effect effect = new Effect(Type.FIGHTING, Tier.TIER_I);
            effect.applyTurn(pokemon);

            double expectedCrit = 0.0;
            double actualCrit = pokemon.getCrit();

            System.out.println("Actual: " + actualCrit);
            System.out.println("Expected: " + expectedCrit);
            assertEquals(expectedCrit, actualCrit);
        }
    }

}