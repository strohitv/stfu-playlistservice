package tv.strohi.stfu.playlistservice.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import tv.strohi.stfu.playlistservice.StfuPlaylistServiceApplication;

@Configuration
@EnableWebSecurity
public class BasicConfiguration extends WebSecurityConfigurerAdapter {
    private final Logger logger = LogManager.getLogger(BasicConfiguration.class.getCanonicalName());
    ServiceSettings settings = StfuPlaylistServiceApplication.getSettings();

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (!settings.getUser().isBlank() && !settings.getPassword().isBlank()) {
            logger.debug("setting username {} and password [HIDDEN] to use for basic auth", settings.getUser());

            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            auth.inMemoryAuthentication()
                    .withUser(settings.getUser())
                    .password(encoder.encode(settings.getPassword()))
                    .roles("USER");
        } else {
            logger.debug("user nameand password won't be set for basic auth");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.debug("setting stateless session policy");
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        if (!settings.getUser().isBlank() && !settings.getPassword().isBlank()) {
            logger.info("setting up username and password auth");
            http.csrf().disable()
                    .authorizeRequests()

                    .antMatchers("/swagger-ui")
                    .permitAll()
                    .antMatchers("/swagger-ui.html")
                    .permitAll()
                    .antMatchers("/v3")
                    .permitAll()

                    .anyRequest()
                    .authenticated()
                    .and()
                    .httpBasic();
        } else {
            logger.info("setting up permit all");
            http.csrf().disable()
                    .authorizeRequests()
                    .anyRequest()
                    .permitAll();
        }
    }
}
