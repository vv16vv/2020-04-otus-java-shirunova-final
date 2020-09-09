package ru.otus.vsh.knb.hibernate.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dao.GameSettingsDao;
import ru.otus.vsh.knb.dbCore.model.GameSettings;
import ru.otus.vsh.knb.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

@Slf4j
@Component
public class GameSettingsDaoHibernate extends AbstractDaoHibernate<GameSettings> implements GameSettingsDao {

    public GameSettingsDaoHibernate(SessionManagerHibernate sessionManager) {
        super(sessionManager, GameSettings.class);
    }

    @Override
    public Optional<GameSettings> getSettingsByParams(int numberOfItems, int numberOfTurns, int numberOfCheats) {
        try {
            val entityManager = sessionManager.getEntityManager();
            val settings = entityManager
                    .createNamedQuery(GameSettings.GET_SETTINGS_BY_PARAMS, GameSettings.class)
                    .setParameter("numberOfItems", numberOfItems)
                    .setParameter("numberOfTurns", numberOfTurns)
                    .setParameter("numberOfCheats", numberOfCheats)
                    .getResultList();
            if (settings.isEmpty()) return Optional.empty();
            else return Optional.of(settings.get(0));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

}
