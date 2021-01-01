package guru.sfg.beer.order.service.web.mappers;

import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.services.beer.BeerService;
import guru.sfg.beer.order.service.web.model.BeerDto;
import guru.sfg.beer.order.service.web.model.BeerOrderLineDto;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/** Created by jt on 2019-06-09. */
public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper {

  private BeerService beerService;
  private BeerOrderLineMapper beerOrderLineMapper;

  @Autowired
  public void setBeerService(final BeerService beerService) {
    this.beerService = beerService;
  }

  @Autowired
  @Qualifier("delegate")
  public void setBeerOrderLineMapper(final BeerOrderLineMapper beerOrderLineMapper) {
    this.beerOrderLineMapper = beerOrderLineMapper;
  }

  @Override
  public BeerOrderLineDto beerOrderLineToDto(final BeerOrderLine line) {
    final BeerOrderLineDto orderLineDto = beerOrderLineMapper.beerOrderLineToDto(line);
    final Optional<BeerDto> beerDtoOptional = beerService.getBeerByUpc(line.getUpc());

    beerDtoOptional.ifPresent(
        beerDto -> {
          orderLineDto.setBeerName(beerDto.getBeerName());
          orderLineDto.setBeerStyle(beerDto.getBeerStyle());
          orderLineDto.setPrice(beerDto.getPrice());
          orderLineDto.setBeerId(beerDto.getId());
        });

    return orderLineDto;
  }
}
