package com.AzrielDimasJBusAF.jbus_android.request;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class responsible for creating and configuring the Retrofit client.
 */
public class RetrofitClient {
    private static Retrofit retrofit = null;
    /**
     * Creates and configures the Retrofit instance.
     *
     * @param baseUrl Base URL for the Retrofit instance
     * @return Retrofit instance
     */
    public static Retrofit getClient(String baseUrl){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient()) // Configures OkHttpClient
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()) // Configures GsonConverterFactory
                    .build();
        }
        return retrofit;
    }

    /**
     * Configures and returns an OkHttpClient instance with a network interceptor.
     *
     * @return OkHttpClient instance with a network interceptor
     */
    private static OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    // Adds a header to the request
                    Request newRequest = originalRequest.newBuilder()
                            .addHeader("Client-Name", "Azriel")
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();
    }
}
