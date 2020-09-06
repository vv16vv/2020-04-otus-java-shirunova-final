package ru.otus.vsh.knb.dbCore.model;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(buildMethodName = "get")
@ToString(exclude = {"password"})
@Data
@NamedQueries({
        @NamedQuery(
                name = Person.GET_PERSON_BY_LOGIN,
                query = "select p from Person p where login = :login"
        )
})
@Entity
@Table(name = "z7_persons")
public class Person implements Model {
    public static final String GET_PERSON_BY_LOGIN = "get_user_by_login";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(targetEntity = Account.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

}
