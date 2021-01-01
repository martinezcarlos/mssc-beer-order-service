package guru.sfg.beer.order.service.statemachine;

import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import java.util.EnumSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;

@Configuration
@EnableStateMachineFactory
public class BeerOrderStateMachineConfig
    extends StateMachineConfigurerAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

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
}
