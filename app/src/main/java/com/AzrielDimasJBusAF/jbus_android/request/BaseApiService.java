package com.AzrielDimasJBusAF.jbus_android.request;
import com.AzrielDimasJBusAF.jbus_android.model.Account;
import com.AzrielDimasJBusAF.jbus_android.model.BaseResponse;
import com.AzrielDimasJBusAF.jbus_android.model.Bus;
import com.AzrielDimasJBusAF.jbus_android.model.BusType;
import com.AzrielDimasJBusAF.jbus_android.model.Facility;
import com.AzrielDimasJBusAF.jbus_android.model.Renter;
import com.AzrielDimasJBusAF.jbus_android.model.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit service interface defining API endpoints and their HTTP methods.
 */
public interface BaseApiService {

    // Retrieves an account by ID
    @GET("account/{id}")
    Call<Account> getAccountbyId (@Path("id") int id);

    // Registers a new account
    @POST("account/register")
    Call<BaseResponse<Account>> register(
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password);

    // Log in an account
    @POST("account/login")
    Call<BaseResponse<Account>> login(
            @Query("email") String email,
            @Query("password") String password);

    // Top up an account balance
    @POST("account/{id}/topUp")
    Call<BaseResponse<Double>> topUp(
            @Path("id") int id,
            @Query("amount") double amount);

    // Registers a renter associated with an account
    @POST("account/{id}/registerRenter")
    Call<BaseResponse<Renter>> registerRenter(
            @Path("id") int id,
            @Query("companyName") String companyName,
            @Query("address") String address,
            @Query("phoneNumber") String phoneNumber);

    // Retrieves buses associated with an account
    @GET("bus/getMyBus")
    Call<List<Bus>> getMyBus(
            @Query("accountId") int accountId
    );

    // Retrieves all stations
    @GET("station/getAll")
    Call<List<Station>> getAllStation();

    // Creates a new bus
    @POST("bus/create")
    Call<BaseResponse<Bus>> create(
            @Query("accountId") int accountId,
            @Query("name") String name,
            @Query("capacity") int capacity,
            @Query("facilities") List<Facility> facilities,
            @Query("busType") BusType busType,
            @Query("price") int price,
            @Query("stationDepartureId") int stationDepartureId,
            @Query("stationArrivalId") int stationArrivalId
    );

    // Retrieves a bus by ID
    @GET("bus/{id}")
    Call<Bus> getBusbyId(@Path("id") int busId);

    // Adds a schedule to a bus
    @POST("bus/addSchedule")
    Call<BaseResponse<Bus>> addSchedule(@Query("busId") int busId,
                                        @Query("time") String time);

    // Retrieves the total number of buses
    @GET("bus/total")
    Call<Integer> numberOfBuses();

    // Retrieves a paginated list of buses
    @GET("bus/page")
    Call<List<Bus>> getBus(@Query("page") int page, @Query("size") int pageSize);

    // Retrieves all buses
    @GET("bus/getAllBus")
    Call<List<Bus>> getAllBus();

}
