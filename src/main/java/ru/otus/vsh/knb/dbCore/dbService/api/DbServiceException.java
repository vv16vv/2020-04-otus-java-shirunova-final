package ru.otus.vsh.knb.dbCore.dbService.api;

public class DbServiceException extends RuntimeException {
    public DbServiceException(Exception e) {
        super(e);
    }
}
