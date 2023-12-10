package com.AzrielDimasJBusAF.jbus_android;

// Import statement
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.AzrielDimasJBusAF.jbus_android.model.Bus;
import com.AzrielDimasJBusAF.jbus_android.model.Facility;

public class BusDetailActivity extends AppCompatActivity {
    // UI Element Declarations
    public static Bus busClick;
    ToggleButton ac,wifi,toilet,lcdTv,coolBox,lunch,largeBaggage,electricSocket;
    TextView busNameTextView,capacityTextView,priceTextView,
            departureTextView,arrivalTextView,busTypeTextView;
    Button orderButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);

        // Retrieve the clicked Bus object from the listBus in MainActivity
        busClick = MainActivity.listBus.get(MainActivity.busIndex-1);

        // Initialize views based on IDs
        // Set values according to the clicked Bus object data
        busNameTextView = findViewById(R.id.detail_nametext);
        priceTextView = findViewById(R.id.detail_pricetext);
        departureTextView = findViewById(R.id.detail_stationdeparture);
        arrivalTextView = findViewById(R.id.detail_stationarrival);
        busTypeTextView = findViewById(R.id.detail_bustype);
        ac =findViewById(R.id.detail_ac);
        wifi =findViewById(R.id.detail_wifi);
        toilet =findViewById(R.id.detail_toilet);
        lcdTv =findViewById(R.id.detail_lcdtv);
        coolBox =findViewById(R.id.detail_coolbox);
        lunch =findViewById(R.id.detail_lunch);
        largeBaggage =findViewById(R.id.detail_largebaggage);
        electricSocket =findViewById(R.id.detail_electricsocket);
        orderButton = findViewById(R.id.detail_bookbutton);

        // Set values in TextViews based on data from the clicked Bus object
        busTypeTextView.setText(busClick.busType.toString());

        // Toggle the CheckBoxes based on the facilities of the clicked Bus
        if(busClick.facilities.contains(Facility.AC)){
            ac.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.WIFI)){
            wifi.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.TOILET)){
            toilet.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.LCD_TV)){
            lcdTv.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.COOL_BOX)){
            coolBox.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.LUNCH)){
            lunch.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.LARGE_BAGGAGE)){
            largeBaggage.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.ELECTRIC_SOCKET)){
            electricSocket.setChecked(true);
        }

        String price = "Rp. " + busClick.price.price;
        priceTextView.setText(price);
        busNameTextView.setText(busClick.name);
        busTypeTextView.setText(busClick.busType.toString());
        departureTextView.setText( busClick.departure.toString());
        arrivalTextView.setText(busClick.arrival.toString());
        String capacityValue = String.valueOf(busClick.capacity);
        TextView capacityTextView = findViewById(R.id.detail_capacity);
        capacityTextView.setText(capacityValue);

        // Click event for orderButton to create an intent to MakeBookingActivity
        orderButton.setOnClickListener(v ->{
            Intent intent = new Intent(BusDetailActivity.this, MakeBookingActivity.class);
            startActivity(intent);
        });

    }

}