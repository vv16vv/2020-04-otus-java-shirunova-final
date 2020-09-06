package ru.otus.vsh.knb.hibernate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dao.PersonDao;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.hibernate.sessionmanager.SessionManagerHibernate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class PersonDaoHibernate extends AbstractDaoHibernate<Person> implements PersonDao {

    public PersonDaoHibernate(SessionManagerHibernate sessionManager) {
        super(sessionManager, Person.class);
    }

    @Override
    public Optional<Person> findByLogin(String login) {
        try {
            var entityManager = sessionManager.getEntityManager();
            List<Person> persons = entityManager
                    .createNamedQuery(Person.GET_PERSON_BY_LOGIN, Person.class)
                    .setParameter("login", login)
                    .getResultList();
            if (persons.isEmpty()) return Optional.empty();
            else return Optional.of(persons.get(0));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

}
