package be.esi.prj.leagueofpokemons.util;

/**
 * Enum representing the different scenes in the application.
 * Each constant corresponds to a specific scene view, which can be loaded and displayed by the {@link SceneManager}.
 */
public enum SceneView {
    /**
     * The main menu scene, typically shown when the application starts.
     */
    MAINMENU,

    /**
     * The hub scene, where the player can navigate between different game options.
     */
    HUB,

    /**
     * The battle scene, where the player engages in combat with opponents.
     */
    BATTLE,

    /**
     * The collection scene, where the player can view and manage their collected cards.
     */
    COLLECTION,

    /**
     * The scanner scene, used for scanning cards via OCR (Optical Character Recognition).
     */
    SCANNER
}
