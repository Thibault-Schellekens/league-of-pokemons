package be.esi.prj.leagueofpokemons.model.db.repository;


import java.util.List;
import java.util.Optional;

public interface Repository<K, T> {
    Optional<T> findById(K id);
    K save(T entity);

    List<T> findAll();
    void delete(T entity);
}