package com.AzrielDimasJBusAF.jbus_android;

//Import Statement
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.AzrielDimasJBusAF.jbus_android.model.Account;
import com.AzrielDimasJBusAF.jbus_android.model.BaseResponse;
import com.AzrielDimasJBusAF.jbus_android.request.BaseApiService;
import com.AzrielDimasJBusAF.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * LoginActivity allows users to log into the application.
 */
public class LoginActivity extends AppCompatActivity {

    // UI Elements
    private TextView registerNow = null;
    private Button loginButton = null;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText email, password;
    public static Account loggedAccount; // Static variable to hold the logged-in account details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize necessary variables and UI elements
        mContext = this;
        mApiService = UtilsApi.getApiService();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> handleLogin());

        // Set up the click listener for the "Register Now" TextView to redirect to the RegisterActivity
        registerNow = findViewById(R.id.register_now);
        registerNow.setOnClickListener( v ->{
            moveActivity(this, RegisterActivity.class);
        });

        // Hide the action bar
        getSupportActionBar().hide();
    }

    /**
     * Method to start a new activity.
     *
     * @param ctx The current context.
     * @param cls The class of the activity to be started.
     */
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    /**
     * Method to display a Toast message.
     *
     * @param ctx     The current context.
     * @param message The message to be displayed.
     */
    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles the login process when the login button is clicked.
     */
    protected void handleLogin() {
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();

        // Check if email or password fields are empty
        if (emailS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform login API request
        mApiService.login(emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                if (!response.isSuccessful()) {
                    // Display error message if the application encounters an error
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Account> res = response.body();

                if (res.success) {
                    // If login is successful, store the loggedAccount details and move to MainActivity
                    loggedAccount = res.payload;
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish(); // Finish LoginActivity
                    Toast.makeText(mContext, "Success to Login " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    // Display the login failure message
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                // Display a server connection error message
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}