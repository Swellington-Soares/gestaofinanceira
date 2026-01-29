package dev.suel.mstransactionapi.infra.mapper;



import dev.suel.mstransactionapi.domain.PageDataDomain;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class PageSortMapper {
    public Sort domainToSort(List<PageDataDomain.SortField> fieldList) {
        if (fieldList == null || fieldList.isEmpty()) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = fieldList.stream().map(
                o -> new Sort.Order(
                        Sort.Direction.fromString(o.order()),
                        o.field()
                )
        ).toList();

        return Sort.by(orders);
    }


    public List<PageDataDomain.SortField> sortToDomain(List<Sort.Order> orders) {
        if (orders == null || orders.isEmpty()) { return List.of(); }

        return orders.stream()
                .map( o ->
                        new PageDataDomain.SortField(o.getProperty(), o.getDirection().name())
                ).toList();
    }
}
