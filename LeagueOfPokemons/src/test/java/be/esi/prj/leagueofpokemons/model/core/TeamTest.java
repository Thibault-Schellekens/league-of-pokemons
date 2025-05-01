package be.esi.prj.leagueofpokemons.model.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private Team team;
    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private Pokemon pokemon3;

    @BeforeEach
    void setUp() {
        team = new Team();
        pokemon1 = new Pokemon(new Card("1", "1", 100, "url", Type.FIRE));
        pokemon2 = new Pokemon(new Card("2", "2", 100, "url", Type.FIRE));
        pokemon3 = new Pokemon(new Card("3", "3", 100, "url", Type.FIRE));
    }

    @Test
    void testGetPokemonEmptyTeam() {
        assertNull(team.getPokemon(0));
    }

    @Test
    void testPokemonGetOutOfBounds() {
        team.addPokemon(pokemon1);
        assertNull(team.getPokemon(1));
    }

    @Test
    void testCanNotAddPokemonToFullTeam() {
        team.addPokemon(pokemon1);
        team.addPokemon(pokemon2);
        team.addPokemon(pokemon3);

        Pokemon pokemon = new Pokemon(new Card("", "", 100, "url", Type.FIRE));

        assertThrows(ModelException.class, () -> team.addPokemon(pokemon));
    }

    @Test
    void testCanNotAddSamePokemon() {
        team.addPokemon(pokemon1);

        assertThrows(ModelException.class, () -> team.addPokemon(pokemon1));
    }

    @Test
    void testCanNotAddPokemonWithGreaterTier() {
        Pokemon pokemon = new Pokemon(new Card("0", "0", 1000, "url", Type.FIRE));

        System.out.println(pokemon.getTier());

        assertThrows(ModelException.class, () -> team.addPokemon(pokemon));
    }

    @Test
    void testAddPokemon() {
        team.addPokemon(pokemon1);

        Pokemon expected = pokemon1;
        Pokemon actual = team.getPokemon(0);

        assertEquals(expected, actual);
    }

    @Test
    void testDefeatedNoPokemon () {
        assertTrue(team.isDefeated());
    }

    @Test
    void testDefeatedAllPokemonDead() {
        team.addPokemon(pokemon1);
        team.addPokemon(pokemon2);
        team.addPokemon(pokemon3);

        pokemon1.takeDamage(1000);
        pokemon2.takeDamage(1000);
        pokemon3.takeDamage(1000);

        assertTrue(team.isDefeated());
    }

    @Test
    void testDefeatedOnePokemonAlive() {
        team.addPokemon(pokemon1);
        team.addPokemon(pokemon2);
        team.addPokemon(pokemon3);

        pokemon1.takeDamage(1000);
        pokemon3.takeDamage(1000);

        assertFalse(team.isDefeated());
    }

}