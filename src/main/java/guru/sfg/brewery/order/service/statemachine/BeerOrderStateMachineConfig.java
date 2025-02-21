package guru.sfg.brewery.order.service.statemachine;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import guru.sfg.brewery.order.service.domain.BeerOrderEventEnum;
import guru.sfg.brewery.order.service.domain.BeerOrderStatusEnum;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class BeerOrderStateMachineConfig
    extends StateMachineConfigurerAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

  private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> validateOrderAction;

  @Override
  public void configure(
      final StateMachineStateConfigurer<BeerOrderStatusEnum, BeerOrderEventEnum> states)
      throws Exception {
    states
        .withStates()
        .initial(BeerOrderStatusEnum.NEW)
        .states(EnumSet.allOf(BeerOrderStatusEnum.class))
        .end(BeerOrderStatusEnum.PICKED_UP)
        .end(BeerOrderStatusEnum.DELIVERED)
        .end(BeerOrderStatusEnum.DELIVERY_EXCEPTION)
        .end(BeerOrderStatusEnum.VALIDATION_EXCEPTION)
        .end(BeerOrderStatusEnum.ALLOCATION_EXCEPTION);
  }

  @Override
  public void configure(
      final StateMachineTransitionConfigurer<BeerOrderStatusEnum, BeerOrderEventEnum> transitions)
      throws Exception {
    transitions
        .withExternal()
        .source(BeerOrderStatusEnum.NEW)
        .target(BeerOrderStatusEnum.VALIDATION_PENDING)
        .event(BeerOrderEventEnum.VALIDATE_ORDER)
        .action(validateOrderAction)
        .and()
        .withExternal()
        .source(BeerOrderStatusEnum.NEW)
        .target(BeerOrderStatusEnum.VALIDATED)
        .event(BeerOrderEventEnum.VALIDATION_PASSED)
        .and()
        .withExternal()
        .source(BeerOrderStatusEnum.NEW)
        .target(BeerOrderStatusEnum.VALIDATION_EXCEPTION)
        .event(BeerOrderEventEnum.VALIDATION_FAILED);
  }
}
