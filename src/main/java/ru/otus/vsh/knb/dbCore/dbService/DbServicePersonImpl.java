package ru.otus.vsh.knb.dbCore.dbService;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.vsh.knb.dbCore.dao.PersonDao;
import ru.otus.vsh.knb.dbCore.dbService.api.AbstractDbServiceImpl;
import ru.otus.vsh.knb.dbCore.model.Person;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class DbServicePersonImpl extends AbstractDbServiceImpl<Person> implements DBServicePerson {
    private final PersonDao personDao;

    public DbServicePersonImpl(PersonDao personDao) {
        super(personDao);
        this.personDao = personDao;
    }

    @Override
    public Optional<Person> findByLogin(String login) {
        return executeInSession((sm, loginName) -> {
            var person = personDao.findByLogin(loginName);
            sm.commitSession();
            log.info("found person with login = {}: {}", loginName, person);
            return person;
        }, login);
    }

}
