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
                name = GameSettings.GET_SETTINGS_BY_PARAMS,
                query = "select s from GameSettings s where numberOfItems = :numberOfItems and numberOfTurns = :numberOfTurns and numberOfCheats = :numberOfCheats"
        )
})
@Table(name = "z4_game_settings", indexes = {
        @Index(columnList = "numberOfItems,numberOfTurns,numberOfCheats", unique = true)
})
public class GameSettings implements Model {
    public static final String GET_SETTINGS_BY_PARAMS = "get_settings_by_params";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "numberOfItems", nullable = false)
    private int numberOfItems;

    @Column(name = "numberOfTurns", nullable = false)
    private int numberOfTurns;

    @Column(name = "numberOfCheats", nullable = false)
    private int numberOfCheats;
}
