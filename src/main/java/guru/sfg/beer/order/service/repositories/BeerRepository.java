package guru.sfg.beer.order.service.repositories;

import guru.sfg.beer.order.service.config.service.BeerServiceConfig;
import guru.sfg.beer.order.service.utils.Constants;
import guru.sfg.beer.order.service.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BeerRepository {
  private final RestTemplate restTemplate;
  private final BeerServiceConfig serviceConfig;

  public Optional<BeerDto> getBeerById(final UUID id) {
    final Map<String, Object> uriVars = Collections.singletonMap(Constants.BEER_ID, id.toString());
    final URI uri = serviceConfig.getUri(Constants.FETCH_BEER_BY_ID, uriVars);
    return Optional.ofNullable(restTemplate.getForObject(uri, BeerDto.class));
  }

  public Optional<BeerDto> getBeerByUpc(final String upc) {
    final Map<String, Object> uriVars = Collections.singletonMap(Constants.BEER_UPC, upc);
    final URI uri = serviceConfig.getUri(Constants.FETCH_BEER_BY_UPC, uriVars);
    return Optional.ofNullable(restTemplate.getForObject(uri, BeerDto.class));
  }
}
