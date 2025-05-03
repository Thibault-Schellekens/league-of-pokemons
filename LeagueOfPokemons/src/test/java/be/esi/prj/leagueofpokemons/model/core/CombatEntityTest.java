package be.esi.prj.leagueofpokemons.model.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CombatEntityTest {

    private CombatEntity combatEntity;

    @BeforeEach
    void setUp() {
        combatEntity = new Player();
    }

    @Test
    void testCannotSwapToNull() {
        assertThrows(ModelException.class, () -> combatEntity.swap(null));
    }

    @Test
    void testCannotSwapToSamePokemon() {
        assertThrows(ModelException.class, () -> combatEntity.swap(combatEntity.getActivePokemon()));
    }

    @Test
    void testCannotSwapFromPokemonNotInTeam() {
        Pokemon pokemonNotInTeam = new Pokemon(new Card("test", "test", 100, "url", Type.FIRE));

        assertThrows(ModelException.class, () -> combatEntity.swap(pokemonNotInTeam));
    }

    @Test
    void testSwapSuccessActivePokemonChanged() {
        Pokemon pokemon = combatEntity.getSlot1Pokemon();

        combatEntity.swap(pokemon);

        Pokemon expected = pokemon;
        Pokemon actual = combatEntity.getActivePokemon();

        assertEquals(expected, actual);
    }

    @Test
    void testSwapSuccessSlot1PokemonChanged() {
        Pokemon pokemon = combatEntity.getActivePokemon();

        combatEntity.swap(combatEntity.getSlot1Pokemon());

        Pokemon expected = pokemon;
        Pokemon actual = combatEntity.getSlot1Pokemon();

        assertEquals(expected, actual);
    }

    @Test
    void testSwapSuccessSlot2PokemonChanged() {
        Pokemon pokemon = combatEntity.getActivePokemon();

        combatEntity.swap(combatEntity.getSlot2Pokemon());

        Pokemon expected = pokemon;
        Pokemon actual = combatEntity.getSlot2Pokemon();

        assertEquals(expected, actual);
    }

    @Test
    void testTeamDefeated() {
        combatEntity.activePokemon.takeDamage(1000);
        combatEntity.getSlot1Pokemon().takeDamage(1000);
        combatEntity.getSlot2Pokemon().takeDamage(1000);

        assertTrue(combatEntity.isDefeated());
    }

}