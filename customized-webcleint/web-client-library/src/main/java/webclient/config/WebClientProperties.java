package webclient.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.util.Map;

@Getter
@Validated
@ConfigurationProperties(prefix = "web.client")
@ConstructorBinding
public class WebClientProperties {

    @Valid
    private WebClientProperties.ApimProperties apim;

    @Valid
    private WebClientProperties.DirectServerProperties directServer;

    @Valid
    private WebClientProperties.RetryConfigProperties retry;

    public WebClientProperties(
            WebClientProperties.ApimProperties apim,
            WebClientProperties.DirectServerProperties directServer,
            WebClientProperties.RetryConfigProperties retry
    ) {
        this.apim = apim;
        this.directServer = directServer;
        this.retry = retry;
    }

    @Getter
    @ConstructorBinding
    public static final class RetryConfigProperties {
        private Integer maxAttempts;

        private Duration minBackoff;

        public RetryConfigProperties(
                @DefaultValue("10") Integer maxAttempts,
                @DefaultValue("1s") Duration minBackoff
        ) {
            this.maxAttempts = maxAttempts;
            this.minBackoff = minBackoff;
        }

    }


    @Getter
    @ConstructorBinding
    public static class ApimProperties {

        private Boolean enabled;

        private String url;

        private String headerMultiBackend;

        @Valid
        private Map<String, WebClientProperties.PathProperties> path;

        public ApimProperties(
                @DefaultValue("false") Boolean enabled,
                String url,
                @DefaultValue("DEV") String headerMultiBackend,
                Map<String, WebClientProperties.PathProperties> path
        ) {
            if (enabled) {
                Assert.notNull(path, "When APIM is enabled, please mention a path");
                Assert.notNull(url, "When APIM is enabled, please mention an APIM url");
            }
            this.enabled = enabled;
            this.url = url;
            this.headerMultiBackend = headerMultiBackend;
            this.path = path;
        }

    }

    @Getter
    @ConstructorBinding
    public static class DirectServerProperties {

        private Boolean enabled;

        private String url;

        @Valid
        private DirectServerProperties.SecurityProperties security;

        @Valid
        private DirectServerProperties.ProxyProperties proxy;

        @Valid
        private DirectServerProperties.TimeoutProperties timeout;

        @Valid
        private Map<String, PathProperties> path;

        public DirectServerProperties(
                @DefaultValue("false") Boolean enabled,
                String url,
                DirectServerProperties.SecurityProperties security,
                DirectServerProperties.ProxyProperties proxy,
                DirectServerProperties.TimeoutProperties timeout,
                Map<String, WebClientProperties.PathProperties> path
        ) {
            if (enabled) {
                Assert.notNull(path, "When Direct Server is enabled, please mention a path");
                Assert.notNull(url, "When Direct Server is enabled, please mention an Direct Server url");
            }
            this.enabled = enabled;
            this.url = url;
            this.security = security;
            this.proxy = proxy;
            this.timeout = timeout;
            this.path = path;
        }

        @Getter
        @ConstructorBinding
        public static class SecurityProperties {
            @Size(min = 1)
            private String username;
            private String password;

            public SecurityProperties(
                    String username,
                    String password
            ) {
                this.username = username;
                this.password = password;
            }
        }

        @Getter
        @ConstructorBinding
        public static class ProxyProperties {
            @Size(min = 1)
            private String host;
            @NotNull
            private Integer port;
            private String username;
            private String password;

            public ProxyProperties(
                    String host,
                    Integer port,
                    String username,
                    String password
            ) {
                this.host = host;
                this.port = port;
                this.username = username;
                this.password = password;
            }
        }

        @Getter
        @ConstructorBinding
        public static class TimeoutProperties {
            @NotNull
            private Integer connect;
            @NotNull
            private Integer read;

            public TimeoutProperties(
                    Integer connect,
                    Integer read
            ) {
                this.connect = connect;
                this.read = read;
            }
        }
    }

    @Getter
    @ConstructorBinding
    public static class PathProperties {
        @Size(min = 1)
        private String uri;
        private Map<String, Object> properties;

        public PathProperties(
                String uri,
                Map<String, Object> properties
        ) {
            this.uri = uri;
            this.properties = properties;
        }

    }


}
