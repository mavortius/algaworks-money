package com.algaworks.algamoney.configuration;

import com.algaworks.algamoney.configuration.property.ProfileType;
import com.algaworks.algamoney.token.CustomTokenEnhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Profile(ProfileType.OAUTH_SECURITY)
@Configuration
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  public AuthorizationServerConfigurer(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
            .inMemory()
            .withClient("web")
            .secret(passwordEncoder.encode("angular")) // @ngul@r
            .scopes("read", "write")
            .authorizedGrantTypes("password", "refresh_token")
            .accessTokenValiditySeconds(60)
            .refreshTokenValiditySeconds(3600 * 24)
            .and()
            .withClient("mobile")
            .secret("$2a$10$z0YxaEuXyt3duJIiEIMAlO/dKSppLKWZhLqcS5FEon2y7ZqqlwYda") // m0b1l3
            .scopes("read")
            .authorizedGrantTypes("password", "refresh_token")
            .accessTokenValiditySeconds(60)
            .refreshTokenValiditySeconds(3600 * 24);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
    endpoints
            .tokenStore(tokenStore())
            .tokenEnhancer(tokenEnhancerChain)
            .reuseRefreshTokens(false)
            .userDetailsService(userDetailsService)
            .authenticationManager(authenticationManager);
  }

  @Bean
  public TokenEnhancer tokenEnhancer() {
    return new CustomTokenEnhancer();
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey("algaworks");
    return converter;
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }
}
