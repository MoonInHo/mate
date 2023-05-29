package com.foodtech.mate.repository;

import com.foodtech.mate.domain.entity.Delivery;
import com.foodtech.mate.enums.state.DeliveryState;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.foodtech.mate.domain.entity.QDelivery.delivery;
import static com.foodtech.mate.domain.entity.QDeliveryDriver.deliveryDriver;


@Repository
@RequiredArgsConstructor
public class DeliveryQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Delivery findDeliveryByDeliveryId(Long deliveryId) {
        return queryFactory
                .selectFrom(delivery)
                .where(delivery.id.eq(deliveryId))
                .fetchFirst();
    }

    public Long findDeliveryDriverCompanyIdByDeliveryDriverId(Long deliveryDriverId) {
        return queryFactory
                .select(deliveryDriver.deliveryCompany.id)
                .from(deliveryDriver)
                .where(deliveryDriver.id.eq(deliveryDriverId))
                .fetchFirst();
    }

    public Long updateDeliveryStateAndDriverId(Long deliveryId, Long deliveryDriverId) {
        return queryFactory
                .update(delivery)
                .set(delivery.deliveryDriver.id, deliveryDriverId)
                .set(delivery.deliveryState, DeliveryState.DISPATCH)
                .where(delivery.id.eq(deliveryId))
                .execute();
    }

    public Long updateDeliveryState(Long deliveryId, DeliveryState deliveryState) {
        return queryFactory
                .update(delivery)
                .set(delivery.deliveryState, deliveryState)
                .where(delivery.id.eq(deliveryId))
                .execute();
    }
}
