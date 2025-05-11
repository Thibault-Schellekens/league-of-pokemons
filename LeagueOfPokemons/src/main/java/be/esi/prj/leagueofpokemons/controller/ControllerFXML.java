package be.esi.prj.leagueofpokemons.controller;

/**
 * Interface for controllers that manage FXML views in the League of Pokemons game.
 * <p>
 * This interface ensures that controllers implementing it provide an initialization method
 * for setting up the necessary state and components when the view is loaded.
 * </p>
 */
public interface ControllerFXML {

    /**
     * Initializes the controller by setting up the necessary components and state.
     * <p>
     * This method is called to prepare the controller for interaction with the associated FXML view.
     * </p>
     */
    void init();
}
