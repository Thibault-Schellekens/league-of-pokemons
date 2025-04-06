package be.esi.prj.leagueofpokemons.model.db.dao;

import be.esi.prj.leagueofpokemons.model.core.Card;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Dao<K, T> {
    Optional<T> findById(K id);  // Return Optional to handle missing results
    void save(T entity);         // Save or update the entity
    void delete(K id);           // Delete an entity by ID
}
