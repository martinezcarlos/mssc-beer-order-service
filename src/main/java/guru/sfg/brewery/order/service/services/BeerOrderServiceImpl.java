/*
 *  Copyright 2019 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package guru.sfg.brewery.order.service.services;

import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderPagedList;
import guru.sfg.brewery.order.service.domain.BeerOrder;
import guru.sfg.brewery.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.brewery.order.service.domain.Customer;
import guru.sfg.brewery.order.service.repositories.BeerOrderRepository;
import guru.sfg.brewery.order.service.repositories.CustomerRepository;
import guru.sfg.brewery.order.service.web.mappers.BeerOrderMapper;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class BeerOrderServiceImpl implements BeerOrderService {

  private final BeerOrderRepository beerOrderRepository;
  private final CustomerRepository customerRepository;
  private final BeerOrderMapper beerOrderMapper;
  private final ApplicationEventPublisher publisher;

  public BeerOrderServiceImpl(
      final BeerOrderRepository beerOrderRepository,
      final CustomerRepository customerRepository,
      final BeerOrderMapper beerOrderMapper,
      final ApplicationEventPublisher publisher) {
    this.beerOrderRepository = beerOrderRepository;
    this.customerRepository = customerRepository;
    this.beerOrderMapper = beerOrderMapper;
    this.publisher = publisher;
  }

  @Override
  public BeerOrderPagedList listOrders(final UUID customerId, final Pageable pageable) {
    final Optional<Customer> customerOptional = customerRepository.findById(customerId);

    if (customerOptional.isPresent()) {
      final Page<BeerOrder> beerOrderPage =
          beerOrderRepository.findAllByCustomer(customerOptional.get(), pageable);

      return new BeerOrderPagedList(
          beerOrderPage.stream().map(beerOrderMapper::beerOrderToDto).collect(Collectors.toList()),
          PageRequest.of(
              beerOrderPage.getPageable().getPageNumber(),
              beerOrderPage.getPageable().getPageSize()),
          beerOrderPage.getTotalElements());
    } else {
      return null;
    }
  }

  @Transactional
  @Override
  public BeerOrderDto placeOrder(final UUID customerId, final BeerOrderDto beerOrderDto) {
    final Optional<Customer> customerOptional = customerRepository.findById(customerId);

    if (customerOptional.isPresent()) {
      final BeerOrder beerOrder = beerOrderMapper.dtoToBeerOrder(beerOrderDto);
      beerOrder.setId(null); // should not be set by outside client
      beerOrder.setCustomer(customerOptional.get());
      beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);

      beerOrder.getBeerOrderLines().forEach(line -> line.setBeerOrder(beerOrder));

      final BeerOrder savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);

      log.debug("Saved Beer Order: " + beerOrder.getId());

      // todo impl
      //  publisher.publishEvent(new NewBeerOrderEvent(savedBeerOrder));

      return beerOrderMapper.beerOrderToDto(savedBeerOrder);
    }
    // todo add exception type
    throw new RuntimeException("Customer Not Found");
  }

  @Override
  public BeerOrderDto getOrderById(final UUID customerId, final UUID orderId) {
    return beerOrderMapper.beerOrderToDto(getOrder(customerId, orderId));
  }

  @Override
  public void pickupOrder(final UUID customerId, final UUID orderId) {
    final BeerOrder beerOrder = getOrder(customerId, orderId);
    beerOrder.setOrderStatus(BeerOrderStatusEnum.PICKED_UP);

    beerOrderRepository.save(beerOrder);
  }

  private BeerOrder getOrder(final UUID customerId, final UUID orderId) {
    final Optional<Customer> customerOptional = customerRepository.findById(customerId);

    if (customerOptional.isPresent()) {
      final Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(orderId);

      if (beerOrderOptional.isPresent()) {
        final BeerOrder beerOrder = beerOrderOptional.get();

        // fall to exception if customer id's do not match - order not for customer
        if (beerOrder.getCustomer().getId().equals(customerId)) {
          return beerOrder;
        }
      }
      throw new RuntimeException("Beer Order Not Found");
    }
    throw new RuntimeException("Customer Not Found");
  }
}
