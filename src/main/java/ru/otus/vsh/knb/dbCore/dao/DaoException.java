package ru.otus.vsh.knb.dbCore.dao;

public class DaoException extends RuntimeException {
    public DaoException(Exception ex) {
        super(ex);
    }
}
