package webclient.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class WebClientResponseErrorHandler {

    public Mono<WebClientResponse> handleResponseErrors(Throwable throwable) {

        if (throwable instanceof WebClientResponseException && ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.NOT_FOUND) {

            log.error("");
            //return WebClientResponse.builder().status("Error throws with status code "+HttpStatus.NOT_FOUND).message("**").build();
        }
        return Mono.error(throwable);
    }
}
