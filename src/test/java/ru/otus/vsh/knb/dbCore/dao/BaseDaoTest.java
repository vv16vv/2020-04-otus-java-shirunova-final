package ru.otus.vsh.knb.dbCore.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.otus.vsh.knb.dbCore.dbService.*;
import ru.otus.vsh.knb.dbCore.model.*;
import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.domain.GameProcessorImpl;
import ru.otus.vsh.knb.dbCore.hibernate.SessionFactoryUtils;
import ru.otus.vsh.knb.dbCore.hibernate.dao.*;
import ru.otus.vsh.knb.dbCore.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BaseDaoTest {
    protected final static int initialItems = 3;
    protected final static int initialTurns = 3;
    protected final static int initialCheats = 1;
    protected final static int changedItems = 10;
    protected final static int changedTurns = 10;
    protected final static int changedCheats = 0;

    private final static String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate-test.cfg.xml";

    protected SessionFactory sessionFactory;
    protected SessionManagerHibernate sessionManagerHibernate;
    protected AccountDaoHibernate accountDaoHibernate;
    protected BetDaoHibernate betDaoHibernate;
    protected GameDaoHibernate gameDaoHibernate;
    protected GameSettingsDaoHibernate gameSettingsDaoHibernate;
    protected PersonDaoHibernate personDaoHibernate;
    protected PersonsInGamesDaoHibernate personsInGamesDaoHibernate;

    protected DBServicePerson dbServicePerson;
    protected DBServiceGame dbServiceGame;
    protected DBServicePersonsInGames dbServicePersonsInGames;
    protected DBServiceBet dbServiceBet;
    protected DBServiceGameSettings dbServiceGameSettings;

    protected GameProcessor gameProcessor;

    @BeforeEach
    public void setUp() {
        sessionFactory = SessionFactoryUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE,
                Bet.class,
                PersonsInGames.class,
                Game.class,
                GameSettings.class,
                Person.class,
                Account.class);
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        betDaoHibernate = new BetDaoHibernate(sessionManagerHibernate);
        personsInGamesDaoHibernate = new PersonsInGamesDaoHibernate(sessionManagerHibernate);
        gameDaoHibernate = new GameDaoHibernate(sessionManagerHibernate);
        gameSettingsDaoHibernate = new GameSettingsDaoHibernate(sessionManagerHibernate);
        personDaoHibernate = new PersonDaoHibernate(sessionManagerHibernate);
        accountDaoHibernate = new AccountDaoHibernate(sessionManagerHibernate);

        dbServicePerson = new DbServicePersonImpl(personDaoHibernate);
        dbServiceGame = new DbServiceGameImpl(gameDaoHibernate);
        dbServicePersonsInGames = new DbServicePersonsInGamesImpl(personsInGamesDaoHibernate);
        dbServiceBet = new DbServiceBetImpl(betDaoHibernate);
        dbServiceGameSettings = new DbServiceGameSettingsImpl(gameSettingsDaoHibernate);

        gameProcessor = new GameProcessorImpl(dbServicePerson, dbServiceGame, dbServicePersonsInGames, dbServiceBet, dbServiceGameSettings);
    }

    @AfterEach
    void tearDown() {
        sessionFactory.close();
    }

    protected Game getNewGame() {
        return Game.builder()
                .settings(getNewGameSettings())
                .get();
    }


    protected Account getNewAccount() {
        return Account.builder()
                .sum(500L)
                .get();
    }

    protected GameSettings getNewGameSettings() {
        return GameSettings.builder()
                .numberOfCheats(initialCheats)
                .numberOfItems(initialItems)
                .numberOfTurns(initialTurns)
                .get();
    }

    protected Person getNewPerson1() {
        return Person.builder()
                .login("vitkus")
                .name("Viktoria")
                .password("12345")
                .account(getNewAccount())
                .get();
    }

    protected Person getNewPerson2() {
        return Person.builder()
                .login("koshir")
                .name("Konstantin")
                .password("24680")
                .account(getNewAccount())
                .get();
    }

    protected Person getNewPerson3() {
        return Person.builder()
                .login("sevantius")
                .name("Vsevolod")
                .password("11111")
                .account(getNewAccount())
                .get();
    }

    protected PersonsInGames getNewPerson1InGame(Game game) {
        return PersonsInGames.builder()
                .game(game)
                .person(getNewPerson1())
                .role(Roles.Player1.id())
                .get();
    }

    protected PersonsInGames getNewPerson2InGame(Game game) {
        return PersonsInGames.builder()
                .game(game)
                .person(getNewPerson2())
                .role(Roles.Player2.id())
                .get();
    }

    protected PersonsInGames getNewObserverInGame(Game game) {
        return PersonsInGames.builder()
                .game(game)
                .person(getNewPerson3())
                .role(Roles.Observer.id())
                .get();
    }

    protected List<PersonsInGames> getNewPersonsInNewGame() {
        val list = new ArrayList<PersonsInGames>(3);
        val game = getNewGame();
        list.add(getNewPerson1InGame(game));
        list.add(getNewPerson2InGame(game));
        list.add(getNewObserverInGame(game));
        return list;
    }

    protected Bet getNewBet(PersonsInGames person1, PersonsInGames person2) {
        return Bet.builder()
                .person1(person1)
                .person2(person2)
                .expectedResult(EventResults.Player1Won.id())
                .wager(100L)
                .get();

    }
}
