package webclient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

@Slf4j
public class WebClientFilters {
    public static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());

            if (log.isDebugEnabled()) {
                clientRequest.headers()
                        .forEach((name, values) -> values.forEach(value -> log.debug("{}={}", name, value)));
            }
            return Mono.just(clientRequest);

        });
    }

    public static ExchangeFilterFunction logResposneStatus() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response Status {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}
