package ru.otus.vsh.knb.dbCore.dbService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.vsh.knb.dbCore.dao.AccountDao;
import ru.otus.vsh.knb.dbCore.dbService.api.AbstractDbServiceImpl;
import ru.otus.vsh.knb.dbCore.model.Account;

@Slf4j
@Repository
public class DbServiceAccountImpl extends AbstractDbServiceImpl<Account> implements DBServiceAccount {
    public DbServiceAccountImpl(AccountDao accountDao) {
        super(accountDao);
    }
}
