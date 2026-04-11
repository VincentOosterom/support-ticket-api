package nl.ticketsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${spring.security.oauth2.resourceserver.jwt.audiences}")
    private String audience;

    @Value("${client-id}")
    private String clientId;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .httpBasic(hp -> hp.disable())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                .decoder(jwtDecoder())
                        ))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users/sync").authenticated()

                        .requestMatchers(HttpMethod.POST, "/tickets").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/tickets/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/tickets/**").hasAnyRole("AGENT", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/tickets/*/comments").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/tickets/*/comments/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/attachments/**").hasAnyRole("CUSTOMER", "AGENT")
                        .requestMatchers(HttpMethod.GET, "/attachments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/attachments/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/ticket-assignments/**").hasAnyRole("ADMIN", "AGENT")
                        .requestMatchers(HttpMethod.PUT, "/ticket-assignments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/ticket-assignments/**").hasAnyRole("ADMIN", "AGENT")
                        .requestMatchers(HttpMethod.DELETE, "/ticket-assignments/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/ticket-history/**").authenticated()

                        .anyRequest().denyAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer);
        OAuth2TokenValidator<Jwt> audienceValidator = new JwtAudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }

    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new Converter<>() {
            @Override
            public Collection<GrantedAuthority> convert(Jwt source) {
                Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                for (String authority : getAuthorities(source)) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(authority));
                }
                return grantedAuthorities;
            }

            private List<String> getAuthorities(Jwt jwt) {
                Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
                if (resourceAccess != null) {
                    if (resourceAccess.get(clientId) instanceof Map) {
                        Map<String, Object> client = (Map<String, Object>) resourceAccess.get(clientId);
                        if (client != null && client.containsKey("roles")) {
                            return (List<String>) client.get("roles");
                        }
                    } else {
                        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                        if (realmAccess != null && realmAccess.containsKey("roles")) {
                            return (List<String>) realmAccess.get("roles");
                        }
                    }
                }
                return new ArrayList<>();
            }
        });
        return jwtAuthenticationConverter;
    }
}