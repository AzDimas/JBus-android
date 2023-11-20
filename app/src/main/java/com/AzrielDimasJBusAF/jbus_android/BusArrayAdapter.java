package com.AzrielDimasJBusAF.jbus_android;

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

public class BusArrayAdapter extends ArrayAdapter<Bus> {
    private Context context;
    private List<Bus> busList;

    public BusArrayAdapter(@NonNull Context context, int resource, @NonNull List<Bus> busList) {
        super(context, resource, busList);
        this.context = context;
        this.busList = busList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.bus_view, parent, false);
        }

        Bus currentBus = busList.get(position);

        TextView busNameTextView = convertView.findViewById(R.id.textView1);
        busNameTextView.setText(currentBus.name);

        return convertView;
    }
}
