package ru.otus.vsh.knb;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.vsh.knb.dbCore.model.*;
import ru.otus.vsh.knb.hibernate.HibernateUtils;

@Configuration
@ComponentScan
public class AppSessionFactoryInit {

    private static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML,
                Bet.class,
                PersonsInGames.class,
                Game.class,
                GameSettings.class,
                Person.class,
                Account.class);
    }

}
