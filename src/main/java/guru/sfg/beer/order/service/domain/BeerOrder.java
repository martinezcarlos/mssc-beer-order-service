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
package guru.sfg.beer.order.service.domain;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/** Created by jt on 2019-01-26. */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class BeerOrder extends BaseEntity {

  private String customerRef;
  @ManyToOne private Customer customer;

  @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL)
  @Fetch(FetchMode.JOIN)
  private Set<BeerOrderLine> beerOrderLines;

  private BeerOrderStatusEnum orderStatus = BeerOrderStatusEnum.NEW;
  private String orderStatusCallbackUrl;

  @Builder
  public BeerOrder(
      final UUID id,
      final Long version,
      final Timestamp createdDate,
      final Timestamp lastModifiedDate,
      final String customerRef,
      final Customer customer,
      final Set<BeerOrderLine> beerOrderLines,
      final BeerOrderStatusEnum orderStatus,
      final String orderStatusCallbackUrl) {
    super(id, version, createdDate, lastModifiedDate);
    this.customerRef = customerRef;
    this.customer = customer;
    this.beerOrderLines = beerOrderLines;
    this.orderStatus = orderStatus;
    this.orderStatusCallbackUrl = orderStatusCallbackUrl;
  }
}
