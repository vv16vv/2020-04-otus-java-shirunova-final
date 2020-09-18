package ru.otus.vsh.knb.webCore;

import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.model.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class SessionKeeperImpl implements SessionKeeper {
    private final Map<String, Person> repository = new HashMap<>();

    @Override
    public void add(String sessionId, Person person) {
        repository.putIfAbsent(sessionId, person);
    }

    @Override
    public Optional<Person> get(String sessionId) {
        return Optional.ofNullable(repository.getOrDefault(sessionId, null));
    }

    @Override
    public Optional<Person> remove(String sessionId) {
        if (repository.containsKey(sessionId)) {
            return Optional.ofNullable(repository.remove(sessionId));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Set<String> sessions() {
        return repository.keySet();
    }
}
