package guru.sfg.brewery.order.service.services;

import guru.sfg.brewery.order.service.domain.BeerOrder;

public interface BeerOrderManager {

  BeerOrder newBeerOrder(BeerOrder beerOrder);
}
