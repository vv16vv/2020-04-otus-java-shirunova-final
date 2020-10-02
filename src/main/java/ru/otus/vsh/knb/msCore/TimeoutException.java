package ru.otus.vsh.knb.msCore;

public class TimeoutException extends RuntimeException{
    public TimeoutException(){
        super();
    }

    public TimeoutException(String message){
        super(message);
    }
}
