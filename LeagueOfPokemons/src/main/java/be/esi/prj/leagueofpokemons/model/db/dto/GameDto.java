package be.esi.prj.leagueofpokemons.model.db.dto;

import java.time.LocalDateTime;

public record GameDto(int gameID, String name, int playerID, String slot1ID, String slot2ID , String slot3ID, int currentStage, LocalDateTime date) {
}
