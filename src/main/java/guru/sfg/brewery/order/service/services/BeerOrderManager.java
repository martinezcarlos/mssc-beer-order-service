package guru.sfg.brewery.order.service.services;

import java.util.UUID;

import guru.sfg.brewery.order.service.domain.BeerOrder;

public interface BeerOrderManager {

  BeerOrder newBeerOrder(BeerOrder beerOrder);

  void processValidationResult(UUID orderId, Boolean isValid);
}
