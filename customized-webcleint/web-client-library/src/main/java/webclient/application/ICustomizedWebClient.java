package webclient.application;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import webclient.config.WebClientProperties;

public interface ICustomizedWebClient {

    /**
     * @param directServerWebClient
     * @param apimWebclient
     * @param properties
     * @return
     */
    public static ICustomizedWebClient create(WebClient directServerWebClient, WebClient apimWebclient, WebClientProperties properties) {
        return CustomizedWebClient.builder().
                webClientDirectServer(directServerWebClient).
                webClientWithApim(apimWebclient).
                properties(properties).
                build();
    }

    /**
     * use this method to make a http post request
     * if you configure the required parameters,
     * the proxy and/or basic oauth will be used
     *
     * @param dataPayload
     * @param type
     * @param path
     * @param contentType
     * @param <T>
     * @return
     */
    public <T, V> Mono<T> post(V dataPayload, Class<T> type, String path, @Nullable MediaType contentType, @Nullable MediaType accept);

    /**
     * use this method to make a http post request
     *  if you configure the required parameters,
     *  the proxy and/or basic oauth and apim will be used
     *
     *  will be used
     *
     * @param dataPayload
     * @param type
     * @param path
     * @param contentType
     * @param accept
     * @return
     */

    /**
     * use this method to make a http post request
     * if you configure the required parameters,
     * the proxy and/or basic oauth and/or retry will be used
     *
     * @param dataPayload
     * @param type
     * @param path
     * @param contentType
     * @param accept
     * @return
     */
    public <T, V> Mono<T> postAndRetry(V dataPayload, Class<T> type, String path, @Nullable MediaType contentType, @Nullable MediaType accept);

    /**
     * use this method to make a http post request
     * if you configure the required parameters,
     * the proxy and/or basic oauth and apim and retry will be used
     *
     * @param dataPayload
     * @param type
     * @param path
     * @param contentType
     * @param accept
     * @return
     */
    public <T, V> Mono<T> postWithApim(V dataPayload, Class<T> type, String path, @Nullable MediaType contentType, @Nullable MediaType accept);

    /**
     * use this method to make a http post request
     * if you configure the required parameters,
     * the proxy and/or basic oauth and apim and retry will be used
     *
     * @param dataPayload
     * @param type
     * @param path
     * @param contentType
     * @param accept
     * @return
     */
    public <T, V> Mono<T> postWithApimAndRetry(V dataPayload, Class<T> type, String path, @Nullable MediaType contentType, @Nullable MediaType accept);

    /**
     * use this method to make a http post request
     * if you configure the required parameters,
     * the proxy and/or basic oauth will be used
     *
     * @param type
     * @param path
     * @param params
     * @param accept
     * @return
     */
    public <T> Mono<T> get(Class<T> type, String path, MultiValueMap<String, String> params, @Nullable MediaType accept);

    /**
     * use this method to make a http post request
     *  if you configure the required parameters,
     *  the proxy and/or basic oauth and apim will be used
     *
     *  will be used
     *
     * @param dataPayload
     * @param type
     * @param path
     * @param contentType
     * @param <T>
     * @return
     */

    /**
     * use this method to make a http post request
     * if you configure the required parameters,
     * the proxy and/or basic oauth and/or retry will be used
     *
     * @param type
     * @param path
     * @param params
     * @param accept
     * @return
     */
    public <T> Mono<T> getAndRetry(Class<T> type, String path, MultiValueMap<String, String> params, @Nullable MediaType accept);

    /**
     * use this method to make a http post request
     * if you configure the required parameters,
     * the proxy and/or basic oauth and apim and retry will be used
     *
     * @param type
     * @param path
     * @param params
     * @param accept
     * @return
     */
    public <T> Mono<T> getWithApimAndRetry(Class<T> type, String path, MultiValueMap<String, String> params, @Nullable MediaType accept);

    /**
     * return a web client
     *
     * @return
     */
    public WebClient getWebClient();

    /**
     * return a web client configured with apim
     *
     * @return
     */
    public WebClient getWebClientWithApim();

    public Retry manageRetry();

    /**
     * use this method to make a http put request
     * if you configure the required parameters,
     * the proxy and/or basic oauth and/or retry will be used
     *
     * @param dataPayload
     * @param type
     * @param path
     * @param contentType
     * @param accept
     * @return
     */
    public <T, V> Mono<T> put(V dataPayload, Class<T> type, String path, @Nullable MediaType contentType, @Nullable MediaType accept);

    /**
     * use this method to make a http delete request
     * if you configure the required parameters,
     * the proxy and/or basic oauth and/or retry will be used
     *
     * @param type
     * @param path
     * @param params
     * @param accept
     * @return
     */
    public <T> Mono<T> delete(Class<T> type, String path, MultiValueMap<String, String> params, @Nullable MediaType accept);

}
