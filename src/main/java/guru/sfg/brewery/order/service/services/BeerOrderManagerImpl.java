package guru.sfg.brewery.order.service.services;

import guru.sfg.brewery.order.service.domain.BeerOrder;
import guru.sfg.brewery.order.service.domain.BeerOrderEventEnum;
import guru.sfg.brewery.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.brewery.order.service.repositories.BeerOrderRepository;
import guru.sfg.brewery.order.service.statemachine.BeerOrderStateChangeInterceptor;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeerOrderManagerImpl implements BeerOrderManager {

  public static final String ORDER_ID_HEADER = "ORDER_ID_HEADER";
  private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachineFactory;
  private final BeerOrderRepository beerOrderRepository;
  private final BeerOrderStateChangeInterceptor interceptor;

  @Override
  public BeerOrder newBeerOrder(final BeerOrder beerOrder) {
    return Optional.ofNullable(beerOrder)
        .map(this::sanitizeOrder)
        .map(this::saveOrderAndNotify)
        .orElseThrow(() -> new IllegalArgumentException("BeerOder cannot be null"));
  }

  private BeerOrder sanitizeOrder(final BeerOrder beerOrder) {
    beerOrder.setId(null);
    beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);
    return beerOrder;
  }

  private BeerOrder saveOrderAndNotify(final BeerOrder beerOrder) {
    final BeerOrder savedOrder = beerOrderRepository.save(beerOrder);
    sendBeerOrderEvent(savedOrder, BeerOrderEventEnum.VALIDATE_ORDER);
    return savedOrder;
  }

  private void sendBeerOrderEvent(final BeerOrder beerOrder, final BeerOrderEventEnum eventEnum) {
    final StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm = build(beerOrder);
    final Message<BeerOrderEventEnum> msg =
        MessageBuilder.withPayload(eventEnum)
            .setHeader(ORDER_ID_HEADER, beerOrder.getId().toString())
            .build();
    sm.sendEvent(msg);
  }

  private StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> build(final BeerOrder beerOrder) {
    final StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm =
        stateMachineFactory.getStateMachine(beerOrder.getId());
    sm.stop();
    sm.getStateMachineAccessor()
        .doWithAllRegions(
            sma -> {
              sma.addStateMachineInterceptor(interceptor);
              sma.resetStateMachine(
                  new DefaultStateMachineContext<>(beerOrder.getOrderStatus(), null, null, null));
            });
    sm.start();
    return sm;
  }
}
