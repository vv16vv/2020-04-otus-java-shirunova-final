package ru.otus.vsh.knb.dbCore.dao;

import ru.otus.vsh.knb.dbCore.model.Model;
import ru.otus.vsh.knb.dbCore.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface Dao<T extends Model> {
    Optional<T> findById(long id);

    List<T> findAll();

    long insert(T t);

    void update(T t);

    void insertOrUpdate(T t);
    
    SessionManager getSessionManager();
}
