package guru.sfg.brewery.order.service.bootstrap;

import guru.sfg.brewery.order.service.domain.Customer;
import guru.sfg.brewery.order.service.repositories.CustomerRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Created by jt on 2019-06-06. */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderBootStrap implements CommandLineRunner {
  public static final String TASTING_ROOM = "Tasting Room";
  public static final String BEER_1_UPC = "0631234200036";
  public static final String BEER_2_UPC = "0631234300019";
  public static final String BEER_3_UPC = "0083783375213";

  private final CustomerRepository customerRepository;

  @Override
  public void run(final String... args) throws Exception {
    loadCustomerData();
  }

  private void loadCustomerData() {
    if (customerRepository.count() == 0) {
      final Customer savedCustomer =
          customerRepository.save(
              Customer.builder().customerName(TASTING_ROOM).apiKey(UUID.randomUUID()).build());

      log.debug("Tasting Room Customer Id: " + savedCustomer.getId().toString());
    }
  }
}
