package com.rivaldy.creditsimulator.util.helper;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum VehicleType {
    MOBIL(8.0, "Mobil"),
    MOTOR(9.0, "Motor");

    private final double baseInterestRate;

    @JsonValue
    private final String displayName;

    VehicleType(double baseInterestRate, String displayName){
        this.baseInterestRate = baseInterestRate;
        this.displayName = displayName;
    }

    public static VehicleType fromString(String value){
        if (value == null)
            return null;
        for (VehicleType type : VehicleType.values()){
            if (type.name().equalsIgnoreCase(value.trim())){
                return type;
            }
        }
        return null;
    }
}
