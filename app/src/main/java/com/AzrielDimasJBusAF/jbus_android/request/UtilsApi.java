package com.AzrielDimasJBusAF.jbus_android.request;

/**
 * Utility class to provide access to the Retrofit API service.
 */
public class UtilsApi {
    // Base URL for the API
    public static final String BASE_URL_API = "http://10.0.2.2:5000/";
    /**
     * Retrieves the instance of the API service.
     *
     * @return BaseApiService instance created using RetrofitClient
     */
    public static BaseApiService getApiService() {
        // Creates and returns an instance of BaseApiService using RetrofitClient
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }

}
