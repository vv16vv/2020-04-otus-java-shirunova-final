package ru.otus.vsh.knb.dbCore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(buildMethodName = "get")
@Data
@Entity
@Table(name = "z8_accounts")
public class Account implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @Builder.Default
    private long id = 0L;

    @Column(name = "sum", nullable = false)
    private long sum;

    public long increase(long inc) {
        sum += inc;
        return sum;
    }

    public long decrease(long dec) {
        return increase(-dec);
    }
}
