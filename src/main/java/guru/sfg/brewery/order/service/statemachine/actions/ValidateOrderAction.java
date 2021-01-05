package guru.sfg.brewery.order.service.statemachine.actions;

import java.util.Optional;
import java.util.UUID;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.events.ValidateOrderRequest;
import guru.sfg.brewery.order.service.config.JmsConfig;
import guru.sfg.brewery.order.service.domain.BeerOrderEventEnum;
import guru.sfg.brewery.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.brewery.order.service.repositories.BeerOrderRepository;
import guru.sfg.brewery.order.service.services.BeerOrderManagerImpl;
import guru.sfg.brewery.order.service.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class ValidateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

  private final BeerOrderRepository beerOrderRepository;
  private final BeerOrderMapper beerOrderMapper;
  private final JmsTemplate jmsTemplate;

  @Override
  public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
    final String beerOrderId =
        Optional.of(stateContext)
            .map(StateContext::getMessage)
            .map(Message::getHeaders)
            .map(h -> h.get(BeerOrderManagerImpl.ORDER_ID_HEADER))
            .map(Object::toString)
            .orElse("");
    final BeerOrderDto beerOrderDto =
        beerOrderRepository
            .findById(UUID.fromString(beerOrderId))
            .map(beerOrderMapper::beerOrderToDto)
            .orElse(null);
    jmsTemplate.convertAndSend(
        JmsConfig.VALIDATE_ORDER_QUEUE,
        ValidateOrderRequest.builder().beerOrder(beerOrderDto).build());
    log.debug("Sent validation request to queue for order id {}.", beerOrderId);
  }
}
