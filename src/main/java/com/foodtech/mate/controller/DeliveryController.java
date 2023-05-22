package com.foodtech.mate.controller;

import com.foodtech.mate.domain.dto.delivery.DeliveryInfoDto;
import com.foodtech.mate.domain.dto.delivery.DeliveryStateDto;
import com.foodtech.mate.domain.dto.delivery.DeliveryTrackingDto;
import com.foodtech.mate.domain.dto.delivery.RequestDeliveryDto;
import com.foodtech.mate.domain.state.DeliveryState;
import com.foodtech.mate.domain.wrapper.delivery.Company;
import com.foodtech.mate.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.foodtech.mate.domain.state.DeliveryState.findByDeliveryState;
import static com.foodtech.mate.domain.wrapper.delivery.Company.findByCompanyName;

@RestController
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/create-delivery-info")
    public ResponseEntity<String> createDeliveryInfo(@RequestBody RequestDeliveryDto requestDeliveryDto) {

        String inputCompanyName = requestDeliveryDto.getCompanyName();
        Company companyName = findByCompanyName(inputCompanyName);

        deliveryService.createDeliveryInfo(requestDeliveryDto, companyName);

        return ResponseEntity.ok("배달원 배정을 요청했습니다");
    }

    @PutMapping("/delivery-driver-assignment")
    public ResponseEntity<String> deliveryDriverAssignment(@RequestBody RequestDeliveryDto requestDeliveryDto) {

        Long deliveryId = requestDeliveryDto.getDeliveryId();
        Long deliveryDriverId = requestDeliveryDto.getDeliveryDriverId();

        Long update = deliveryService.deliveryDriverAssignment(deliveryId, deliveryDriverId);
        if (update == null) {
            return ResponseEntity.badRequest().body("배달원 배정에 실패하였습니다.");
        }
        return ResponseEntity.ok("배달원 배정되었습니다.");
    }

    @PutMapping("/delivery-pickup")
    public ResponseEntity<String> deliveryPickUp(@RequestBody RequestDeliveryDto requestDeliveryDto) {

        Long deliveryId = requestDeliveryDto.getDeliveryId();
        Long deliveryDriverId = requestDeliveryDto.getDeliveryDriverId();

        deliveryService.deliveryPickUp(deliveryId, deliveryDriverId, DeliveryState.PICKUP);

        return ResponseEntity.ok("배달원이 물품을 픽업했습니다");
    }

    @PutMapping("/delivery-complete")
    public ResponseEntity<String> deliveryComplete(@RequestBody RequestDeliveryDto requestDeliveryDto) {

        Long deliveryId = requestDeliveryDto.getDeliveryId();
        Long deliveryDriverId = requestDeliveryDto.getDeliveryDriverId();

        deliveryService.deliveryComplete(deliveryId, deliveryDriverId, DeliveryState.COMPLETE);

        return ResponseEntity.ok("배달원이 배달을 완료했습니다");
    }

    @PostMapping("/delivery-tracking")
    public List<DeliveryTrackingDto> deliveryTracking(@RequestBody DeliveryStateDto deliveryStateDto) {

        String inputDeliveryState = deliveryStateDto.getDeliveryState();

        DeliveryState deliveryStateCode = findByDeliveryState(inputDeliveryState);
        if (deliveryStateCode == null) {
            throw new IllegalArgumentException("올바르지 않은 요청입니다");
        }

        return deliveryService.deliveryTracking(deliveryStateCode);
    }

    @PostMapping("/delivery-info-inquiry")
    public List<DeliveryInfoDto> deliveryInfoInquiry(@RequestBody DeliveryStateDto deliveryStateDto) {

        Long deliveryId = deliveryStateDto.getDeliveryId();

        return deliveryService.deliveryInfoInquiry(deliveryId);
    }
}
