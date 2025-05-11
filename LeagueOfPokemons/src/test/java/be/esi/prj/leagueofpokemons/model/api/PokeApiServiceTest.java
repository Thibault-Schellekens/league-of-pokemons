package be.esi.prj.leagueofpokemons.model.api;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Type;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PokeApiServiceTest {
    private static PokeApiService pokeApiService;

    @BeforeAll
    static void setUp() {
        pokeApiService = new PokeApiService();
    }

    @Test
    void testInvalidLocalId_negative() {
        int invalidLocalId = -1;
        String pokemonName = "Scorbunny";

        Optional<Card> expectedCard = Optional.empty();

        Optional<Card> actualCard = pokeApiService.getPokemonCard(invalidLocalId, pokemonName);

        assertEquals(expectedCard, actualCard);
    }

    @Test
    void testInvalidLocalId() {
        int invalidLocalId = 500;
        String pokemonName = "Scorbunny";

        Optional<Card> expectedCard = Optional.empty();

        Optional<Card> actualCard = pokeApiService.getPokemonCard(invalidLocalId, pokemonName);

        assertEquals(expectedCard, actualCard);
    }

    @Test
    void testInvalidSet() {
        int localId = 1;
        String pokemonNameFromOtherSet = "Alakazam";

        Optional<Card> expectedCard = Optional.empty();

        Optional<Card> actualCard = pokeApiService.getPokemonCard(localId, pokemonNameFromOtherSet);

        assertEquals(expectedCard, actualCard);
    }

    @Test
    void testInvalidType() {
        // PSYCHIC Pokémon
        int localId = 87;
        String pokemonName = "Munna";

        Optional<Card> expectedCard = Optional.empty();
        Optional<Card> actualCard = pokeApiService.getPokemonCard(localId, pokemonName);

        assertEquals(expectedCard, actualCard);
    }

    @Test
    void testValidCard() {
        int localId = 1;
        String pokemonName = "Exeggcute";

        Optional<Card> expectedCard = Optional.of(new Card("swsh9-001", pokemonName, 50, "https://assets.tcgdex.net/en/swsh/swsh9/001/high.png", Type.GRASS));

        Optional<Card> actualCard = pokeApiService.getPokemonCard(localId, pokemonName);

        assertEquals(expectedCard, actualCard);
    }

}