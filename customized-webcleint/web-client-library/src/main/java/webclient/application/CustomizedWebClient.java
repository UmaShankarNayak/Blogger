package webclient.application;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import webclient.config.WebClientProperties;

@Slf4j
@Builder
public class CustomizedWebClient implements ICustomizedWebClient {

    private final WebClient webClientWithApim;
    private final WebClient webClientDirectServer;
    private final WebClientProperties properties;

    /**
     * @param webClientWithApim
     * @param webClientDirectServer
     * @param properties
     */
    public CustomizedWebClient(WebClient webClientWithApim, WebClient webClientDirectServer, WebClientProperties properties) {
        this.webClientWithApim = webClientWithApim;
        this.webClientDirectServer = webClientDirectServer;
        this.properties = properties;
    }

    private static Mono<Throwable> logError(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            log.error("Error body {}", ((WebClientResponseException) throwable).getResponseBodyAsString());
        }

        return Mono.error(throwable);

    }

    @Override
    public <T, V> Mono<T> postWithApimAndRetry(V dataPlayLoad, Class<T> type, String path, @Nullable MediaType contentType, @Nullable MediaType accept) {
        Assert.isTrue(this.webClientWithApim != null, "apim must be enabled ");
        if (null == accept) {
            accept = MediaType.ALL;
        }

        return this.webClientWithApim.post()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build()
                )
                .contentType(contentType == null ? MediaType.APPLICATION_JSON : contentType)
                .accept(accept)
                .bodyValue(dataPlayLoad)
                .retrieve()
                .bodyToMono(type)
                .doOnError(CustomizedWebClient::logError)
                .retryWhen(manageRetry());
    }

    public <T, V> Mono<T> postWithApim(V dataPlayLoad, Class<T> type, String path, @Nullable MediaType contentType, @Nullable MediaType accept) {
        Assert.isTrue(this.webClientWithApim != null, "apim must be enabled ");
        if (null == accept) {
            accept = MediaType.ALL;
        }

        return this.webClientWithApim.post()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build()
                )
                .contentType(contentType == null ? MediaType.APPLICATION_JSON : contentType)
                .accept(accept)
                .bodyValue(dataPlayLoad)
                .retrieve()
                .bodyToMono(type)
                .doOnError(CustomizedWebClient::logError);
    }

    @Override
    public <T> Mono<T> getWithApimAndRetry(Class<T> type, String path, MultiValueMap<String, String> params, @Nullable MediaType accept) {
        Assert.isTrue(this.webClientWithApim != null, "apim must be enabled ");
        if (null == accept) {
            accept = MediaType.ALL;
        }

        return this.webClientWithApim.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParams(params)
                        .build()
                )
                .accept(accept)
                .retrieve()
                .bodyToMono(type)
                .doOnError(CustomizedWebClient::logError)
                .retryWhen(manageRetry());
    }

    @Override
    public WebClient getWebClient() {
        return webClientDirectServer;
    }

    @Override
    public WebClient getWebClientWithApim() {
        return webClientWithApim;
    }

    @Override
    public <T, V> Mono<T> postAndRetry(V dataPlayLoad, Class<T> type, String path, @Nullable MediaType contentType, @Nullable MediaType accept) {
        Assert.isTrue(this.webClientDirectServer != null, "direct server must be enabled ");

        if (null == accept) {
            accept = MediaType.ALL;
        }

        return this.webClientDirectServer.post()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build()
                )
                .contentType(contentType == null ? MediaType.APPLICATION_JSON : contentType)
                .accept(accept)
                .bodyValue(dataPlayLoad)
                .retrieve()
                .bodyToMono(type)
                .doOnError(CustomizedWebClient::logError)
                .retryWhen(manageRetry())
                ;
    }

    @Override
    public <T> Mono<T> getAndRetry(Class<T> type, String path, MultiValueMap<String, String> params, @Nullable MediaType accept) {
        Assert.isTrue(this.webClientDirectServer != null, "direct server must be enabled ");

        if (null == accept) {
            accept = MediaType.ALL;
        }

        return this.webClientDirectServer.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParams(params)
                        .build()
                )
                .accept(accept)
                .retrieve()
                .bodyToMono(type)
                .doOnError(CustomizedWebClient::logError)
                .retryWhen(manageRetry());
    }

    @Override
    public <T> Mono<T> get(Class<T> type, String path, MultiValueMap<String, String> params, @Nullable MediaType accept) {
        Assert.isTrue(this.webClientDirectServer != null, "direct server must be enabled ");

        if (null == accept) {
            accept = MediaType.ALL;
        }

        return this.webClientDirectServer.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParams(params)
                        .build()
                )
                .accept(accept)
                .retrieve()
                .bodyToMono(type)
                .doOnError(CustomizedWebClient::logError)
                ;

    }

    @Override
    public <T, V> Mono<T> post(V dataPlayLoad, Class<T> type, String path, @Nullable MediaType contentType, @Nullable MediaType accept) {
        Assert.isTrue(this.webClientDirectServer != null, "direct server must be enabled ");

        if (null == accept) {
            accept = MediaType.ALL;
        }

        return this.webClientDirectServer.post()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build()
                )
                .contentType(contentType == null ? MediaType.APPLICATION_JSON : contentType)
                .accept(accept)
                .bodyValue(dataPlayLoad)
                .retrieve()
                .bodyToMono(type)
                .doOnError(CustomizedWebClient::logError)
                ;
    }

    public Retry manageRetry() {
        return Retry
                .backoff(properties.getRetry().getMaxAttempts(), properties.getRetry().getMinBackoff())
                .doAfterRetry(objectRetryContext -> {
                    if (log.isInfoEnabled()) {
                        log.info("Error occured {}, retrying (attempts {})", objectRetryContext.failure().getLocalizedMessage(), objectRetryContext.totalRetries());
                    }
                });
    }

    @Override
    public <T, V> Mono<T> put(V dataPayload, Class<T> type, String path, MediaType contentType, MediaType accept) {
        Assert.isTrue(this.webClientDirectServer != null, "direct server must be enabled ");

        if (null == accept) {
            accept = MediaType.ALL;
        }

        return this.webClientDirectServer.put()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build()
                )
                .contentType(contentType == null ? MediaType.APPLICATION_JSON : contentType)
                .accept(accept)
                .bodyValue(dataPayload)
                .retrieve()
                .bodyToMono(type)
                .doOnError(CustomizedWebClient::logError)
                ;
    }

    @Override
    public <T> Mono<T> delete(Class<T> type, String path, MultiValueMap<String, String> params, MediaType accept) {
        Assert.isTrue(this.webClientDirectServer != null, "direct server must be enabled ");

        if (null == accept) {
            accept = MediaType.ALL;
        }

        return this.webClientDirectServer.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParams(params)
                        .build()
                )
                .accept(accept)
                .retrieve()
                .bodyToMono(type)
                .doOnError(CustomizedWebClient::logError);
    }


}
