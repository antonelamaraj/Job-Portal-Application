package amaraj.searchjob.application.configuration;

//import amaraj.searchjob.application.service.impl.UserDetailsServiceImpl;

import amaraj.searchjob.application.service.impl.UserDetailsServiceImpl;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.key}")
    private String jwtKey;

    @Bean
    PasswordEncoder setEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService setupUserDetails(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    SecurityFilterChain setUpFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests( req -> req
                       .requestMatchers("/login").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()  //te gjithe mund te bejne log in

                        .requestMatchers(HttpMethod.GET,"/api/users/**").permitAll()
                    //    .requestMatchers(HttpMethod.GET,"/api/**").hasAnyAuthority("ADMIN")

                        .requestMatchers(HttpMethod.POST,"/api/employees/**").hasAnyAuthority("ROLE_EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/employees/**").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/employees/jobsApplied**").hasAnyAuthority("ROLE_EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT,"/api/employees/**").hasAnyAuthority("ROLE_EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE,"/api/employees/**").hasAnyAuthority("ROLE_EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/employees/**").permitAll()

                        .requestMatchers(HttpMethod.GET,"/api/companies/job/**").hasAnyAuthority("ROLE_COMPANY")
                        .requestMatchers(HttpMethod.GET,"/api/companies/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/companies/**").hasAnyAuthority("ROLE_COMPANY")
                        .requestMatchers(HttpMethod.DELETE,"/api/companies/**").hasAnyAuthority("ROLE_COMPANY")


                        .requestMatchers(HttpMethod.GET,"/api/jobs/archive/**").hasAnyAuthority("ROLE_COMPANY")
                        .requestMatchers(HttpMethod.GET,"/api/jobs/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/jobs/comp/**").hasAnyAuthority("ROLE_EMPLOYEE")   //aplikimi
                        .requestMatchers(HttpMethod.DELETE,"/api/jobs/empl/**").hasAnyAuthority("ROLE_EMPLOYEE") //anullimi
                        .requestMatchers(HttpMethod.POST,"/api/jobs/**").hasAnyAuthority("ROLE_COMPANY")
                        .requestMatchers(HttpMethod.PUT,"/api/jobs/**").hasAnyAuthority("ROLE_COMPANY")
                        .requestMatchers(HttpMethod.DELETE,"/api/jobs/**").hasAnyAuthority("ROLE_COMPANY")


                        .requestMatchers(HttpMethod.GET,"/api/applications/**").hasAnyAuthority("ROLE_COMPANY")
                        .requestMatchers(HttpMethod.DELETE,"/api/applications/**").hasAnyAuthority("ROLE_EMPLOYEE")


                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))

                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }



    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers("/swagger-ui/**","/v3/api-docs/**");
    }

    @Bean
    JwtEncoder initJwtEncoder(){
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
    }

    @Bean
    JwtDecoder initDecoder(){
        byte[] bytes = jwtKey.getBytes();
        SecretKeySpec originalKey = new SecretKeySpec(bytes, 0, bytes.length,"RSA");
        return NimbusJwtDecoder.withSecretKey(originalKey).macAlgorithm(MacAlgorithm.HS512).build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
