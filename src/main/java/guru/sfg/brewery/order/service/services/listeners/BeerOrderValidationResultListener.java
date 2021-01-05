package guru.sfg.brewery.order.service.services.listeners;

import java.util.UUID;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import guru.sfg.brewery.model.events.ValidateOrderResult;
import guru.sfg.brewery.order.service.config.JmsConfig;
import guru.sfg.brewery.order.service.services.BeerOrderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class BeerOrderValidationResultListener {
  private final BeerOrderManager beerOrderManager;

  @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE)
  public void listen(ValidateOrderResult validateOrderResult) {
    final UUID orderId = validateOrderResult.getOrderId();
    log.debug("Validation result for order id {}", orderId);
    beerOrderManager.processValidationResult(orderId, validateOrderResult.getIsValid());
  }
}
