package be.esi.prj.leagueofpokemons.model.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PokeApiService {
    private final ApiClient apiClient;
    private final String baseURL = "https://pokeapi.co/api/v2/pokemon/";
    private final ObjectMapper mapper;

    public PokeApiService() {
        this.apiClient = new ApiClient();
        mapper = new ObjectMapper();
    }

    public String getPokemonType(String pokemonName) {
        String pokemonNameURL = baseURL + pokemonName;
        String response = apiClient.get(pokemonNameURL).orElse(null);

        if (response == null) {
            throw new RuntimeException(pokemonName + " not found");
        }

        return extractPokemonType(response);
    }

    private String extractPokemonType(String jsonResponse) {
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            // todo: throw some real exception
            throw new RuntimeException(e);
        }
        JsonNode typesNode = rootNode.get("types");
        if (typesNode == null) {
            throw new RuntimeException(jsonResponse + " not found");
        }

        JsonNode firstTypeNode = typesNode.get(0).get("type").get("name");
        return firstTypeNode.asText();
    }

    public static void main(String[] args) {
        PokeApiService apiService = new PokeApiService();
        String firstType = apiService.getPokemonType("bulbasaur");
        System.out.println(firstType);
    }
}
