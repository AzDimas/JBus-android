package com.AzrielDimasJBusAF.jbus_android.model;

/**
 * Represents an account entity with user details.
 */
public class Account extends Serializable {
    public String name; // Name of the account holder
    public String email; // Email address associated with the account
    public String password; // Password for the account
    public double balance; // Current balance in the account
    public Renter company; // Renter details associated with the account
}
