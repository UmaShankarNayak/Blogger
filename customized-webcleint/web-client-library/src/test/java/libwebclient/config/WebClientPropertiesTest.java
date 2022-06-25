package libwebclient.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import webclient.config.WebClientProperties;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
@EnableConfigurationProperties(WebClientProperties.class)
public class WebClientPropertiesTest {

    @Autowired
    private WebClientProperties properties;

    @Test
    public void testWebClientPropertiesDirectServer() {
        assertNotNull(properties);

        final WebClientProperties.DirectServerProperties server = properties.getDirectServer();
        assertNotNull(server);
        assertTrue(server.getEnabled());
        assertEquals("http://localhost:8080", server.getUrl());
        assertNotNull(server.getPath());
        assertEquals(1, server.getPath().size());
        Map<String, WebClientProperties.PathProperties> paths = server.getPath();
        assertNotNull(paths);
        WebClientProperties.PathProperties path = paths.get("v1");
        assertEquals("/api/junit", path.getUri());
        assertNotNull(path.getProperties());
        assertEquals(2, path.getProperties().size());
        assertEquals("p1", path.getProperties().get("property1"));
        assertEquals("p2", path.getProperties().get("property2"));
        WebClientProperties.DirectServerProperties.SecurityProperties security = server.getSecurity();
        assertNotNull(security);
        assertEquals("default", security.getUsername());
        assertEquals("password", security.getPassword());
        WebClientProperties.DirectServerProperties.ProxyProperties proxyProperties = server.getProxy();
        assertNotNull(proxyProperties);
        assertEquals("hostProxy", proxyProperties.getHost());
        assertEquals(Integer.valueOf(78), proxyProperties.getPort());
        assertEquals("user", proxyProperties.getUsername());
        assertEquals("pwd", proxyProperties.getPassword());
        WebClientProperties.DirectServerProperties.TimeoutProperties timeoutProperties = server.getTimeout();
        assertNotNull(security);
        assertEquals(Integer.valueOf(3000), timeoutProperties.getConnect());
        assertEquals(Integer.valueOf(10000), timeoutProperties.getRead());

        final WebClientProperties.ApimProperties apim = properties.getApim();
        assertTrue(apim.getEnabled());
        assertEquals("http://localhost:8280", apim.getUrl());
        assertEquals("TEST", apim.getHeaderMultiBackend());

        Map<String, WebClientProperties.PathProperties> apimPaths = apim.getPath();
        assertNotNull(apimPaths);
        WebClientProperties.PathProperties apimPath = apimPaths.get("pathapim");
        assertEquals("/apim/junit", apimPath.getUri());
        assertNotNull(apimPath.getProperties());
        assertEquals(2, apimPath.getProperties().size());
        assertEquals("a1", apimPath.getProperties().get("apimproperty1"));
        assertEquals("a2", apimPath.getProperties().get("apimproperty2"));

        final WebClientProperties.RetryConfigProperties retry = properties.getRetry();
        assertEquals(2, retry.getMaxAttempts());
        assertEquals(Duration.ofSeconds(1), retry.getMinBackoff());
    }
}
