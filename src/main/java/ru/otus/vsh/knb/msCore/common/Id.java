package ru.otus.vsh.knb.msCore.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Id implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    protected final String innerId = UUID.randomUUID().toString();
}
