package be.esi.prj.leagueofpokemons.model.api;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Type;
import net.tcgdex.sdk.Extension;
import net.tcgdex.sdk.Quality;
import net.tcgdex.sdk.TCGdex;
import net.tcgdex.sdk.models.Set;

import java.util.Optional;

/**
 * Service for interacting with the TCGdex API to fetch Pokémon cards based on a local ID and Pokémon name.
 * <p>
 * This class utilizes the TCGdex SDK to search for Pokémon cards in the Sword and Shield sets (1-9).
 * It verifies the card details and fetches the appropriate card from the API.
 * </p>
 */
public class PokeApiService {

    private final TCGdex tcgdex;

    /**
     * Constructs a {@link PokeApiService} instance, initializing the TCGdex API client for the English locale.
     */
    public PokeApiService() {
        tcgdex = new TCGdex("en");
    }

    /**
     * Fetches a Pokémon card from the TCGdex API based on the provided local ID and Pokémon name.
     * It iterates through the Sword and Shield sets (1-9) to find a matching card.
     *
     * @param localId The local ID of the Pokémon card to fetch.
     * @param pokemonName The name of the Pokémon to match with the card.
     * @return An {@link Optional} containing the {@link Card} if a matching card is found, or an empty {@link Optional} if no card is found.
     */
    public Optional<Card> getPokemonCard(int localId, String pokemonName) {
        // Iterate through all the swsh (Sword and Shield) sets. (1-9)
        for (int i = 1; i <= 9; i++) {
            String swshSet = "swsh" + i;
            if (!isLocalIdValid(swshSet, localId)) continue;

            net.tcgdex.sdk.models.Card card = tcgdex.fetchCard(swshSet, String.valueOf(localId));
            if (card == null) continue;
            String typeName = card.getTypes().getFirst().toUpperCase();
            if (Type.isValidTypeName(typeName) && pokemonName.equals(card.getName())) {
                return Optional.of(new Card(card.getId(),
                        pokemonName,
                        card.getHp(),
                        card.getImageUrl(Quality.HIGH, Extension.PNG).toLowerCase(),
                        Type.valueOf(card.getTypes().getFirst().toUpperCase())));
            }
        }
        return Optional.empty();
    }

    /**
     * Validates if the provided local ID is valid for the given Sword and Shield set.
     *
     * @param swshSet The name of the Sword and Shield set (e.g., "swsh1").
     * @param localId The local ID of the card to validate.
     * @return {@code true} if the local ID is valid for the given set, {@code false} otherwise.
     */
    private boolean isLocalIdValid(String swshSet, int localId) {
        if (localId < 1) {
            return false;
        }
        Set set = tcgdex.fetchSet(swshSet);
        int totalCards = set.getCardCount().getTotal();
        return totalCards >= localId;
    }

}
