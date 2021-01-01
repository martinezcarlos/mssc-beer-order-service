package guru.sfg.brewery.order.service.services;

import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import guru.sfg.brewery.order.service.bootstrap.BeerOrderBootStrap;
import guru.sfg.brewery.order.service.domain.Customer;
import guru.sfg.brewery.order.service.repositories.BeerOrderRepository;
import guru.sfg.brewery.order.service.repositories.CustomerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TastingRoomService {

  private final CustomerRepository customerRepository;
  private final BeerOrderService beerOrderService;
  private final BeerOrderRepository beerOrderRepository;
  private final List<String> beerUpcs = new ArrayList<>(3);

  public TastingRoomService(
      final CustomerRepository customerRepository,
      final BeerOrderService beerOrderService,
      final BeerOrderRepository beerOrderRepository) {
    this.customerRepository = customerRepository;
    this.beerOrderService = beerOrderService;
    this.beerOrderRepository = beerOrderRepository;

    beerUpcs.add(BeerOrderBootStrap.BEER_1_UPC);
    beerUpcs.add(BeerOrderBootStrap.BEER_2_UPC);
    beerUpcs.add(BeerOrderBootStrap.BEER_3_UPC);
  }

  @Transactional
  @Scheduled(fixedRate = 2000) // run every 2 seconds
  public void placeTastingRoomOrder() {

    final List<Customer> customerList =
        customerRepository.findAllByCustomerNameLike(BeerOrderBootStrap.TASTING_ROOM);

    if (customerList.size() == 1) { // should be just one
      doPlaceOrder(customerList.get(0));
    } else {
      log.error("Too many or too few tasting room customers found");
    }
  }

  private void doPlaceOrder(final Customer customer) {
    final String beerToOrder = getRandomBeerUpc();

    final BeerOrderLineDto beerOrderLine =
        BeerOrderLineDto.builder()
            .upc(beerToOrder)
            .orderQuantity(new Random().nextInt(6)) // todo externalize value to property
            .build();

    final List<BeerOrderLineDto> beerOrderLineSet = new ArrayList<>();
    beerOrderLineSet.add(beerOrderLine);

    final BeerOrderDto beerOrder =
        BeerOrderDto.builder()
            .customerId(customer.getId())
            .customerRef(UUID.randomUUID().toString())
            .beerOrderLines(beerOrderLineSet)
            .build();

    final BeerOrderDto savedOrder = beerOrderService.placeOrder(customer.getId(), beerOrder);
  }

  private String getRandomBeerUpc() {
    return beerUpcs.get(new Random().nextInt(beerUpcs.size() - 0));
  }
}
