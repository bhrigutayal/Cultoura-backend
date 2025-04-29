package com.tourism.Cultoura.jwt;

import java.security.KeyPair;  
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class JwtSecurityConfig {
	
	
	  @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	  @SuppressWarnings({ "removal", "deprecation" })
	@Bean
	  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	      // Create a JwtGrantedAuthoritiesConverter and configure it to use the "roles" claim without adding a prefix.
	      JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
	      authoritiesConverter.setAuthoritiesClaimName("roles");
	      authoritiesConverter.setAuthorityPrefix(""); // No extra "ROLE_" prefix is added

	      // Create a JwtAuthenticationConverter and set the custom authorities converter.
	      JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
	      jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

	      return httpSecurity
	              .csrf(AbstractHttpConfigurer::disable)
	              .sessionManagement(session ->
	                      session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	              .authorizeRequests(auth ->
	                      auth.requestMatchers("/authenticate", "/actuator", "/actuator/*")
	                          .permitAll()
	                          .requestMatchers("/h2-console/**")
	                          .permitAll()
	                          .requestMatchers(HttpMethod.OPTIONS, "/**")
	                          .permitAll()
	                          .anyRequest()
	                          .authenticated())
	              .oauth2ResourceServer(oauth2 ->
	                      oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
	              .exceptionHandling(ex ->
	                      ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
	                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
	              .httpBasic(Customizer.withDefaults())
	              .headers(header -> header.frameOptions().sameOrigin())
	              .build();
	  }


    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService, 
            PasswordEncoder passwordEncoder) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }


    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("bhrigu")
                               .password(passwordEncoder.encode("test"))
                               .authorities("read")
                               .roles("USER")
                               .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        JWKSet jwkSet = new JWKSet(rsaKey());
        return (((jwkSelector, securityContext) 
                        -> jwkSelector.select(jwkSet)));
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(rsaKey().toRSAPublicKey())
                .build();
    }
    
    @Bean
    public RSAKey rsaKey() {
        
        KeyPair keyPair = keyPair();
        
        return new RSAKey
                .Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public KeyPair keyPair() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to generate an RSA Key Pair", e);
        }
    }
    
}


