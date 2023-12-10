package com.AzrielDimasJBusAF.jbus_android.model;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Represents a bus schedule including departure time and seat availability.
 */
public class Schedule {
    /** Timestamp for the departure schedule */
    public Timestamp departureSchedule;

    /** Map of seat availability where the key represents seat numbers and value is availability */
    public Map<String, Boolean> seatAvailability;

    /**
     * Returns a formatted string representation of the schedule with departure time and seat availability.
     * @return Formatted string with departure time and available/occupied seats count
     */
    @NonNull
    @Override
    public String toString() {
        // Calculate the count of occupied seats
        int countOccupied = 0;
        for (boolean val : seatAvailability.values()) {
            if (!val) countOccupied++;
        }
        // Calculate total seats available
        int totalSeat = seatAvailability.size();
        // Format the departure time using SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
        // Construct and return the formatted string
        return dateFormat.format(this.departureSchedule.getTime()) + "\t\t" +"[ "+countOccupied + "/" + totalSeat+" ]";
    }
}
