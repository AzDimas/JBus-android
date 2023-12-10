package com.AzrielDimasJBusAF.jbus_android.model;

import androidx.annotation.NonNull;

/**
 * Represents a station where buses operate.
 */
public class Station extends Serializable {
    /** Name of the station */
    public String stationName;
    /** City where the station is located */
    public City city;
    /** Address of the station */
    public String address;

    /**
     * Provides a string representation of the Station object.
     *
     * @return The name of the station
     */
    @NonNull
    @Override
    public String toString() {
        return stationName;
    }
}