package order.service;

import order.dto.ProductDto;

public interface IOrderService {

    Boolean isProductAvailable(ProductDto product);
}
