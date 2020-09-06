package ru.otus.vsh.knb.webCore.services;

import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dbService.DBServicePerson;
import ru.otus.vsh.knb.dbCore.model.Account;
import ru.otus.vsh.knb.dbCore.model.Person;

@Component
public class DataInitializerImpl implements DataInitializer {
    private final DBServicePerson dbServicePerson;

    public DataInitializerImpl(DBServicePerson dbServicePerson) {
        this.dbServicePerson = dbServicePerson;

        createInitialData();
    }

    private Account createDefaultAccount(){
        return Account.builder()
                .id(0L)
                .sum(500L)
                .get();
    }

    @Override
    public void createInitialData() {
        val player1 = Person.builder()
                .login("vitkus")
                .name("Виктория")
                .password("12345")
                .account(createDefaultAccount())
                .get();
        val player2 = Person.builder()
                .login("sevantius")
                .name("Всеволод")
                .password("11111")
                .account(createDefaultAccount())
                .get();
        val player3 = Person.builder()
                .login("koshir")
                .name("Константин")
                .password("24680")
                .account(createDefaultAccount())
                .get();

        dbServicePerson.saveObject(player1);
        dbServicePerson.saveObject(player2);
        dbServicePerson.saveObject(player3);

    }
}
