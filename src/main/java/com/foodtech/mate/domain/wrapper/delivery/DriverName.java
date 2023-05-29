package com.foodtech.mate.domain.wrapper.delivery;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

import static com.foodtech.mate.util.validation.PatternMatcher.isInvalidNameFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class DriverName {

    private final String driverName;

    private DriverName(String driverName) {
        this.driverName = driverName;
    }

    public static DriverName of(String driverName) {

        if (driverName == null || driverName.isBlank()) {
            throw new IllegalArgumentException("배달원 이름을 입력하세요");
        }
        return new DriverName(driverName);
    }
}
