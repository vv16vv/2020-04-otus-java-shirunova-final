package ru.otus.vsh.knb.dbCore.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import ru.otus.vsh.knb.dbCore.model.Account;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AccountDaoTest extends BaseDaoTest {

    @Test
    void searchUnexistingAccount() {
        sessionManagerHibernate.beginSession();
        Optional<Account> account = accountDaoHibernate.findById(100500);
        sessionManagerHibernate.commitSession();

        assertThat(account).isEmpty();
    }

    @Test
    void createNewAccountInsert() {
        val account = getNewAccount();

        sessionManagerHibernate.beginSession();
        val newAccountId = accountDaoHibernate.insert(account);
        sessionManagerHibernate.commitSession();

        assertThat(newAccountId).isGreaterThan(0L);
    }

    @Test
    void createNewAccountInsertOrUpdate() {
        val account = getNewAccount();

        sessionManagerHibernate.beginSession();
        accountDaoHibernate.insertOrUpdate(account);
        val newAccountId = account.getId();
        sessionManagerHibernate.commitSession();

        assertThat(newAccountId).isGreaterThan(0L);
    }

    @Test
    void searchExistingAccount() {
        val account = getNewAccount();

        sessionManagerHibernate.beginSession();
        val newAccountId = accountDaoHibernate.insert(account);
        sessionManagerHibernate.commitSession();
        log.info("Account is created with id #{}", newAccountId);

        sessionManagerHibernate.beginSession();
        val foundAccount = accountDaoHibernate.findById(newAccountId);
        sessionManagerHibernate.commitSession();
        log.info("Found account is {}", foundAccount);

        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get()).isEqualTo(account);
    }

    @Test
    void editAccountUpdate() {
        val account = getNewAccount();

        sessionManagerHibernate.beginSession();
        accountDaoHibernate.insert(account);
        sessionManagerHibernate.commitSession();

        account.setSum(600);
        sessionManagerHibernate.beginSession();
        accountDaoHibernate.update(account);
        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();
        val editedAccount = accountDaoHibernate.findById(account.getId());
        sessionManagerHibernate.commitSession();

        assertThat(editedAccount).isPresent();
        assertThat(editedAccount.get().getSum()).isEqualTo(account.getSum());
    }

    @Test
    void editAccountInsertOrUpdate() {
        val account = getNewAccount();

        sessionManagerHibernate.beginSession();
        accountDaoHibernate.insert(account);
        sessionManagerHibernate.commitSession();

        account.setSum(600);
        sessionManagerHibernate.beginSession();
        accountDaoHibernate.insertOrUpdate(account);
        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();
        val editedAccount = accountDaoHibernate.findById(account.getId());
        sessionManagerHibernate.commitSession();

        assertThat(editedAccount).isPresent();
        assertThat(editedAccount.get().getSum()).isEqualTo(account.getSum());
    }

}
