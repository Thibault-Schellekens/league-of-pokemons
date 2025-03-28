package be.esi.prj.leagueofpokemons.model.api;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Tier;
import be.esi.prj.leagueofpokemons.model.core.Type;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tcgdex.sdk.Extension;
import net.tcgdex.sdk.Quality;
import net.tcgdex.sdk.TCGdex;
import net.tcgdex.sdk.models.Set;

import java.util.Optional;

public class PokeApiService {

    private final TCGdex tcgdex;

    public PokeApiService() {
        tcgdex = new TCGdex("en");
    }

    public Optional<Card> getPokemonCard(int localId, String pokemonName) {
        // Iterate through all the swsh (Sword and Shield) sets. (1-9)
        for (int i = 1; i <= 9; i++) {
            String swshSet = "swsh" + i;
            if (!isLocalIdValid(swshSet, localId)) continue;

            net.tcgdex.sdk.models.Card card = tcgdex.fetchCard(swshSet, String.valueOf(localId));
            if (card != null && pokemonName.equals(card.getName())) {
                return Optional.of(new Card(card.getId(),
                        pokemonName,
                        card.getHp(),
                        card.getImageUrl(Quality.HIGH, Extension.PNG).toLowerCase(),
                        Type.valueOf(card.getTypes().getFirst().toUpperCase())));
            }
        }
        return Optional.empty();
    }

    private boolean isLocalIdValid(String swshSet, int localId) {
        if (localId < 1) {
            return false;
        }
        Set set = tcgdex.fetchSet(swshSet);
        int totalCards = set.getCardCount().getTotal();
        return totalCards >= localId;
    }

}
