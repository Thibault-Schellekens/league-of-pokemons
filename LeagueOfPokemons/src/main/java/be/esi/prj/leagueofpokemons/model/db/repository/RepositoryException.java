package be.esi.prj.leagueofpokemons.model.db.repository;

public class RepositoryException extends RuntimeException{
    public RepositoryException(String message, Throwable cause){
        super(message,cause);
    }
}
