package libwebclient.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import webclient.config.WebClientConfig;

class WebClientConfigTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(WebClientConfig.class));


    @Test
    void createWebClientWithApim() {
        contextRunner.
                withPropertyValues("web.client.apim.enabled=true").
                withPropertyValues("web.client.apim.url=clientUrl").
                withPropertyValues("web.client.apim.path.v1.uri=api").
                withBean(ReactiveOAuth2AuthorizedClientServiceMock.class).
                withConfiguration(AutoConfigurations.of(OAuth2TestConfig.class)).
                run(
                        context -> {
                            Assertions.assertNotNull(context.getBean("webClientWithApim"));
                        }

                );
    }

    @Test
    void createWebClientWithDirectServer() {
        contextRunner.
                withPropertyValues("web.client.direct-server.enabled=true").
                withPropertyValues("web.client.direct-server.url=clientUrl").
                withPropertyValues("web.client.direct-server.proxy.host=proxyhost").
                withPropertyValues("web.client.direct-server.proxy.port=8080").
                withPropertyValues("web.client.direct-server.path.v1.uri=uri").
                withBean(ReactiveOAuth2AuthorizedClientServiceMock.class).
                withConfiguration(AutoConfigurations.of(OAuth2TestConfig.class)).
                run(
                        context -> {
                            Assertions.assertNotNull(context.getBean("webClientWithDirectServer"));

                        }

                );
    }


    @Test
    void createWebClientWithApim_should_throw_exception_when_path_is_null() {
        contextRunner.
                withPropertyValues("web.client.direct-server.enabled=false").
                withPropertyValues("web.client.apim.enabled=true").
                withPropertyValues("web.client.apim.url=clientUrl").
                withBean(ReactiveOAuth2AuthorizedClientServiceMock.class).
                withConfiguration(AutoConfigurations.of(OAuth2TestConfig.class)).
                run(
                        context -> {
                            Assertions.assertThrows(IllegalStateException.class, () -> {
                                context.getBean("webClientWithApim");

                            });

                        }

                );
    }

    @Test
    void testCreateDefaultConnectWebClientWithApimAndDirectServer() {

        contextRunner.
                withPropertyValues("web.client.apim.enabled=true").
                withPropertyValues("web.client.apim.url=clientUrl").
                withPropertyValues("web.client.apim.path.v1.uri=api").
                withPropertyValues("web.client.direct-server.enabled=true").
                withPropertyValues("web.client.direct-server.url=clientUrl").
                withPropertyValues("web.client.direct-server.path.v1.uri=uri").
                withBean(ReactiveOAuth2AuthorizedClientServiceMock.class).
                withConfiguration(AutoConfigurations.of(OAuth2TestConfig.class)).
                run(
                        context -> {
                            Assertions.assertNotNull(context.getBean("defaultWebClientWithApimAndDirectServer"));
                            Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean("defaultWebClientWithApim"));
                            Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean("defaultWebClientWithDirectServer"));
                        }

                );
    }

    @Test
    void testCreateDefaultConnectWebClientWithApim() {

        contextRunner.
                withPropertyValues("web.client.apim.enabled=true").
                withPropertyValues("web.client.apim.url=clientUrl").
                withPropertyValues("web.client.apim.path.v1.uri=api").
                withBean(ReactiveOAuth2AuthorizedClientServiceMock.class).
                withConfiguration(AutoConfigurations.of(OAuth2TestConfig.class)).
                run(
                        context -> {
                            Assertions.assertNotNull(context.getBean("defaultWebClientWithApim"));
                            Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean("defaultWebClientWithDirectServer"));
                            Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean("defaultWebClientWithApimAndDirectServer"));
                        }

                );
    }

    @Test
    void testCreateDefaultConnectWebClientWithDirectServer() {

        contextRunner.
                withPropertyValues("web.client.direct-server.enabled=true").
                withPropertyValues("web.client.direct-server.url=clientUrl").
                withPropertyValues("web.client.direct-server.path.v1.uri=uri").
                withBean(ReactiveOAuth2AuthorizedClientServiceMock.class).
                withConfiguration(AutoConfigurations.of(OAuth2TestConfig.class)).
                run(
                        context -> {
                            Assertions.assertNotNull(context.getBean("defaultWebClientWithDirectServer"));
                            Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean("defaultWebClientWithApim"));
                            Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean("defaultWebClientWithApimAndDirectServer"));
                        }

                );
    }

    @Test
    void testCreateDefaultConnectWebClientWithoutWebClient() {

        contextRunner.
                withPropertyValues("web.client.direct-server.enabled=false").
                withPropertyValues("web.client.apim.enabled=false").
                withBean(ReactiveOAuth2AuthorizedClientServiceMock.class).
                withConfiguration(AutoConfigurations.of(OAuth2TestConfig.class)).
                run(
                        context -> {
                            Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean("defaultWebClientWithDirectServer"));
                            Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean("defaultWebClientWithApim"));
                            Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean("defaultWebClientWithApimAndDirectServer"));
                        }

                );
    }
}