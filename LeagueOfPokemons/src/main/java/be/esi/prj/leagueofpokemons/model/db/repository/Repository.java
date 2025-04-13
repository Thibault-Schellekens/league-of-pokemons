package be.esi.prj.leagueofpokemons.model.db.repository;


import be.esi.prj.leagueofpokemons.model.core.Game;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Repository<K, T> {
    Optional<T> findById(K id);
    void save(T entity);

    Set<T> findAll();
    void delete(T entity);
}