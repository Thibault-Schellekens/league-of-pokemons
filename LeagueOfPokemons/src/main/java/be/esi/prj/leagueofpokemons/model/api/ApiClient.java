package be.esi.prj.leagueofpokemons.model.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class ApiClient {
    private final HttpClient httpClient;

    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public Optional<String> get(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();


        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            // Success
            if (response.statusCode() == 200) {
                return Optional.of(response.body());
            }
        } catch (IOException | InterruptedException _) {}

        return Optional.empty();
    }
}
