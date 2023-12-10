package com.AzrielDimasJBusAF.jbus_android.model;

/**
 * Represents a base response from the API.
 *
 * @param <T> The type of payload included in the response.
 */
public class BaseResponse<T> {
    public boolean success; // Indicates whether the response was successful
    public String message; // Message accompanying the response
    public T payload; // Payload containing data in the response

}
