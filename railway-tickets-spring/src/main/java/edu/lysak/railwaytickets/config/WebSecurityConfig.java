package edu.lysak.railwaytickets.config;

import edu.lysak.railwaytickets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Class for ensuring that only authenticated users can see the secret greeting
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    /**
     * Method defines which URL paths should be secured and which should not.
     * Specifically, the / and /home paths are configured to not require any authentication.
     * All other paths must be authenticated
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/home",
                        "/routes/*",
                        "/registration",
                        "/i18n/**",
                        "/api/routes",
                        "/api/routes/search",   //todo - check if it is working
                        "/api/stations",
                        "/style.css",
                        "/home.main.js",
                        "/tickets.main.js",
                        "/admin/stations.main.js",
                        "/admin/routes.main.js"
//                        "/img/ukrainian.png"//todo - check if it is working
                )
                .permitAll()
                .antMatchers(
                        "/admin/**",
                        "/api/routes/*",
                        "/api/stations/*",
                        "/api/routes/*/edit",   //todo - check if it is working
                        "/api/stations/*/edit") //todo - check if it is working
                .hasAuthority("ADMIN")
                .antMatchers("/api/tickets") //todo - check if it is working
                .hasAuthority("USER")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    /**
     * Method for taking users from database
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}
