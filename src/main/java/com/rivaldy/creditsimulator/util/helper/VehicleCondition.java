package com.rivaldy.creditsimulator.util.helper;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum VehicleCondition {
    BARU(0.35, "Baru"),
    BEKAS(0.25, "Bekas");

    private final double minimumDpPercentage;

    @JsonValue
    private final String displayName;

    VehicleCondition(double minimumDpPercentage, String displayName) {
        this.minimumDpPercentage = minimumDpPercentage;
        this.displayName = displayName;
    }

    public static VehicleCondition fromString(String value){
        if (value == null)
            return null;
        for (VehicleCondition condition : VehicleCondition.values()){
            if (condition.name().equalsIgnoreCase(value.trim())){
                return condition;
            }
        }
        return null;
    }
}
