package com.rivaldy.creditsimulator.util.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleConditionTest {

    @Test
    @DisplayName("Must success to get VehicleCondition based on fromString with case sensitive")
    void fromString_ValidValues_ReturnsEnum() {
        assertEquals(VehicleCondition.BARU, VehicleCondition.fromString("baru"));
        assertEquals(VehicleCondition.BEKAS, VehicleCondition.fromString("BEKAS"));
    }

    @Test
    @DisplayName("Must return null if null or not valid input")
    void fromString_InvalidOrNullValues_ReturnsNull() {
        assertNull(VehicleCondition.fromString(null));
        assertNull(VehicleCondition.fromString("Rusak"));
    }
}