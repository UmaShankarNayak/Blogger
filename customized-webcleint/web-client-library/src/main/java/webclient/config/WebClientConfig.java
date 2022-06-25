package webclient.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;
import webclient.application.CustomizedWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(WebClientProperties.class)
@Slf4j
public class WebClientConfig {

    @Autowired
    private WebClientProperties properties;

    @Bean(name = "webClientWithApim")
    @ConditionalOnExpression("${web.client.apim.enabled:false}")
    WebClient createWebClientWithApim(OAuth2ClientProperties oAuth2ClientProperties, ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        WebClientProperties.ApimProperties apimProperties = properties.getApim();


        List<ClientRegistration> clientRegistrations = new ArrayList<>(OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(oAuth2ClientProperties).values());

        InMemoryReactiveClientRegistrationRepository oAuth2clientRegistrationRepository = new InMemoryReactiveClientRegistrationRepository(clientRegistrations);

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(oAuth2clientRegistrationRepository, authorizedClientService)
        );


        oauth.setDefaultClientRegistrationId("apim");

        return WebClient.builder()
                .baseUrl(apimProperties.getUrl())
                .defaultHeader("APIM-O2-EndPoint", apimProperties.getHeaderMultiBackend())
                .filter(oauth)
                .filter(WebClientFilters.logResposneStatus())
                .filter(WebClientFilters.logRequest())
                .build();
    }

    @Bean(name = "webClientWithDirectServer")
    @ConditionalOnExpression("${web.client.direct-server.enabled:false}")
    public WebClient createWebClientWithDirectServer() {
        WebClient.Builder builder = WebClient.builder();
        WebClientProperties.DirectServerProperties directServerProperties = properties.getDirectServer();
        builder = builder.baseUrl(directServerProperties.getUrl());

        final WebClientProperties.DirectServerProperties.SecurityProperties authProperties = directServerProperties.getSecurity();
        if (authProperties != null) {
            builder.filter(ExchangeFilterFunctions.basicAuthentication(authProperties.getUsername(), authProperties.getPassword()));
        }

        HttpClient httpClient = HttpClient.create();

        final WebClientProperties.DirectServerProperties.ProxyProperties proxyProperties = directServerProperties.getProxy();


        if (proxyProperties != null) {
            httpClient = httpClient.
                    proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                            .host(proxyProperties.getHost())
                            .port(proxyProperties.getPort())
                            .username(proxyProperties.getUsername())
                            .password(username -> proxyProperties.getPassword())
                    );
        }

        final WebClientProperties.DirectServerProperties.TimeoutProperties timeoutProperties = directServerProperties.getTimeout();

        if (timeoutProperties != null) {
            httpClient = httpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutProperties.getConnect());
            httpClient = httpClient.doOnConnected(conn -> conn
                    .addHandlerLast(new ReadTimeoutHandler(timeoutProperties.getRead(), TimeUnit.MILLISECONDS)));
        }

        builder.clientConnector(new ReactorClientHttpConnector(httpClient.wiretap(true)));

        // adding filers for loggin
        builder.filter(WebClientFilters.logResposneStatus())
                .filter(WebClientFilters.logRequest());

        return builder.build();
    }

    @Bean("defaultWebClientWithApimAndDirectServer")
    @ConditionalOnExpression("${web.client.apim.enabled:false} and ${web.client.direct-server.enabled:false}")
    public CustomizedWebClient createDefaultConnectWebClientWithApimAndDirectServer(OAuth2ClientProperties oAuth2ClientProperties, ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        return new CustomizedWebClient(
                createWebClientWithApim(oAuth2ClientProperties, authorizedClientService),
                createWebClientWithDirectServer(),
                properties
        );
    }

    @Bean("defaultWebClientWithApim")
    @ConditionalOnExpression("${web.client.apim.enabled:false} and !${web.client.direct-server.enabled:false}")
    public CustomizedWebClient createDefaultConnectWebClientWithApim(OAuth2ClientProperties oAuth2ClientProperties, ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        return new CustomizedWebClient(
                createWebClientWithApim(oAuth2ClientProperties, authorizedClientService),
                null,
                properties
        );
    }


    @Bean("defaultWebClientWithDirectServer")
    @ConditionalOnExpression("!${web.client.apim.enabled:false} and ${web.client.direct-server.enabled:false}")
    public CustomizedWebClient createDefaultConnectWebClientWithDirectServer() {
        return new CustomizedWebClient(
                null,
                createWebClientWithDirectServer(),
                properties
        );
    }


}
