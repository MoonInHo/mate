package com.foodtech.mate.repository;

import com.foodtech.mate.domain.entity.DeliveryCompany;
import com.foodtech.mate.domain.entity.Order;
import com.foodtech.mate.domain.wrapper.delivery.Company;
import com.foodtech.mate.dto.delivery.DeliveryDetailResponseDto;
import com.foodtech.mate.dto.delivery.DeliveryTrackingResponseDto;
import com.foodtech.mate.dto.order.CompletedOrderResponseDto;
import com.foodtech.mate.dto.order.PreparingOrderResponseDto;
import com.foodtech.mate.dto.order.WaitingOrderResponseDto;
import com.foodtech.mate.enums.state.DeliveryState;
import com.foodtech.mate.enums.state.OrderState;
import com.foodtech.mate.enums.type.OrderType;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.foodtech.mate.domain.entity.QCustomer.customer;
import static com.foodtech.mate.domain.entity.QDelivery.delivery;
import static com.foodtech.mate.domain.entity.QDeliveryCompany.deliveryCompany;
import static com.foodtech.mate.domain.entity.QDeliveryDriver.deliveryDriver;
import static com.foodtech.mate.domain.entity.QMenu.menu;
import static com.foodtech.mate.domain.entity.QOrder.order;
import static com.foodtech.mate.domain.entity.QOrderDetail.orderDetail;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Order findOrderByOrderId(Long orderId) {
        return queryFactory
                .selectFrom(order)
                .where(order.id.eq(orderId))
                .fetchOne();
    }

    //TODO 코드 합칠수있는 방법 생각해보기
    public List<WaitingOrderResponseDto> findWaitingOrder() {

        Expression<String> customerInfo = getCustomerInfo();
        Expression<String> menuName = getMenuName();

        return queryFactory
                .select(
                        Projections.constructor(
                                WaitingOrderResponseDto.class,
                                order.id,
                                order.orderTimestamp.orderTimestamp,
                                menuName,
                                order.totalPrice,
                                customerInfo,
                                order.customerRequest,
                                order.orderState,
                                order.orderType,
                                order.paymentType
                        )
                )
                .from(order)
                .join(order.customer, customer)
                .where(order.orderState.eq(OrderState.WAITING))
                .orderBy(order.orderTimestamp.orderTimestamp.asc())
                .fetch();
    }

    public List<PreparingOrderResponseDto> findPreparingOrder() {

        Expression<String> customerInfo = getCustomerInfo();
        Expression<String> menuName = getMenuName();

        return queryFactory
                .select(
                        Projections.constructor(
                                PreparingOrderResponseDto.class,
                                order.id,
                                order.orderTimestamp.orderTimestamp,
                                menuName,
                                order.totalPrice,
                                customerInfo,
                                //TODO List로 출력하는 방법
//                                order.orderDetail,
                                order.orderState,
                                order.orderType,
                                order.paymentType
                        )
                )
                .from(order)
                .join(order.customer, customer)
                .where(order.orderState.eq(OrderState.PREPARING))
                .orderBy(order.orderTimestamp.orderTimestamp.asc())
                .fetch();
    }

    public List<CompletedOrderResponseDto> findCompleteOrder() {

        Expression<String> customerInfo = getCustomerInfo();
        Expression<String> menuName = getMenuName();

        return queryFactory
                .select(
                        Projections.constructor(
                                CompletedOrderResponseDto.class,
                                order.orderTimestamp.orderTimestamp,
                                order.orderType,
                                order.paymentType,
                                menuName,
                                order.totalPrice,
                                customerInfo,
                                order.orderState
                        )
                )
                .from(order)
                .join(order.customer, customer)
                .where(order.orderState.eq(OrderState.COMPLETE))
                .fetch();
    }

    public OrderState findOrderStateByOrderId(Long orderId) {
        return queryFactory
                .select(order.orderState)
                .from(order)
                .where(order.id.eq(orderId))
                .fetchOne();
    }

    public Long updateOrderState(Long orderId, OrderState orderStateCode) {
        return queryFactory
                .update(order)
                .set(order.orderState, orderStateCode)
                .where(order.id.eq(orderId))
                .execute();
    }

    public OrderType findOrderTypeByOrderId(Long orderId) {
        return queryFactory
                .select(order.orderType)
                .from(order)
                .where(order.orderType.eq(OrderType.TOGO), order.id.eq(orderId))
                .fetchOne();
    }

    private static Expression<String> getMenuName() {
        Expression<String> menuName =
                ExpressionUtils.as(
                        JPAExpressions
                                .select(Expressions.stringTemplate("group_concat({0})", menu.menuName))
                                .from(orderDetail)
                                .join(orderDetail.menu, menu)
                                .where(orderDetail.order.id.eq(order.id))
                                .groupBy(orderDetail.order.id),
                        "menuNames"
                );
        return menuName;
    }

    private static Expression<String> getCustomerInfo() {
        Expression<String> customerContact = customer.contact.contact;
        Expression<String> address = customer.address.address
                .concat(" ")
                .concat(customer.address.addressDetail);

        Expression<String> customerInfo = Expressions.cases()
                .when(order.orderType.eq(OrderType.DELIVERY))
                .then(address)
                .otherwise(customerContact)
                .as("customerInfo");

        return customerInfo;
    }

    public DeliveryCompany findDeliveryCompanyByCompanyName(Company companyName) {
        return queryFactory
                .selectFrom(deliveryCompany)
                .where(deliveryCompany.company.eq(companyName))
                .fetchOne();
    }

    public List<DeliveryTrackingResponseDto> findDeliveryByDeliveryState() {
        return queryFactory
                .select(
                        Projections.constructor(
                                DeliveryTrackingResponseDto.class,
                                delivery.id,
                                order.orderTimestamp.orderTimestamp,
                                order.orderType,
                                delivery.deliveryCompany.company,
                                order.paymentType,
                                order.totalPrice,
                                delivery.deliveryState
                        )
                )
                .from(order)
                .join(order.delivery, delivery)
                .where(order.orderType.eq(OrderType.DELIVERY), delivery.deliveryState.ne(DeliveryState.COMPLETE))
                .fetch();
    }

    public List<DeliveryDetailResponseDto> findDeliveryDetail(Long deliveryId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                DeliveryDetailResponseDto.class,
                                delivery.deliveryState,
                                deliveryCompany.company,
                                delivery.deliveryTips,
                                deliveryDriver.driverName,
                                deliveryDriver.driverPhone,
                                customer.contact,
                                customer.address,
                                order.orderTimestamp.orderTimestamp,
                                order.orderType
                        )
                )
                .from(delivery)
                .join(delivery.deliveryDriver, deliveryDriver)
                .join(delivery.deliveryCompany, deliveryCompany)
                .join(delivery.order, order)
                .join(order.customer, customer)
                .where(delivery.id.eq(deliveryId))
                .fetch();
    }
}
