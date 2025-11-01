//package com.adit.order_management_service.security.config;
//
//import com.adit.order_management_service.security.jwt.JwtAuthFilter;
//import com.adit.order_management_service.service.UserService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.RegexRequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class WebSecurityConfiguration {
//
//
//    //private final CustomAuthenticationEntryPoint authenticationEntryPoint;
//
//    private final UserService userDetailsService;
//    private final JwtAuthFilter jwtAuthFilter;
//
//    public WebSecurityConfiguration(UserService userDetailsService, JwtAuthFilter jwtAuthFilter) {
//        this.userDetailsService = userDetailsService;
//        this.jwtAuthFilter = jwtAuthFilter;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // Define in-memory users with encoded passwords
//    /*@Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder.encode("pass"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }*/
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
//
//    // Define security filter chain
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf(csrf -> csrf.disable()) // disable CSRF for APIs
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/health").permitAll()
//                        .anyRequest().authenticated()
//
////                        .requestMatchers(new RegexRequestMatcher(".*/public/.*", null)).permitAll() // access all
////                        .requestMatchers("/user/**").hasRole("USER") // user authenticated
////                        .requestMatchers("/admin/**").hasAnyRole("USER", "ADMIN") // restricted access
////                        .anyRequest().authenticated()
//                )
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//                /*.httpBasic(basic ->
//                        basic.authenticationEntryPoint(jwtAuthFilter));*/  // plug in custom authentication
//        return httpSecurity.build();
//    }
//
//
//
//    /*public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//
//        httpSecurity
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(
//                        auth -> auth.requestMatchers("/public/**").permitAll()
//                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                                .anyRequest().authenticated())
//                .httpBasic(basic-> basic.realmName("my app real name"));
//        return httpSecurity.build();
//
//    }*/
//}
