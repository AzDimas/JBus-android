package com.AzrielDimasJBusAF.jbus_android;

// Import statements
import static com.AzrielDimasJBusAF.jbus_android.LoginActivity.loggedAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.AzrielDimasJBusAF.jbus_android.model.BaseResponse;
import com.AzrielDimasJBusAF.jbus_android.request.BaseApiService;
import com.AzrielDimasJBusAF.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity that displays user information and allows for balance top-up.
 */
public class AboutMeActivity extends AppCompatActivity {
    // Variable declarations
    private TextView isiDariEmail;
    private TextView isiDariUser;
    private TextView isiDariBalance;
    private Context mContext;
    private BaseApiService mApiService;
    private EditText topupAmount;
    private Button topupButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        // Initializing variables and views
        mContext = this;
        mApiService = UtilsApi.getApiService();
        isiDariUser = findViewById(R.id.username);
        isiDariEmail = findViewById(R.id.email);
        isiDariBalance = findViewById(R.id.balance);

        // Set user information in respective TextViews
        isiDariUser.setText(loggedAccount.name); // Set username
        isiDariEmail.setText(loggedAccount.email); // Set email
        Double strdouble = new Double(loggedAccount.balance); // Convert balance to Double
        isiDariBalance.setText(strdouble.toString()); // Set balance in TextView

        mContext = this;
        mApiService = UtilsApi.getApiService();
        topupAmount = findViewById(R.id.topup);
        topupButton = findViewById(R.id.ButtonTopUp);
        topupButton.setOnClickListener(v -> handleTopup());

        // Check if user is registered as a company or not
        if (LoginActivity.loggedAccount.company != null) {
            // User is a Renter
            // Set status text
            TextView textView = findViewById(R.id.status);
            textView.setText("You're Already Registered as a Renter");

            // Hide registerRenter and show manageBusButton
            TextView registerRenter = findViewById(R.id.Register_Company);
            registerRenter.setVisibility(View.GONE);


            Button manageBusButton = findViewById(R.id.Manage_Bus);
            manageBusButton.setOnClickListener(v -> {moveActivity(mContext, ManageBusActivity.class);
            });
        } else {
            // User is not a Renter
            // Set status text
            TextView textView = findViewById(R.id.status);
            textView.setText("You're Not Registered as a Renter");

            // Hide manageBusButton and show registerCompany
            Button manageBusButton = findViewById(R.id.Manage_Bus);
            manageBusButton.setVisibility(View.GONE);

            TextView registerCompany = findViewById(R.id.Register_Company);
            registerCompany.setOnClickListener(v -> {moveActivity(mContext, RegisterRenterActivity.class);
            });
        }



    }
    /**
     * Method to handle moving to another activity.
     *
     * @param ctx Context of the current activity.
     * @param cls Class of the activity to be started.
     */
    private void moveActivity(Context ctx, Class<?> cls) {
        // Intent creation and starting the activity
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    /**
     * Method to handle balance top-up.
     */
    protected void handleTopup(){
        // Get top-up amount from EditText
        String topUpAmountS = topupAmount.getText().toString();
        if (topUpAmountS.isEmpty()) {
            // Show toast for empty field
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        double topUpD = Double.valueOf(topUpAmountS);
        mApiService.topUp(LoginActivity.loggedAccount.id, topUpD).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                // Handle response
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Double> res = response.body();
                // if success finish this activity
                if (res.success){
                    // If success, update balance and show toast
                    finish();
                    LoginActivity.loggedAccount.balance += res.payload;
                    startActivity(getIntent());
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }else{
                    // Show toast for failure
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                // Show toast for server problem
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}