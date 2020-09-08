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
    @Builder.Default
    private long id = 0L;

    @Column(name = "numberOfItems", nullable = false)
    @Builder.Default
    private int numberOfItems = 3;

    @Column(name = "numberOfTurns", nullable = false)
    @Builder.Default
    private int numberOfTurns = 3;

    @Column(name = "numberOfCheats", nullable = false)
    @Builder.Default
    private int numberOfCheats = 0;

    public String title() {
        return String.format("%d элементов / %d ходов / %d выручаек", numberOfItems, numberOfTurns, numberOfCheats);
    }
}
