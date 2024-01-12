package de.swtpro.factorybuilder.configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import de.swtpro.factorybuilder.controller.UserRestController;

import java.security.PublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    HandlerMappingIntrospector introspector;
    SecurityConfiguration(HandlerMappingIntrospector introspector){
       this.introspector = introspector;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.builder()
                .username("joendhard")
                .password(passwordEncoder().encode("1234"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1);
    }

    @Order(1)
    @Bean
    SecurityFilterChain filterChainAPI(HttpSecurity http) throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);
        http.securityMatchers(s -> s.requestMatchers(mvc.pattern("/api/**"), mvc.pattern("/stompbroker")))
                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults())
                .cors(withDefaults())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain filterChainApp(HttpSecurity http) throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(toH2Console()).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/models/**"), mvc.pattern(HttpMethod.GET, "/icons/**")).permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(withDefaults()).formLogin(in -> in.defaultSuccessUrl("/h2-console", true))
                .csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()))
                .headers(hdrs -> hdrs.frameOptions().sameOrigin())
                .cors(withDefaults())
                .logout(withDefaults()).logout(out -> out.logoutSuccessUrl("/login"));

        return http.build();
    }


}