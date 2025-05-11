package be.esi.prj.leagueofpokemons.model.core;

/**
 * Represents the possible statuses of a battle.
 * <p>
 * The status of a battle can be one of the following:
 * </p>
 * <ul>
 *     <li>{@link #NOT_STARTED} - The battle has not yet started.</li>
 *     <li>{@link #IN_PROGRESS} - The battle is currently ongoing.</li>
 *     <li>{@link #PLAYER_WON} - The player has won the battle.</li>
 *     <li>{@link #OPPONENT_WON} - The opponent has won the battle.</li>
 * </ul>
 */
public enum BattleStatus {
    NOT_STARTED,
    IN_PROGRESS,
    PLAYER_WON,
    OPPONENT_WON
}
