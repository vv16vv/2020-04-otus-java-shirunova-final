package ru.otus.vsh.knb.dbCore.dbService.api;

import ru.otus.vsh.knb.dbCore.model.Model;

import java.util.List;
import java.util.Optional;

public interface DBService<T extends Model> {

    Long saveObject(T t);

    Long newObject(T t);

    void editObject(T t);

    Optional<T> getObject(Long id);

    List<T> findAll();

}
