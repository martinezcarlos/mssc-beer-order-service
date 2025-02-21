package guru.sfg.brewery.order.service.services.beer;

import guru.sfg.brewery.model.BeerDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** Created by jt on 2019-06-09. */
@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@Service
public class BeerServiceImpl implements BeerService {
  public final String BEER_PATH_V1 = "/api/v1/beer/";
  public final String BEER_UPC_PATH_V1 = "/api/v1/beerUpc/";
  private final RestTemplate restTemplate;

  private String beerServiceHost;

  public BeerServiceImpl(final RestTemplateBuilder restTemplateBuilder) {
    restTemplate = restTemplateBuilder.build();
  }

  @Override
  public Optional<BeerDto> getBeerById(final UUID uuid) {
    return Optional.of(
        restTemplate.getForObject(beerServiceHost + BEER_PATH_V1 + uuid.toString(), BeerDto.class));
  }

  @Override
  public Optional<BeerDto> getBeerByUpc(final String upc) {
    return Optional.of(
        restTemplate.getForObject(beerServiceHost + BEER_UPC_PATH_V1 + upc, BeerDto.class));
  }

  public void setBeerServiceHost(final String beerServiceHost) {
    this.beerServiceHost = beerServiceHost;
  }
}
