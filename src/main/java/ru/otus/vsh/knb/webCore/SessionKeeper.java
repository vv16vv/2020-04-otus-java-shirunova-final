package ru.otus.vsh.knb.webCore;

import ru.otus.vsh.knb.dbCore.model.Person;

import java.util.Optional;
import java.util.Set;

public interface SessionKeeper {
    void add(String sessionId, Person person);

    Optional<Person> get(String sessionId);

    Optional<Person> remove(String sessionId);

    Set<String> sessions();

    String get(Person person);

}
