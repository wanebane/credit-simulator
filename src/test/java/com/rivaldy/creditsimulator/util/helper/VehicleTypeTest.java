package com.rivaldy.creditsimulator.util.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTypeTest {

    @Test
    @DisplayName("Must success to get VehicleType based on fromString with case sensitive")
    void fromString_ValidValues_ReturnsEnum() {
        assertEquals(VehicleType.MOBIL, VehicleType.fromString("mobil"));
        assertEquals(VehicleType.MOBIL, VehicleType.fromString("MOBIL"));
        assertEquals(VehicleType.MOTOR, VehicleType.fromString("Motor"));
    }

    @Test
    @DisplayName("Must return null if null or not valid input")
    void fromString_InvalidOrNullValues_ReturnsNull() {
        assertNull(VehicleType.fromString(null));
        assertNull(VehicleType.fromString("  "));
        assertNull(VehicleType.fromString("Kapal"));
    }
}