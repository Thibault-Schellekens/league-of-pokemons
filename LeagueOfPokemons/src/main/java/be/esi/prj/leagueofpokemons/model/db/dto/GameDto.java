package be.esi.prj.leagueofpokemons.model.db.dto;

public record GameDto(int gameID, int playerID, String slot1ID, String slot2ID , String slot3ID, int collectionID, int currentStage) {
}
