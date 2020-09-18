package ru.otus.vsh.knb;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.vsh.knb.dbCore.dbService.DBServicePerson;
import ru.otus.vsh.knb.webCore.Routes;
import ru.otus.vsh.knb.webCore.SessionKeeper;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    private final DBServicePerson dbServicePerson;
    private final SessionKeeper sessionKeeper;

    @Autowired
    protected void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(dbServicePerson)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/*.css", "/*.js").permitAll()
                    .antMatchers(Routes.NEW_PLAYER).permitAll()
                    .antMatchers(Routes.ROOT).permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage(Routes.ROOT)
                    .loginProcessingUrl(Routes.API_LOGIN)
                    .defaultSuccessUrl(Routes.LOBBY, true)
                    .failureUrl(Routes.ERROR)
                .and()
                    .logout()
                    .logoutUrl(Routes.API_LOGOUT)
                    .addLogoutHandler((request, response, authentication) -> sessionKeeper.remove(request.getRequestedSessionId()))
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl(Routes.ROOT)
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
