package com.AzrielDimasJBusAF.jbus_android;

//Import statement
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.AzrielDimasJBusAF.jbus_android.model.BaseResponse;
import com.AzrielDimasJBusAF.jbus_android.model.Renter;
import com.AzrielDimasJBusAF.jbus_android.request.BaseApiService;
import com.AzrielDimasJBusAF.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity responsible for registering a new renter (company).
 */
public class RegisterRenterActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private EditText companyName, address, phoneNumber;
    private CardView notRegistered, registered;
    private Button registerButtonCompany = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        companyName = findViewById(R.id.company_name);
        address = findViewById(R.id.company_address);
        phoneNumber = findViewById(R.id.company_number);
        registerButtonCompany = findViewById(R.id.company_register_button);

        registerButtonCompany.setOnClickListener(v -> handleRegisterRenter());
    }

    /**
     * Handles the registration of a new renter.
     */
    protected void handleRegisterRenter(){
        String companyNameS = companyName.getText().toString();
        String addresS = address.getText().toString();
        String phoneNumberS = phoneNumber.getText().toString();

        if (companyNameS.isEmpty() || addresS.isEmpty() || phoneNumberS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Sends the registration request to the server
        mApiService.registerRenter(LoginActivity.loggedAccount.id,   companyNameS, addresS, phoneNumberS).enqueue(new Callback<BaseResponse<Renter>>(){
            @Override
            public void onResponse(Call<BaseResponse<Renter>> call, Response<BaseResponse<Renter>> response){
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Renter> res = response.body();

                if (res.success){
                    LoginActivity.loggedAccount.company = res.payload;
                    moveActivity(mContext, AboutMeActivity.class); // Moves to AboutMeActivity on successful registration
                }

                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<Renter>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Moves to another activity.
     *
     * @param ctx The context.
     * @param cls The class to move to.
     */
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);

    }
}