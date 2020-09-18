package ru.otus.vsh.knb;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.vsh.knb.dbCore.model.*;
import ru.otus.vsh.knb.dbCore.hibernate.SessionFactoryUtils;
import ru.otus.vsh.knb.webCore.SessionKeeper;
import ru.otus.vsh.knb.webCore.SessionKeeperImpl;

@Configuration
@ComponentScan
public class AppConfig {

    private static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";

    @Bean
    public SessionFactory sessionFactory() {
        return SessionFactoryUtils.buildSessionFactory(HIBERNATE_CFG_XML,
                Bet.class,
                PersonsInGames.class,
                Game.class,
                GameSettings.class,
                Person.class,
                Account.class);
    }


    @Bean
    public SessionKeeper sessionKeeper() {
        return new SessionKeeperImpl();
    }
}
