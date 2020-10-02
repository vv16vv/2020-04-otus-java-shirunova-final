package ru.otus.vsh.knb.dbCore.sessionmanager;

import javax.persistence.EntityManager;

public interface SessionManager extends AutoCloseable {
    void beginSession();

    void commitSession();

    void rollbackSession();

    void close();

    EntityManager getEntityManager();

    DatabaseSession getCurrentSession();
}
