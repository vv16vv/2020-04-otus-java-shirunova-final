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
                name = PersonsInGames.GET_ALL_PERSONS_BY_GAME,
                query = "select s from PersonsInGames s where game.id = :gameId"
        ),
        @NamedQuery(
                name = PersonsInGames.GET_PIG_BY_GAME_AND_ROLE,
                query = "select s from PersonsInGames s where game.id = :gameId and role = :role"
        ),
        @NamedQuery(
                name = PersonsInGames.GET_PIG_BY_GAME_AND_PERSON,
                query = "select s from PersonsInGames s where game.id = :gameId and person.id = :personId"
        )
})
@Table(name = "z2_persons_in_games")
public class PersonsInGames implements Model {
    public static final String GET_ALL_PERSONS_BY_GAME = "get_all_persons_by_game";
    public static final String GET_PIG_BY_GAME_AND_ROLE = "get_person_by_game_and_role";
    public static final String GET_PIG_BY_GAME_AND_PERSON = "get_pig_by_game_and_person";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @Builder.Default
    private long id = 0L;

    @ManyToOne(targetEntity = Game.class, cascade = CascadeType.ALL)
    private Game game;

    @ManyToOne(targetEntity = Person.class, cascade = CascadeType.ALL)
    private Person person;

    @Column(name = "role", nullable = false)
    private int role;

    public Roles getRole() {
        return Roles.fromId(role);
    }

}
