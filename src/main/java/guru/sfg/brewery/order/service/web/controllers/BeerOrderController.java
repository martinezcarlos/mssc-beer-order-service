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

package guru.sfg.brewery.order.service.web.controllers;

import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderPagedList;
import guru.sfg.brewery.order.service.services.BeerOrderService;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/customers/{customerId}/")
@RestController
public class BeerOrderController {

  private static final Integer DEFAULT_PAGE_NUMBER = 0;
  private static final Integer DEFAULT_PAGE_SIZE = 25;

  private final BeerOrderService beerOrderService;

  public BeerOrderController(final BeerOrderService beerOrderService) {
    this.beerOrderService = beerOrderService;
  }

  @GetMapping("orders")
  public BeerOrderPagedList listOrders(
      @PathVariable("customerId") final UUID customerId,
      @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
      @RequestParam(value = "pageSize", required = false) Integer pageSize) {

    if (pageNumber == null || pageNumber < 0) {
      pageNumber = DEFAULT_PAGE_NUMBER;
    }

    if (pageSize == null || pageSize < 1) {
      pageSize = DEFAULT_PAGE_SIZE;
    }

    return beerOrderService.listOrders(customerId, PageRequest.of(pageNumber, pageSize));
  }

  @PostMapping("orders")
  @ResponseStatus(HttpStatus.CREATED)
  public BeerOrderDto placeOrder(
      @PathVariable("customerId") final UUID customerId,
      @RequestBody final BeerOrderDto beerOrderDto) {
    return beerOrderService.placeOrder(customerId, beerOrderDto);
  }

  @GetMapping("orders/{orderId}")
  public BeerOrderDto getOrder(
      @PathVariable("customerId") final UUID customerId,
      @PathVariable("orderId") final UUID orderId) {
    return beerOrderService.getOrderById(customerId, orderId);
  }

  @PutMapping("/orders/{orderId}/pickup")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void pickupOrder(
      @PathVariable("customerId") final UUID customerId,
      @PathVariable("orderId") final UUID orderId) {
    beerOrderService.pickupOrder(customerId, orderId);
  }
}
