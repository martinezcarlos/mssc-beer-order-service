package guru.sfg.brewery.order.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/** Created by jt on 2019-07-20. */
@Configuration
public class JmsConfig {
  public static final String VALIDATE_ORDER_QUEUE = "validate-order";
  public static final String VALIDATE_ORDER_RESPONSE_QUEUE = "validate-order-response";

  @Bean // Serialize message content to json using TextMessage
  public MessageConverter jacksonJmsMessageConverter() {
    final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    return converter;
  }
}
