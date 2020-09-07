package ru.otus.vsh.knb.dbCore;

import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dbService.DBServicePerson;
import ru.otus.vsh.knb.dbCore.model.Account;
import ru.otus.vsh.knb.dbCore.model.Person;

@Component
public class DataInitializerImpl implements DataInitializer {
    private final DBServicePerson dbServicePerson;
    private final PasswordEncoder passwordEncoder;

    public DataInitializerImpl(DBServicePerson dbServicePerson, PasswordEncoder passwordEncoder) {
        this.dbServicePerson = dbServicePerson;
        this.passwordEncoder = passwordEncoder;

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
                .password(passwordEncoder.encode("12345"))
                .account(createDefaultAccount())
                .get();
        val player2 = Person.builder()
                .login("sevantius")
                .name("Всеволод")
                .password(passwordEncoder.encode("11111"))
                .account(createDefaultAccount())
                .get();
        val player3 = Person.builder()
                .login("koshir")
                .name("Константин")
                .password(passwordEncoder.encode("24680"))
                .account(createDefaultAccount())
                .get();

        dbServicePerson.saveObject(player1);
        dbServicePerson.saveObject(player2);
        dbServicePerson.saveObject(player3);

    }
}
