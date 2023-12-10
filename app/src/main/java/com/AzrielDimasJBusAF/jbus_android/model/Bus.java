package com.AzrielDimasJBusAF.jbus_android.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Bus entity.
 */
public class Bus extends Serializable {
    public int accountId; // Account ID associated with the bus
    public String name; // Name of the bus
    public List<Facility> facilities; // List of facilities available on the bus
    public Price price; // Price details for the bus
    public int capacity; // Maximum seating capacity of the bus
    public BusType busType; // Type of bus
    public Station departure; // Departure station of the bus
    public Station arrival; // Arrival station of the bus
    public List<Schedule> schedules; // List of schedules for the bus

    /**
     * Generates a list of sample buses.
     *
     * @param size Number of sample buses to generate.
     * @return List of sample Bus objects.
     */
    public static List<Bus> sampleBusList(int size) {
        List<Bus> busList = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            Bus bus = new Bus();
            bus.name = "Bus " + i;
            busList.add(bus);
        }

        return busList;
    }

    /**
     * Provides a string representation of the bus.
     *
     * @return String representation (bus name).
     */
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
