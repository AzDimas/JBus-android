package com.AzrielDimasJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class AboutMeActivity extends AppCompatActivity {

    private TextView isiDariEmail;
    private TextView isiDariUser;
    private TextView isiDariBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        isiDariUser = findViewById(R.id.username);
        isiDariEmail = findViewById(R.id.email);
        isiDariBalance = findViewById(R.id.balance);

        isiDariUser.setText("azriel");
        isiDariEmail.setText("azriel@gmail.com");
        isiDariBalance.setText("Rp999999999");
    }
}