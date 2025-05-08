package be.esi.prj.leagueofpokemons.model.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpponentTest {

    private Opponent opponent;
    private Pokemon water;
    private Pokemon fire;
    private Pokemon grass;
    private Pokemon fighting;
    private Pokemon lightning;

    @BeforeEach
    void setUp() {
        opponent = new Opponent();
        opponent.team = new Team(Tier.TIER_V);
        water = new Pokemon(new Card("water-test", "Testwater", 100, "url", Type.WATER));
        fire = new Pokemon(new Card("fire-test", "Testfire", 100, "url", Type.FIRE));
        grass = new Pokemon(new Card("grass-test", "Testgrass", 100, "url", Type.GRASS));
        fighting = new Pokemon(new Card("fighting-test", "Testfighting", 100, "url", Type.FIGHTING));
        lightning = new Pokemon(new Card("lightning-test", "Testlightning", 100, "url", Type.LIGHTNING));
    }

    @Test
    void testSwapIfActivePokemonDefeated() {
        opponent.team.addPokemon(water);
        opponent.activePokemon = water;
        water.takeDamage(1000);

        ActionType expected = ActionType.SWAP;
        ActionType actual = opponent.think(fire);

        assertEquals(expected, actual);
    }

    @Test
    void testSpecialAttackIfEffective() {
        opponent.team.addPokemon(water);
        opponent.activePokemon = water;

        ActionType expected = ActionType.SPECIAL_ATTACK;
        ActionType actual = opponent.think(fire);

        assertEquals(expected, actual);
    }

    @Test
    void testSpecialAttackIfNeutral() {
        opponent.team.addPokemon(water);
        opponent.activePokemon = water;

        ActionType expected = ActionType.SPECIAL_ATTACK;
        ActionType actual = opponent.think(fighting);

        assertEquals(expected, actual);
    }

    @Test
    void testBasicAttackIfWeak() {
        opponent.team.addPokemon(water);
        opponent.activePokemon = water;

        ActionType expected = ActionType.BASIC_ATTACK;
        ActionType actual = opponent.think(lightning);

        assertEquals(expected, actual);

    }

    @Test
    void testSwapIfWeakAndCanSwapToNeutralOrEffective() {
        opponent.team.addPokemon(water);
        opponent.activePokemon = water;

        opponent.team.addPokemon(fire);

        ActionType expected = ActionType.SWAP;
        ActionType actual = opponent.think(lightning);

        assertEquals(expected, actual);

    }

    @Test
    void testBasicAttackIfWeakAndCannotSwap() {
        opponent.team.addPokemon(water);
        opponent.activePokemon = water;

        opponent.team.addPokemon(fire);
        fire.takeDamage(1000);

        ActionType expected = ActionType.BASIC_ATTACK;
        ActionType actual = opponent.think(lightning);

        assertEquals(expected, actual);
    }

    @Test
    void testNextPokemonEffective() {
        opponent.team.addPokemon(water);
        opponent.activePokemon = water;

        opponent.team.addPokemon(fighting);

        Pokemon expected = fighting;
        Pokemon actual = opponent.getNextPokemon(lightning);

        assertEquals(expected, actual);
    }

}