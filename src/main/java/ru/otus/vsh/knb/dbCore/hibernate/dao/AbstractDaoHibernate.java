package ru.otus.vsh.knb.dbCore.hibernate.dao;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import ru.otus.vsh.knb.dbCore.dao.Dao;
import ru.otus.vsh.knb.dbCore.dao.DaoException;
import ru.otus.vsh.knb.dbCore.model.Model;
import ru.otus.vsh.knb.dbCore.sessionmanager.SessionManager;
import ru.otus.vsh.knb.dbCore.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.vsh.knb.dbCore.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.List;
import java.util.Optional;

@Slf4j
abstract public class AbstractDaoHibernate<T extends Model> implements Dao<T> {

    protected final SessionManagerHibernate sessionManager;
    protected final Class<T> modelClass;

    public AbstractDaoHibernate(SessionManagerHibernate sessionManager, Class<T> modelClass) {
        this.sessionManager = sessionManager;
        this.modelClass = modelClass;
    }

    @Override
    public Optional<T> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getSession().find(modelClass, id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        var query = String.format("select u from %s u", modelClass.getSimpleName());
        try {
            var entityManager = sessionManager.getEntityManager();
            return entityManager
                    .createQuery(query, modelClass)
                    .getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    @Override
    public long insert(T t) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getSession();
            hibernateSession.save(t);
            return t.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public void update(T t) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getSession();
            hibernateSession.merge(t);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(T t) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getSession();
            if (t.getId() > 0) {
                hibernateSession.merge(t);
            } else {
                hibernateSession.persist(t);
                hibernateSession.flush();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

}
