package guru.sfg.brewery.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Created by jt on 2019-06-09. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerDto {
  private UUID id = null;
  private Integer version = null;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
  private OffsetDateTime createdDate = null;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
  private OffsetDateTime lastModifiedDate = null;

  private String beerName;
  private String beerStyle;
  private String upc;
  private Integer quantityOnHand;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal price;
}
