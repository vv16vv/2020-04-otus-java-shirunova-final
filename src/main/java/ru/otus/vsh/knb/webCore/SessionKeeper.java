package ru.otus.vsh.knb.webCore;

import ru.otus.vsh.knb.dbCore.model.Person;

import java.util.Optional;

public interface SessionKeeper {
    void add(String sessionId, Person person);

    Optional<Person> get(String sessionId);

    Optional<Person> remove(String sessionId);

}
