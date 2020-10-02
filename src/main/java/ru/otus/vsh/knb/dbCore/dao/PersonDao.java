package ru.otus.vsh.knb.dbCore.dao;

import ru.otus.vsh.knb.dbCore.model.Person;

import java.util.Optional;

public interface PersonDao extends Dao<Person> {
    Optional<Person> findByLogin(String login);
}
