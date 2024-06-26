package com.InstaGenius.AuthorizationServer.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    /* Security filter chain responsible for auth protocol endpoint, for example oauth2/token
    *
    * If error occurs (for example user cannot get token, because of bad credentials), 401 is returned
    * */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(withDefaults()); /* enable OpenID connect */
        http
                .exceptionHandling((exceptions) -> exceptions
//                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                                .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"),
                                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML))
//                                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))

                )
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(withDefaults()));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> {
                            request.requestMatchers( "/error","/static/**" ,"/webjars/**", "/images/**", "/css/**", "/assets/**", "/favicon.ico", "/login").permitAll();
                            request.anyRequest().authenticated();
                        }
                )
//                .oauth2Login(withDefaults())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        )
                .build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web
//                .debug(false)
//                .ignoring()
//                .requestMatchers("/webjars/**", "/images/**", "/css/**", "/assets/**", "/favicon.ico");
//    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user1 = User.withUsername("user").password("password").roles("USER").build();
        var user2 = User.withUsername("user2").password("password").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /* Beans required for authorization server, checking tokens etc */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }


    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient testClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .clientAuthenticationMethods(clientAuthenticationMethods -> {
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                })
                .authorizationGrantTypes(authorizationGrantTypes -> {
                    authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    authorizationGrantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                    authorizationGrantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .redirectUri("http://127.0.0.1:8082/login/oauth2/code/test-client")
                .postLogoutRedirectUri("http://127.0.0.1:8082/logged-out")
                .scopes(scopes -> {
                    scopes.add(OidcScopes.OPENID);
                    scopes.add(OidcScopes.PROFILE);
//                    scopes.add(OidcScopes.EMAIL);
                    scopes.add("user.read");
//                    scopes.add("user.write");
                })
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        return new InMemoryRegisteredClientRepository(testClient);
    }

}
