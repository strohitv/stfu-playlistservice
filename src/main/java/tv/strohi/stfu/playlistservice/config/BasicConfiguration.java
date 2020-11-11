package tv.strohi.stfu.playlistservice.config;

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
    ServiceSettings settings = StfuPlaylistServiceApplication.getSettings();

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (!settings.getUser().isBlank() && !settings.getPassword().isBlank()) {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            auth.inMemoryAuthentication()
                    .withUser(settings.getUser())
                    .password(encoder.encode(settings.getPassword()))
                    .roles("USER");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        if (!settings.getUser().isBlank() && !settings.getPassword().isBlank()) {
            http.authorizeRequests()

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
            http.authorizeRequests()
                    .anyRequest()
                    .permitAll();
        }
    }
}
