package be.esi.prj.leagueofpokemons.model.ocr;

/**
 * Represents the result of a card scan operation.
 * This record contains the outcome of the scan, including whether the scan was successful,
 * the name of the Pokémon recognized on the card, and the local ID associated with the scanned card.
 */
public record CardScanResult(
        /**
         * Indicates whether the card scan was successful.
         * A value of {@code true} means the scan was successful, while {@code false} indicates failure.
         */
        boolean successful,

        /**
         * The name of the Pokémon identified on the scanned card.
         * If the scan was unsuccessful, this will be {@code null}.
         */
        String pokemonName,

        /**
         * The local ID associated with the scanned card.
         * This ID is used for identifying and tracking the card in the system.
         */
        int localId
) {
}
