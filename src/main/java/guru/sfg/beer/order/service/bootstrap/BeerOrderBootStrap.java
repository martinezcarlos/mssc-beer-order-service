package guru.sfg.beer.order.service.bootstrap;

import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

/** Created by jt on 2019-06-06. */
@Component
@Log4j2
@RequiredArgsConstructor
public class BeerOrderBootStrap implements CommandLineRunner {
  public static final String TASTING_ROOM = "Tasting Room";
  public static final String BEER_1_UPC = "0631234200036";
  public static final String BEER_2_UPC = "0631234300019";
  public static final String BEER_3_UPC = "0083783375213";

  private final CustomerRepository customerRepository;

  @Override
  public void run(final String... args) {
    loadCustomerData();
  }

  private void loadCustomerData() {
    if (customerRepository.count() == 0) {
      final Customer save =
          customerRepository.save(
              Customer.builder().customerName(TASTING_ROOM).apiKey(UUID.randomUUID()).build());
      log.debug("Tasting room customer id: {}", save.getId().toString());
    }
  }
}
