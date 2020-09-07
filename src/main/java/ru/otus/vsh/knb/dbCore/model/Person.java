package ru.otus.vsh.knb.dbCore.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

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
public class Person implements Model, UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // there are no different access permissions.
        // player is either allowed to do anything or not allowed at all
        return null;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
