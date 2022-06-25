package order.service.impl;

import fr.laposte.atlascontenants.back.libwebclient.application.ContenantsWebClient;
import fr.laposte.atlascontenants.back.libwebclient.config.WebClientProperties;
import order.dto.ProductDto;
import order.service.IOrderService;

public class OrderService implements IOrderService {

    private final WebClientProperties webClientProperties;

    private final ContenantsWebClient contenantsWebClient;

    public OrderService(WebClientProperties webClientProperties, ContenantsWebClient contenantsWebClient){
        this.webClientProperties=webClientProperties;
        this.contenantsWebClient=contenantsWebClient;
    }

    @Override
    public Boolean isProductAvailable(ProductDto product) {

        //contenantsWebClient.get("")
        return null;
    }
}
