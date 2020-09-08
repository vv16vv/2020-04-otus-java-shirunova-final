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
@NamedQueries({
        @NamedQuery(
                name = Bet.GET_BETS_BY_PIG,
                query = "select b from Bet b where person1.id = :personId or person2.id = :personId"
        )
})
@Table(name = "z1_bets")
public class Bet implements Model {
    public static final String GET_BETS_BY_PIG = "get_bets_by_pig";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @Builder.Default
    private long id = 0L;

    @ManyToOne(targetEntity = PersonsInGames.class, cascade = CascadeType.ALL)
    private PersonsInGames person1;

    @ManyToOne(targetEntity = PersonsInGames.class, cascade = CascadeType.ALL)
    @Builder.Default
    private PersonsInGames person2 = null;

    @Column(name = "expectedResult")
    private int expectedResult;

    @Column(name = "wager")
    private long wager;

    @Column(name = "isClosed")
    @Builder.Default
    private boolean isClosed = false;

    public EventResults getExpectedResult() {
        return EventResults.fromId(expectedResult);
    }
}
