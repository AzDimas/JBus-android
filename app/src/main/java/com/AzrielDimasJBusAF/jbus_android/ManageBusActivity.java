package com.AzrielDimasJBusAF.jbus_android;
//// Import Statement
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.AzrielDimasJBusAF.jbus_android.model.BaseResponse;
import com.AzrielDimasJBusAF.jbus_android.model.Bus;
import com.AzrielDimasJBusAF.jbus_android.request.BaseApiService;
import com.AzrielDimasJBusAF.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The ManageBusActivity class manages the functionality related to managing buses.
 */
public class ManageBusActivity extends AppCompatActivity {
    // Variable declarations
    private BaseApiService mApiService;
    private AddedBusArrayAdapter adapter;
    private ListView busListView;
    private List<Bus> listBus = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);
        getSupportActionBar().setTitle("Manage Bus");
        mContext = this;
        mApiService = UtilsApi.getApiService();
        busListView = this.findViewById(R.id.my_bus_list_view);

        ShowMyBus();
    }

    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    private void ShowMyBus() {
        mApiService.getMyBus(LoginActivity.loggedAccount.id).enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (!response.isSuccessful()) return;

                List<Bus> myBusList = response.body();

                AddedBusArrayAdapter adapter = new AddedBusArrayAdapter(mContext, myBusList);
                busListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {

            }
        });
    }

    private class AddedBusArrayAdapter extends ArrayAdapter<Bus> {

        public AddedBusArrayAdapter(@NonNull Context context, @NonNull List<Bus> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View currentItemView = convertView;

            // of the recyclable view is null then inflate the custom layout for the same
            if (currentItemView == null) {
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.my_bus_view, parent, false);
            }

            // get the position of the view from the ArrayAdapter
            Bus currentBus = getItem(position);

            // then according to the position of the view assign the desired TextView 1 for the same
            TextView busName = currentItemView.findViewById(R.id.bus_name);
            busName.setText(currentBus.name);

            ImageView addSched = currentItemView.findViewById(R.id.manage_schedule);
            addSched.setOnClickListener(v->{
                Intent i = new Intent(mContext, ManageBusSchedule.class);
                i.putExtra("busId", currentBus.id);
                mContext.startActivity(i);
            });

            return currentItemView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflating the options menu layout
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_managemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handling item clicks from the options menu
        if (item.getItemId() == R.id.add_button){
            Intent intent = new Intent(this, AddBusActivity.class);
            startActivity(intent);
        } return super.onOptionsItemSelected(item);
    }
}