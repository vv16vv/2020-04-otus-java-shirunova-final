package ru.otus.vsh.knb.dbCore.dbService;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.vsh.knb.dbCore.dbService.api.DBService;
import ru.otus.vsh.knb.dbCore.model.Person;

import java.util.Optional;

public interface DBServicePerson extends DBService<Person>, UserDetailsService {
    Optional<Person> findByLogin(String login);
}
