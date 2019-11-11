package guru.sfg.beer.order.service.config.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "services.beer", ignoreUnknownFields = false)
public class BeerServiceConfig extends ServiceConfig {}
