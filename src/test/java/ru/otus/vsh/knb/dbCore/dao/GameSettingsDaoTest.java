package ru.otus.vsh.knb.dbCore.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class GameSettingsDaoTest extends BaseDaoTest {

    @Test
    void createNewGameSettingsInsert() {
        val gameSettings = getNewGameSettings();

        sessionManagerHibernate.beginSession();
        val newGameSettingsId = gameSettingsDaoHibernate.insert(gameSettings);
        sessionManagerHibernate.commitSession();

        assertThat(newGameSettingsId).isGreaterThan(0L);
    }

    @Test
    void searchExistingGameSettingsById() {
        val gameSettings = getNewGameSettings();

        sessionManagerHibernate.beginSession();
        val newGameSettingsId = gameSettingsDaoHibernate.insert(gameSettings);
        sessionManagerHibernate.commitSession();
        log.info("GameSettings is created with id #{} ", newGameSettingsId);

        sessionManagerHibernate.beginSession();
        val foundGameSettings = gameSettingsDaoHibernate.findById(newGameSettingsId);
        sessionManagerHibernate.commitSession();
        log.info("Found gameSettings is {}", foundGameSettings);

        assertThat(foundGameSettings).isPresent();
        assertThat(foundGameSettings.get()).isEqualTo(gameSettings);
    }

    @Test
    void searchGameSettingsByParamsExisting() {
        val gameSettings = getNewGameSettings();

        sessionManagerHibernate.beginSession();
        val newGameSettingsId = gameSettingsDaoHibernate.insert(gameSettings);
        sessionManagerHibernate.commitSession();
        log.info("GameSettings is created with id #{} ", newGameSettingsId);

        sessionManagerHibernate.beginSession();
        val foundGameSettings = gameSettingsDaoHibernate
                .getSettingsByParams(initialItems, initialTurns, initialCheats);
        val allGameSettings = gameSettingsDaoHibernate.findAll();
        sessionManagerHibernate.commitSession();
        log.info("Found gameSettings is {}", foundGameSettings);

        assertThat(foundGameSettings).isPresent();
        assertThat(foundGameSettings.get()).isEqualTo(gameSettings);
        assertThat(allGameSettings).hasSize(1);
    }

    @Test
    void searchGameSettingsByParamsNotExistItems() {
        val gameSettings = getNewGameSettings();

        sessionManagerHibernate.beginSession();
        val newGameSettingsId = gameSettingsDaoHibernate.insert(gameSettings);
        sessionManagerHibernate.commitSession();
        log.info("GameSettings is created with id #{} ", newGameSettingsId);

        sessionManagerHibernate.beginSession();
        val foundGameSettings = gameSettingsDaoHibernate
                .getSettingsByParams(changedItems, initialTurns, initialCheats);
        val allGameSettings = gameSettingsDaoHibernate.findAll();
        sessionManagerHibernate.commitSession();
        log.info("Found gameSettings is {}", foundGameSettings);

        assertThat(foundGameSettings).isEmpty();
        assertThat(allGameSettings).hasSize(1);
    }

    @Test
    void searchGameSettingsByParamsNotExistTurns() {
        val gameSettings = getNewGameSettings();

        sessionManagerHibernate.beginSession();
        val newGameSettingsId = gameSettingsDaoHibernate.insert(gameSettings);
        sessionManagerHibernate.commitSession();
        log.info("GameSettings is created with id #{} ", newGameSettingsId);

        sessionManagerHibernate.beginSession();
        val foundGameSettings = gameSettingsDaoHibernate
                .getSettingsByParams(initialItems, changedTurns, initialCheats);
        val allGameSettings = gameSettingsDaoHibernate.findAll();
        sessionManagerHibernate.commitSession();
        log.info("Found gameSettings is {}", foundGameSettings);

        assertThat(foundGameSettings).isEmpty();
        assertThat(allGameSettings).hasSize(1);
    }

    @Test
    void searchGameSettingsByParamsNotExistCheats() {
        val gameSettings = getNewGameSettings();

        sessionManagerHibernate.beginSession();
        val newGameSettingsId = gameSettingsDaoHibernate.insert(gameSettings);
        sessionManagerHibernate.commitSession();
        log.info("GameSettings is created with id #{} ", newGameSettingsId);

        sessionManagerHibernate.beginSession();
        val foundGameSettings = gameSettingsDaoHibernate
                .getSettingsByParams(initialItems, initialTurns, changedCheats);
        val allGameSettings = gameSettingsDaoHibernate.findAll();
        sessionManagerHibernate.commitSession();
        log.info("Found gameSettings is {}", foundGameSettings);

        assertThat(foundGameSettings).isEmpty();
        assertThat(allGameSettings).hasSize(1);
    }

}
