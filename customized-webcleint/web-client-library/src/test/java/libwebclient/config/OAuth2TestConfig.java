package libwebclient.config;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OAuth2TestConfig {

    @Bean
    public OAuth2ClientProperties createOAuth2ClientProperties() {

        OAuth2ClientProperties.Registration registration = new OAuth2ClientProperties.Registration();

        registration.setClientId("apim");
        registration.setAuthorizationGrantType("client_credentials");
        registration.setClientName("apim");
        registration.setClientSecret("secret");

        OAuth2ClientProperties.Provider provider = new OAuth2ClientProperties.Provider();
        provider.setTokenUri("testTokenURI");


        OAuth2ClientProperties oAuth2ClientProperties = Mockito.mock(OAuth2ClientProperties.class);

        Mockito.when(oAuth2ClientProperties.getRegistration()).thenReturn(
                Map.of("apim", registration));

        Mockito.when(oAuth2ClientProperties.getProvider()).thenReturn(
                Map.of("apim", provider)
        );
        return oAuth2ClientProperties;
    }
}
