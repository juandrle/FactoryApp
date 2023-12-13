package de.swtpro.factorybuilder.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


        @Bean 
        PasswordEncoder passwordEncoder(){
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
        @Bean 
        public UserDetailsService userDetailsService() {
            UserBuilder userbuilder = User.withDefaultPasswordEncoder();

            UserDetails user1 = userbuilder.username("joendhard").password("1234")
                                .roles("USER").build();
            

            return new InMemoryUserDetailsManager(user1);
        }

        @Bean
        SecurityFilterChain filterChainApp(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz 
        .requestMatchers(HttpMethod.GET, "/assets/*").permitAll()
        .requestMatchers("/signup").permitAll()
        

        .requestMatchers("/home", "/factory", "/create").hasRole("USER")
        .anyRequest().authenticated())
        .formLogin(withDefaults()) // oder httpBasic oder ... // optional Redirect auf Wunschseite nach Logut 

        .logout().logoutUrl("/logout").logoutSuccessUrl("/frage"); 

        return http.build(); 
    }
}
    

