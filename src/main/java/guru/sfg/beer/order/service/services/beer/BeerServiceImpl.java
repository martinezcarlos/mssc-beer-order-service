package guru.sfg.beer.order.service.services.beer;

import guru.sfg.beer.order.service.repositories.BeerRepository;
import guru.sfg.beer.order.service.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

  private final BeerRepository beerRepository;

  @Override
  public Optional<BeerDto> getBeerById(final UUID id) {
    return beerRepository.getBeerById(id);
  }

  @Override
  public Optional<BeerDto> getBeerByUpc(final String upc) {
    return beerRepository.getBeerByUpc(upc);
  }
}
