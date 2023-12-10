package com.AzrielDimasJBusAF.jbus_android;

// Import statement
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.AzrielDimasJBusAF.jbus_android.model.Bus;

import java.util.List;

/**
 * Custom ArrayAdapter for displaying Bus objects in a ListView or Spinner.
 */
public class BusArrayAdapter extends ArrayAdapter<Bus> {
    private Context context;
    private List<Bus> busList;

    /**
     * Constructor for BusArrayAdapter.
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
     * @param busList The list of Bus objects to represent in the ListView or Spinner.
     */
    public BusArrayAdapter(@NonNull Context context, int resource, @NonNull List<Bus> busList) {
        super(context, resource, busList);
        this.context = context;
        this.busList = busList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            // Inflate the layout for each list item if it's not already inflated
            convertView = LayoutInflater.from(context).inflate(R.layout.bus_view, parent, false);
        }

        // Get the Bus object for the current position
        Bus currentBus = busList.get(position);

        // Set the bus name to the TextView in the layout
        TextView busNameTextView = convertView.findViewById(R.id.bistxt);
        busNameTextView.setText(currentBus.name);

        return convertView;
    }
}
