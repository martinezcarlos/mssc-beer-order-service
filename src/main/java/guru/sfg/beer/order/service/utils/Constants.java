package guru.sfg.beer.order.service.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

  ////////// ENDPOINTS //////////

  // Inventory
  public static final String FETCH_BEER_BY_ID = "fetchBeerById";
  public static final String FETCH_BEER_BY_UPC = "fetchBeerByUpc";

  // Variable keys
  public static final String BEER_ID = "beerId";
  public static final String BEER_UPC = "upc";
}
