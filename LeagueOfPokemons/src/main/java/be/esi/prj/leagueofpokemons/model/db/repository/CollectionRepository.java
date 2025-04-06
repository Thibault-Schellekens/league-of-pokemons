package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Collection;
import be.esi.prj.leagueofpokemons.model.db.dao.CollectionDao;
import be.esi.prj.leagueofpokemons.util.GameManager;

import java.sql.SQLOutput;
import java.util.*;

public class CollectionRepository implements Repository<Integer, Collection> {
    private final CollectionDao collectionDao;
    private static Set<Card> currentCache = null;

    public CollectionRepository() {
        this.collectionDao = new CollectionDao();
        this.currentCache = new HashSet<>();
    }


    public void save(int colID){
        collectionDao.save(colID, currentCache);
    }

    public Set<Card> loadBaseSet(){
        return collectionDao.loadBaseSet();
    }

    public static void saveInCache(Card card){
        System.out.println("Saving "+ card.getName() + " in cache");
        if (currentCache.contains(card)){
            System.out.println("This card already exists in the collection");
        } else {
            currentCache.add(card);
            System.out.println("Card : " + card.getName() + " was added successfully");
        }
    }

    public Set<Card> getCache(){
        return currentCache;
    }
    @Override
    public Optional<Collection> findById(Integer id) {
        return Optional.ofNullable(collectionDao.findById(id).orElse(null));
    }



    //we dont really need this
    @Override
    public void save(Collection collection) {
        collectionDao.save(collection);
    }

    //we dont really need this
    @Override
    public Set<Collection> findAll() {
        return Collections.emptySet();
    }

}
