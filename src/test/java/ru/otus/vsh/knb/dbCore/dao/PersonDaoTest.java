package ru.otus.vsh.knb.dbCore.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import ru.otus.vsh.knb.dbCore.model.Person;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class PersonDaoTest extends BaseDaoTest {

    @Test
    void searchUnexistingPerson() {
        sessionManagerHibernate.beginSession();
        Optional<Person> person = personDaoHibernate.findById(100500);
        sessionManagerHibernate.commitSession();

        assertThat(person).isEmpty();
    }

    @Test
    void createNewPersonInsert() {
        val person = getNewPerson1();

        sessionManagerHibernate.beginSession();
        val newPersonId = personDaoHibernate.insert(person);
        sessionManagerHibernate.commitSession();

        assertThat(newPersonId).isGreaterThan(0L);
        assertThat(person.getAccount().getId()).isGreaterThan(0L);
    }

    @Test
    void searchExistingPersonById() {
        val person = getNewPerson1();

        sessionManagerHibernate.beginSession();
        val newPersonId = personDaoHibernate.insert(person);
        sessionManagerHibernate.commitSession();
        log.info("Person is created with id #{} and account id {}", newPersonId, person.getAccount().getId());

        sessionManagerHibernate.beginSession();
        val foundPerson = personDaoHibernate.findById(newPersonId);
        val foundAccount = accountDaoHibernate.findById(person.getAccount().getId());
        sessionManagerHibernate.commitSession();
        log.info("Found person is {}", foundPerson);
        log.info("Found account is {}", foundAccount);

        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get()).isEqualTo(person);
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get()).isEqualTo(person.getAccount());
    }

    @Test
    void searchExistingPersonByLogin() {
        val person = getNewPerson1();

        sessionManagerHibernate.beginSession();
        val newPersonId = personDaoHibernate.insert(person);
        sessionManagerHibernate.commitSession();
        log.info("Person is created with id #{} and account id {}", newPersonId, person.getAccount().getId());

        sessionManagerHibernate.beginSession();
        val foundPerson = personDaoHibernate.findByLogin(person.getLogin());
        sessionManagerHibernate.commitSession();
        log.info("Found person is {}", foundPerson);

        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get()).isEqualTo(person);
    }

    @Test
    void editPersonUpdate() {
        val person = getNewPerson1();

        sessionManagerHibernate.beginSession();
        personDaoHibernate.insert(person);
        sessionManagerHibernate.commitSession();

        person.getAccount().increase(600);
        sessionManagerHibernate.beginSession();
        personDaoHibernate.update(person);
        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();
        val editedPerson = personDaoHibernate.findById(person.getId());
        sessionManagerHibernate.commitSession();

        assertThat(editedPerson).isPresent();
        assertThat(editedPerson.get().getAccount().getSum()).isEqualTo(1100);
        assertThat(editedPerson.get().getAccount().getSum()).isEqualTo(person.getAccount().getSum());
    }

}
