package ru.otus.vsh.knb.dbCore.hibernate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dao.AccountDao;
import ru.otus.vsh.knb.dbCore.model.Account;
import ru.otus.vsh.knb.dbCore.hibernate.sessionmanager.SessionManagerHibernate;

@Slf4j
@Component
public class AccountDaoHibernate extends AbstractDaoHibernate<Account> implements AccountDao {

    public AccountDaoHibernate(SessionManagerHibernate sessionManager) {
        super(sessionManager, Account.class);
    }

}
