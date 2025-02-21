/*
 *  Copyright 2019 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package guru.sfg.brewery.order.service.domain;

import java.sql.Timestamp;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Created by jt on 2019-01-26. */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class BeerOrderLine extends BaseEntity {

  @ManyToOne private BeerOrder beerOrder;
  private UUID beerId;
  private String upc;
  private Integer orderQuantity = 0;
  private Integer quantityAllocated = 0;

  @Builder
  public BeerOrderLine(
      final UUID id,
      final Long version,
      final Timestamp createdDate,
      final Timestamp lastModifiedDate,
      final BeerOrder beerOrder,
      final UUID beerId,
      final String upc,
      final Integer orderQuantity,
      final Integer quantityAllocated) {
    super(id, version, createdDate, lastModifiedDate);
    this.beerOrder = beerOrder;
    this.beerId = beerId;
    this.upc = upc;
    this.orderQuantity = orderQuantity;
    this.quantityAllocated = quantityAllocated;
  }
}
