package com.AzrielDimasJBusAF.jbus_android;

// Import statement
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.AzrielDimasJBusAF.jbus_android.model.BaseResponse;
import com.AzrielDimasJBusAF.jbus_android.model.Bus;
import com.AzrielDimasJBusAF.jbus_android.model.BusType;
import com.AzrielDimasJBusAF.jbus_android.model.Facility;
import com.AzrielDimasJBusAF.jbus_android.model.Station;
import com.AzrielDimasJBusAF.jbus_android.request.BaseApiService;
import com.AzrielDimasJBusAF.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for adding a new bus.
 */
public class AddBusActivity extends AppCompatActivity {
    // Variable declarations
    private TextView title;
    private BaseApiService mApiService;
    private Context mContext;
    private BusType[] busType = BusType.values();
    private EditText bus_name, capacity, priceET;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private Spinner busTypeSpinner, departureSpinner, arrivalSpinner;
    private Button addButton;
    private List<Station> stationList = new ArrayList<>();

    private BusType selectedBusType;
    private int selectedDeptStationID;
    private int selectedArrStationID;
    private List<Facility> selectedFacilities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);
        getSupportActionBar().hide();

        // Initializing variables and views
        mContext = this;
        mApiService = UtilsApi.getApiService();

        bus_name = this.findViewById(R.id.bus_name);
        capacity = this.findViewById(R.id.bus_capacity);
        priceET = this.findViewById(R.id.bus_price);
        busTypeSpinner = this.findViewById(R.id.bus_type_dropdown);
        departureSpinner = this.findViewById(R.id.bus_departure_dropdown);
        arrivalSpinner = this.findViewById(R.id.bus_arrival_dropdown);

        acCheckBox = findViewById(R.id.AC);
        wifiCheckBox = findViewById(R.id.Wifi);
        toiletCheckBox = findViewById(R.id.Toilet);
        lcdCheckBox = findViewById(R.id.LCD_TV);
        coolboxCheckBox = findViewById(R.id.Coolbox);
        lunchCheckBox = findViewById(R.id.Lunch);
        baggageCheckBox = findViewById(R.id.Large_baggage);
        electricCheckBox = findViewById(R.id.electric_socket);
        addButton = findViewById(R.id.add_button2);

        // Set up spinner adapters and item selection listeners
        ArrayAdapter adBus = new ArrayAdapter(this, android.R.layout.simple_list_item_1, busType);
        adBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        busTypeSpinner.setAdapter(adBus);
        busTypeSpinner.setOnItemSelectedListener(busTypeOISL);

        // Get station list from the API
        getStationList();
        // Handle add button click
        addButton.setOnClickListener(v -> handleAdd());
    }
    /**
     * Validates user input for adding a new bus.
     *
     * @return boolean value indicating whether the input is valid or not.
     */
    private boolean validateInput(){
        // Check for incomplete fields and similar stations
        updateSelectedFacilities();
        // Show toast messages for incomplete or similar stations
        if (bus_name.getText().toString().isEmpty() || capacity.getText().toString().isEmpty() || priceET.getText().toString().isEmpty() || selectedFacilities.isEmpty()){
            Toast.makeText(mContext, "Incomplete Field!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedDeptStationID == selectedArrStationID){
            Toast.makeText(mContext, "Similar Station!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    /**
     * Handles the addition of a new bus based on user input.
     */
    private void handleAdd(){
        // Validate input before proceeding
        if (!validateInput()) return;

        // Get values from EditText and CheckBoxes
        String busName = bus_name.getText().toString();
        int seatCapacity = Integer.parseInt(capacity.getText().toString());
        int price = Integer.parseInt(priceET.getText().toString());

        // Call API service to create a new bus
        mApiService.create(LoginActivity.loggedAccount.id, busName, seatCapacity, selectedFacilities, selectedBusType, price, selectedDeptStationID, selectedArrStationID).enqueue(new Callback<BaseResponse<Bus>>() {
            // Handle response from API (success or failure)
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                if (!response.isSuccessful()) return;

                BaseResponse<Bus> res = response.body();
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                mContext.startActivity(new Intent(mContext, ManageBusActivity.class));
            }

            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {

            }
        });
    }

    /**
     * Updates the list of selected facilities based on CheckBox state.
     */
    private void updateSelectedFacilities() {
        // Clear the selected facilities list
        selectedFacilities.clear();

        // Check CheckBox state and add selected facilities to the list
        if (acCheckBox.isChecked()) {
            selectedFacilities.add(Facility.AC);
        }

        if (wifiCheckBox.isChecked()) {
            selectedFacilities.add(Facility.WIFI);
        }

        if (toiletCheckBox.isChecked()) {
            selectedFacilities.add(Facility.TOILET);
        }

        if (lcdCheckBox.isChecked()) {
            selectedFacilities.add(Facility.LCD_TV);
        }

        if (coolboxCheckBox.isChecked()) {
            selectedFacilities.add(Facility.COOL_BOX);
        }

        if (lunchCheckBox.isChecked()) {
            selectedFacilities.add(Facility.LUNCH);
        }

        if (baggageCheckBox.isChecked()) {
            selectedFacilities.add(Facility.LARGE_BAGGAGE);
        }

        if (electricCheckBox.isChecked()) {
            selectedFacilities.add(Facility.ELECTRIC_SOCKET);
        }
    }
    /**
     * Retrieves the list of stations from the API and sets up spinners accordingly.
     */
    private void getStationList() {
        // Get all stations from the API
        mApiService.getAllStation().enqueue(new Callback<List<Station>>() {
            // Set up ArrayAdapter for departure and arrival spinners
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if(!response.isSuccessful()) return;

                stationList = response.body();
                List<String> nameStation = new ArrayList<>();
                for(Station station : stationList){
                    nameStation.add(station.stationName);
                }

                // AdapterView item selection listeners
                ArrayAdapter deptBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, nameStation);
                deptBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                departureSpinner.setAdapter(deptBus);
                departureSpinner.setOnItemSelectedListener(deptOISL);

                ArrayAdapter arrBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, nameStation);
                arrBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                arrivalSpinner.setAdapter(arrBus);
                arrivalSpinner.setOnItemSelectedListener(arrOISL);
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {

            }
        });
    }
    AdapterView.OnItemSelectedListener busTypeOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            selectedBusType = busType[position];
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            selectedDeptStationID = stationList.get(position).id;
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    AdapterView.OnItemSelectedListener arrOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            selectedArrStationID = stationList.get(position).id;
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}