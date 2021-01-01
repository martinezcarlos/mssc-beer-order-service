package guru.sfg.brewery.order.service.statemachine;

import guru.sfg.brewery.order.service.domain.BeerOrder;
import guru.sfg.brewery.order.service.domain.BeerOrderEventEnum;
import guru.sfg.brewery.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.brewery.order.service.repositories.BeerOrderRepository;
import guru.sfg.brewery.order.service.services.BeerOrderManagerImpl;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class BeerOrderStateChangeInterceptor
    extends StateMachineInterceptorAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {
  private final BeerOrderRepository beerOrderRepository;

  @Override
  public void preStateChange(
      final State<BeerOrderStatusEnum, BeerOrderEventEnum> state,
      final Message<BeerOrderEventEnum> message,
      final Transition<BeerOrderStatusEnum, BeerOrderEventEnum> transition,
      final StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine) {
    Optional.ofNullable(message)
        .flatMap(
            msg ->
                Optional.ofNullable(
                        msg.getHeaders().getOrDefault(BeerOrderManagerImpl.ORDER_ID_HEADER, " "))
                    .map(Object::toString))
        .ifPresent(
            id -> {
              log.debug("Saving state for order id: {}, status: {}", id, state.getId());
              final BeerOrder order = beerOrderRepository.getOne(UUID.fromString(id));
              order.setOrderStatus(state.getId());
              beerOrderRepository.saveAndFlush(order);
            });
  }
}
