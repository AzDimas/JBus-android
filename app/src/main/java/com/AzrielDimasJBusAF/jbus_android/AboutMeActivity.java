package com.AzrielDimasJBusAF.jbus_android;

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

public class AboutMeActivity extends AppCompatActivity {

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

        mContext = this;
        mApiService = UtilsApi.getApiService();
        isiDariUser = findViewById(R.id.username);
        isiDariEmail = findViewById(R.id.email);
        isiDariBalance = findViewById(R.id.balance);

        isiDariUser.setText(loggedAccount.name);
        isiDariEmail.setText(loggedAccount.email);
        Double strdouble = new Double(loggedAccount.balance);
        isiDariBalance.setText(strdouble.toString());

        mContext = this;
        mApiService = UtilsApi.getApiService();
        topupAmount = findViewById(R.id.topup);
        topupButton = findViewById(R.id.ButtonTopUp);
        topupButton.setOnClickListener(v -> handleTopup());

        if (LoginActivity.loggedAccount.company != null) {
            TextView textView = findViewById(R.id.status);
            textView.setText("You're Already Registered as a Renter");

            TextView registerRenter = findViewById(R.id.Register_Company);
            registerRenter.setVisibility(View.GONE);


            Button manageBusButton = findViewById(R.id.Manage_Bus);
            manageBusButton.setOnClickListener(v -> {moveActivity(mContext, ManageBusActivity.class);
            });
        } else {
            TextView textView = findViewById(R.id.status);
            textView.setText("You're Not Registered as a Renter");

            Button manageBusButton = findViewById(R.id.Manage_Bus);
            manageBusButton.setVisibility(View.GONE);

            TextView registerCompany = findViewById(R.id.Register_Company);
            registerCompany.setOnClickListener(v -> {moveActivity(mContext, RegisterRenterActivity.class);
            });
        }



    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    protected void handleTopup(){
        String topUpAmountS = topupAmount.getText().toString();
        if (topUpAmountS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        double topUpD = Double.valueOf(topUpAmountS);
        mApiService.topUp(LoginActivity.loggedAccount.id, topUpD).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Double> res = response.body();
                // if success finish this activity
                if (res.success){
                    finish();
                    LoginActivity.loggedAccount.balance += res.payload;
                    startActivity(getIntent());
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}