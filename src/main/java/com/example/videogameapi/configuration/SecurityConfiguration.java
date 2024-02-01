package com.example.videogameapi.configuration;

import com.example.videogameapi.filters.JwtAuthFilter;
import com.example.videogameapi.repositorires.UserRepo;
import com.example.videogameapi.services.VideoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserRepo userRepo;
    private final JwtAuthFilter jwtAuthFilter;
    private final VideoUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(JwtAuthFilter jwtAuthFilter, UserRepo userRepo, VideoUserDetailsService videoUserDetailsService) {
        this.userDetailsService = videoUserDetailsService;
        this.userRepo = userRepo;
        this.jwtAuthFilter = jwtAuthFilter;
    }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((request) -> request
                            .requestMatchers(new AntPathRequestMatcher("/register*")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/login*")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/error*")).permitAll()
                            .anyRequest().authenticated())
                    .authenticationProvider(authenticationProvider())
                    .sessionManagement(Customizer.withDefaults())
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .logout(Customizer.withDefaults());
            return http.build();
        }

        @Bean
        public static PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }


        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
            daoAuthenticationProvider.setUserDetailsService(userDetailsService);
            daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
            return daoAuthenticationProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
           return authenticationConfiguration.getAuthenticationManager();
        }
}