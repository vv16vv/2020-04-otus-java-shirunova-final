package ru.otus.vsh.knb.webCore;

import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.model.Person;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class SessionKeeperImpl implements SessionKeeper {
    private final Map<String, Person> repository = new ConcurrentHashMap<>();

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

    @Override
    public synchronized String get(Person person) {
        val entries = repository.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getId() == person.getId())
                .collect(Collectors.toList());
        if (entries.isEmpty()) return "";
        else return entries.get(0).getKey();
    }

    @Override
    public synchronized void update(Person person) {
        repository.put(get(person), person);
    }

}
