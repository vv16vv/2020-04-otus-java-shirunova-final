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
                name = Game.GET_ACTIVE_GAMES,
                query = "select g from Game g where isCompleted = false"
        )
})
@Table(name = "z3_games")
public class Game implements Model {
    public static final String GET_ACTIVE_GAMES = "get_active_games";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @ManyToOne(targetEntity = GameSettings.class, optional = false, cascade = CascadeType.ALL)
    private GameSettings settings;

    @Column(name = "actualResult", nullable = false)
    private int actualResult;

    @Column(name = "isCompleted", nullable = false)
    private boolean isCompleted;

    public EventResults getActualResult(){
        return EventResults.fromId(actualResult);
    }

}
